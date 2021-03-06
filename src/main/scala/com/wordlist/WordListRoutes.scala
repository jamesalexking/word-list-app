package com.wordlist

import akka.actor.{ ActorRef, ActorSystem }
import akka.event.Logging

import de.heikoseeberger.akkahttpcirce.FailFastCirceSupport._
import io.circe.generic.auto._
import scala.concurrent.duration._
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.server.directives.MethodDirectives.delete
import akka.http.scaladsl.server.directives.MethodDirectives.get
import akka.http.scaladsl.server.directives.MethodDirectives.post
import akka.http.scaladsl.server.directives.RouteDirectives.complete
import akka.http.scaladsl.server.directives.PathDirectives.path

import scala.concurrent.Future
import com.wordlist.UserRegistryActor._
import com.wordlist.WordDomain._
import akka.pattern.ask
import akka.util.Timeout

trait WordListRoutes extends JsonSupport {

  implicit def system: ActorSystem

  lazy val log = Logging(system, classOf[WordListRoutes])

  def userRegistryActor: ActorRef
  def wordActor: ActorRef

  // Required by the `ask` (?) method below
  implicit lazy val timeout = Timeout(5.seconds) // usually we'd obtain the timeout from the system's configuration

  lazy val wordRoutes: Route =
    pathPrefix("words") {
      concat(
        pathEnd {
          concat(
            get {
              val wordsFuture: Future[Words] = (wordActor ? WordsRequest).mapTo[Words]
              complete(wordsFuture)
            })
        })
    }

  lazy val userRoutes: Route =
    pathPrefix("users") {
      concat(
        pathEnd {
          concat(
            get {
              val users: Future[Users] =
                (userRegistryActor ? GetUsers).mapTo[Users]
              complete(users)
            },
            post {
              entity(as[User]) { user =>
                val userCreated: Future[ActionPerformed] =
                  (userRegistryActor ? CreateUser(user)).mapTo[ActionPerformed]
                onSuccess(userCreated) { performed =>
                  log.info("Created user [{}]: {}", user.name, performed.description)
                  complete((StatusCodes.Created, performed))
                }
              }
            })
        },
        path(Segment) { name =>
          concat(
            get {
              //#retrieve-user-info
              val maybeUser: Future[Option[User]] =
                (userRegistryActor ? GetUser(name)).mapTo[Option[User]]
              rejectEmptyResponse {
                complete(maybeUser)
              }
              //#retrieve-user-info
            },
            delete {
              val userDeleted: Future[ActionPerformed] =
                (userRegistryActor ? DeleteUser(name)).mapTo[ActionPerformed]
              onSuccess(userDeleted) { performed =>
                log.info("Deleted user [{}]: {}", name, performed.description)
                complete((StatusCodes.OK, performed))
              }
            })
        })
    }
}