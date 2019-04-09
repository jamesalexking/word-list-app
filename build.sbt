import sbt.Resolver

lazy val akkaHttpVersion = "10.1.6"
lazy val akkaVersion    = "2.5.19"
lazy val swaggerV = "2.0.0"
lazy val swaggerUIV = "1.2.1"

resolvers ++= Seq(
  Resolver.sonatypeRepo("releases"),
  Resolver.typesafeRepo("releases"),
  Resolver.bintrayRepo("websudos", "oss-releases")
)

lazy val Versions = new {
  val phantom = "2.14.5"
  val util = "0.30.1"
  val scalatest = "3.0.4"
}

scalacOptions ++= Seq("-Xlog-implicit-conversions")

lazy val root = (project in file(".")).
  settings(
    inThisBuild(List(
      organization    := "com.jkc.dictionary",
      scalaVersion    := "2.12.7"
    )),
    name := "word-dictionary",
    libraryDependencies ++= Seq(
      "com.typesafe.akka" %% "akka-http"            % akkaHttpVersion,
      "com.typesafe.akka" %% "akka-http-spray-json" % akkaHttpVersion,
      "com.typesafe.akka" %% "akka-http-xml"        % akkaHttpVersion,
      "com.typesafe.akka" %% "akka-stream"          % akkaVersion,
      "com.github.swagger-akka-http" %% "swagger-akka-http" % swaggerV,
      "co.pragmati" %% "swagger-ui-akka-http" % swaggerUIV,
      "javax.ws.rs" % "javax.ws.rs-api" % "2.1.1" artifacts Artifact("javax.ws.rs-api", "jar", "jar"),
      "org.slf4j" % "slf4j-api" % "1.7.10",
      "org.slf4j" % "slf4j-simple" % "1.6.0",
      "flatfiledatabase" %% "flatfiledatabase" % "0.5",
      "com.typesafe.akka" %% "akka-persistence-cassandra" % "0.92",
      "com.outworkers"  %%  "phantom-dsl"       % Versions.phantom,
      "com.outworkers"  %%  "phantom-streams"   % Versions.phantom,
      "com.datastax.cassandra" % "cassandra-driver-core" % "3.6.0",

      "com.typesafe.akka" %% "akka-persistence-cassandra-launcher" % "0.92" % Test,
      "com.typesafe.akka" %% "akka-http-testkit"    % akkaHttpVersion % Test,
      "com.typesafe.akka" %% "akka-testkit"         % akkaVersion     % Test,
      "com.typesafe.akka" %% "akka-stream-testkit"  % akkaVersion     % Test,
      "org.scalatest"     %% "scalatest"            % "3.0.5"         % Test,
      "com.outworkers"    %% "util-testing"         % Versions.util    % Test,
      "org.cassandraunit" % "cassandra-unit" % "3.5.0.1" % Test

    )

  )