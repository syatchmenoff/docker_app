name := "example-service"

version := "0.1-SNAPSHOT"

scalaVersion := "2.10.3"

Revolver.settings

packageArchetype.java_application

resolvers ++= Seq(
  "spray repo" at "http://repo.spray.io"
)
    
libraryDependencies ++= Seq(
  "ch.qos.logback" % "logback-classic" % "1.0.11" % "runtime",
  "com.typesafe.akka" %% "akka-actor" % "2.3.2",
  "com.typesafe.akka" %% "akka-slf4j" % "2.3.2",
  "io.spray" % "spray-can" % "1.3.1",
  "io.spray" % "spray-routing" % "1.3.1",
  "io.spray" %% "spray-json" % "1.2.6"
)
