lazy val akkaHttpVersion = "10.1.8"
lazy val akkaVersion    = "2.5.22"

name := "word-list-app"

val stage = taskKey[Unit]("Stage task")

val Stage = config("stage")

stage := {
  //(package in Compile).value
  (update in Stage).value.allFiles.foreach { f =>
    if (f.getName.matches("hello-world_2.12-0.1.0-SNAPSHOT.jar")) {
      println("copying " + f.getName)
      IO.copyFile(f, baseDirectory.value / "target" / "scala-2.12" / "hello-world_2.12-0.1.0-SNAPSHOT.jar")
    }
  }
}

lazy val root = (project in file(".")).
  settings(
    inThisBuild(List(
      organization    := "com.example",
      scalaVersion    := "2.12.7"
    )),
    name := "hello-world",
    libraryDependencies ++= Seq(
      "com.typesafe.akka" %% "akka-http"            % akkaHttpVersion,
      "com.typesafe.akka" %% "akka-http-spray-json" % akkaHttpVersion,
      "com.typesafe.akka" %% "akka-http-xml"        % akkaHttpVersion,
      "com.typesafe.akka" %% "akka-stream"          % akkaVersion,

      "com.typesafe.akka" %% "akka-http-testkit"    % akkaHttpVersion % Test,
      "com.typesafe.akka" %% "akka-testkit"         % akkaVersion     % Test,
      "com.typesafe.akka" %% "akka-stream-testkit"  % akkaVersion     % Test,
      "org.scalatest"     %% "scalatest"            % "3.0.5"         % Test
    )
  )

