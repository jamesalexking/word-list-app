package com.jkc.dictionary

import scala.concurrent.{Await, ExecutionContext, Future}
import scala.concurrent.duration.Duration
import scala.util.{Failure, Success}
import akka.actor.{ActorRef, ActorSystem, Props}
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Route
import akka.stream.ActorMaterializer
import com.jkc.dictionary.swagger.SwaggerDocService
import com.jkc.dictionary.service.{DictionaryActor, DictionaryService}
import com.github.swagger.akka.SwaggerSite

object QuickstartServer extends App with UserRoutes with SwaggerSite {

  implicit val system: ActorSystem = ActorSystem("helloAkkaHttpServer")
  implicit val materializer: ActorMaterializer = ActorMaterializer()
  implicit val executionContext: ExecutionContext = system.dispatcher

  //actors
  val userRegistryActor: ActorRef = system.actorOf(UserRegistryActor.props, "userRegistryActor")
  val wordsActor = system.actorOf(Props(new DictionaryActor(userRegistryActor)))

  //services
  val wordsService = new DictionaryService(wordsActor)

  lazy val routes: Route = SwaggerDocService.routes ~ swaggerSiteRoute ~ wordsService.route

  val serverBinding: Future[Http.ServerBinding] = Http().bindAndHandle(routes, "127.0.0.1", 8080)

  serverBinding.onComplete {
    case Success(bound) =>
      println(s"Server online at http://${bound.localAddress.getHostString}:${bound.localAddress.getPort}/")
    case Failure(e) =>
      Console.err.println(s"Server could not start!")
      e.printStackTrace()
      system.terminate()
  }

  Await.result(system.whenTerminated, Duration.Inf)
}
