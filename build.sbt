name := "example-service"

version := "0.1-SNAPSHOT"

scalaVersion := "2.10.3"

packageArchetype.java_application

resolvers ++= Seq(
  "spray repo" at "http://repo.spray.io"
)
    
libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor" % "2.3.2",
  "io.spray" % "spray-can" % "1.3.1",
  "io.spray" % "spray-routing" % "1.3.1"
)
