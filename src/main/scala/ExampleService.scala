package janrain.exampleservice

import scala.concurrent.ExecutionContext
import akka.actor.{Props, ActorSystem, Actor, ActorLogging}
import akka.event.Logging
import akka.io.IO
import akka.io.Tcp.Bound
import spray.can.Http
import spray.can.Http.Bind
import spray.routing._

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
      val service = context.actorOf(Props(classOf[ServiceActor]), name = "serviceActor")
      IO(Http) ! Bind(service, "0.0.0.0", 8000)
    }
    case Bound(address) => {
      log.info("Bound address %s".format(address))
    }
  }
}

trait ExampleService extends HttpService {
  val route: Route = {
    complete { "Hello, World!" }
  }
}

class ServiceActor extends Actor with ExampleService {
  def actorRefFactory = context

  def receive = runRoute(route)
}
