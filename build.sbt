enablePlugins(JavaAppPackaging)

name := "akka-http-hello-world"
organization := "mu.node"
version := "0.1"
scalaVersion := "2.11.6"
scalacOptions := Seq("-unchecked", "-deprecation", "-encoding", "utf8")
assemblyJarName in assembly := "akka-http-hello-world.jar"

libraryDependencies ++= {
  val akkaStreamVersion = "1.0"
  Seq(
    "com.typesafe.akka" %% "akka-actor" % "2.3.12",
    "com.typesafe.akka" % "akka-stream-experimental_2.11" % akkaStreamVersion,
    "com.typesafe.akka" % "akka-http-core-experimental_2.11" % akkaStreamVersion,
    "com.typesafe.akka" % "akka-http-experimental_2.11" % akkaStreamVersion
  )
}

Revolver.settings
