package janrain.exampleservice

import scala.reflect.ClassTag
import scala.collection.mutable.ArrayBuffer
import scala.concurrent._
import scala.concurrent.duration._
import akka.actor.{Props, ActorSystem, ActorRef, Actor, ActorLogging}
import akka.event.Logging
import akka.util.Timeout
import akka.io.IO
import akka.io.Tcp.Bound
import akka.pattern.ask
import spray.can.Http
import spray.can.Http.Bind
import spray.routing._
import spray.json._
import spray.httpx.SprayJsonSupport._

object ExampleServiceApp extends App {
  val actorSystem = ActorSystem("exampleService")
  actorSystem.actorOf(Props[BootActor], name = "boot") ! BootActor.Start
  actorSystem.awaitTermination()
}

object BootActor {
  case object Start
}

class BootActor extends Actor with ActorLogging {
  import BootActor._

  implicit val actorSystem = context.system

  def receive = {
    case Start => {
      log.info("Starting BootActor")
      val messageStore = context.actorOf(Props(classOf[MessageStoreActor], new LocalMessageStore), name = "messageStore")
      val service = context.actorOf(Props(classOf[ServiceActor], messageStore), name = "service")
      IO(Http) ! Bind(service, "0.0.0.0", 8000)
    }
    case Bound(address) => {
      log.info("Bound address %s".format(address))
    }
  }
}

object JsonFormats extends DefaultJsonProtocol {
  implicit val messageFormat = jsonFormat2(Message)
}
import JsonFormats._

trait ExampleService extends HttpService {
  implicit def executionContext: ExecutionContext

  def messageStoreActor: ActorRef

  implicit val timeout = Timeout(1.second)

  val route: Route = {
    (path("api" / "v1" / "messages") & get & parameters('after.as[Long], 'count.as[Int])) { (after, count) =>
      complete {
        for {
          resp <- (messageStoreActor ? MessageStoreActor.Get(after, count)).mapTo[MessageStoreActor.GetResponse]
        } yield resp.messages
      }
    } ~
    (path("api" / "v1" / "messages") & post & entity(as[String])) { value =>
      messageStoreActor ! MessageStoreActor.Post(value)
      complete { Map("status" -> "ok") }
    }
  }
}

class ServiceActor(val messageStoreActor: ActorRef) extends Actor with ExampleService {
  def executionContext = context.dispatcher
  def actorRefFactory = context
  def receive = runRoute(route)
}

case class Message(id: Long, value: String)

trait MessageStore {
  def postMessage(value: String): Long
  def getMessages(after: Long, count: Int): List[Message]
}

class LocalMessageStore extends MessageStore {
  val maxCount = 50

  val messages: ArrayBuffer[Message] = ArrayBuffer()

  def postMessage(value: String): Long = {
    val id: Long = messages.lastOption.map(_.id + 1).getOrElse(0)
    messages += Message(id, value)
    if(messages.length > maxCount) {
      messages.remove(0, messages.length - maxCount)
    }
    id
  }

  def getMessages(after: Long, count: Int): List[Message] = {
    messages.view.dropWhile(_.id <= after).take(count).toList
  }
}

object MessageStoreActor {
  case class Post(value: String)
  case class Get(after: Long, count: Int)
  case class GetResponse(messages: List[Message])
}

class MessageStoreActor(store: MessageStore) extends Actor with ActorLogging {
  import MessageStoreActor._

  def receive = {
    case Post(value) => store.postMessage(value)
    case Get(after, count) => sender ! GetResponse(store.getMessages(after, count))
  }
}
