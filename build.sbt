enablePlugins(JavaAppPackaging)

//-----------------------
// Properties
//-----------------------
lazy val akkaHttpVersion = "10.1.8"
lazy val akkaVersion    = "2.5.22"
lazy val sttpV = "1.0.2"
lazy val akkaHttpCirceV = "1.17.0"
lazy val circeV = "0.8.0"


//-----------------------
// Project
//-----------------------
lazy val root = (project in file("."))

  .settings(
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
      "org.scalatest"     %% "scalatest"            % "3.0.5"         % Test,

      // json
      "de.heikoseeberger" %% "akka-http-circe" % akkaHttpCirceV,
      "io.circe" %% "circe-core" % circeV,
      "io.circe" %% "circe-generic" % circeV,
      "io.circe" %% "circe-parser" % circeV,
      "io.circe" %% "circe-jawn" % circeV,

      //
      "com.softwaremill.sttp" %% "core" % sttpV,
      "com.softwaremill.sttp" %% "okhttp-backend" % sttpV,
      "com.softwaremill.sttp" %% "circe" % sttpV


    )
  )

