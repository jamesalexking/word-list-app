package com.jkc.dictionary.service

import akka.actor.ActorRef
import akka.http.scaladsl.server.Directives
import akka.pattern.ask
import akka.util.Timeout
import com.jkc.dictionary.DefaultJsonFormats
import io.swagger.v3.oas.annotations.media.{Content, Schema}
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.{Operation, Parameter}
import javax.ws.rs._
import DictionaryActor.{Fetch, FetchAll, RandomWord, Save, Saved, Words}
import com.jkc.dictionary.database.entity.Word
import io.swagger.v3.oas.annotations.enums.ParameterIn
import io.swagger.v3.oas.annotations.parameters.RequestBody

import scala.concurrent.ExecutionContext
import scala.concurrent.duration._

@Path("/dictionary")
class DictionaryService(wordsActor: ActorRef)(implicit executionContext: ExecutionContext)
  extends Directives with DefaultJsonFormats {

  implicit val timeout = Timeout(5.seconds)

  implicit val wordFormat = jsonFormat4(Word)
  implicit val saveFormat = jsonFormat1(Save)
  implicit val savedFormat = jsonFormat1(Saved)
  implicit val wordsFormat = jsonFormat1(Words)
  implicit val randomWordFormat = jsonFormat0(RandomWord)

  val route = fetchWord ~ saveWord ~ all ~ random

  @GET
  @Path("/fetch/{name}")
  @Operation(
    summary = "fetch a word from dictionary",
    description = "fetch a word from dictionary",
    parameters = Array(new Parameter(name = "name", in = ParameterIn.PATH, description = "word to fetch")),
    responses = Array(
      new ApiResponse(responseCode = "200", description = "word is fetched",
        content = Array(new Content(schema = new Schema(implementation = classOf[Saved])))),
      new ApiResponse(responseCode = "500", description = "Internal server error")
    )
  )
  def fetchWord =
    path("dictionary" / "fetch" / Segment) { name =>
      get {
        val f = (wordsActor ? Fetch(name)).mapTo[Word]
        complete { f }
      }
    }

  @GET
  @Path("/random")
  @Operation(
    summary = "fetch a word from dictionary",
    description = "fetch a word from dictionary",
    responses = Array(
      new ApiResponse(responseCode = "200", description = "word is fetched",
        content = Array(new Content(schema = new Schema(implementation = classOf[Saved])))),
      new ApiResponse(responseCode = "500", description = "Internal server error")
    )
  )
  def random =
    path("dictionary" / "random") {
      get {
        complete { (wordsActor ? RandomWord).mapTo[Word] }
      }
    }

  @GET
  @Path("/all")
  @Operation(
    summary = "fetch a word from dictionary",
    description = "fetch a word from dictionary",
    responses = Array(
      new ApiResponse(responseCode = "200", description = "word is fetched",
        content = Array(new Content(schema = new Schema(implementation = classOf[Saved])))),
      new ApiResponse(responseCode = "500", description = "Internal server error")
    )
  )
  def all =
    path("dictionary" / "all") {
      get {
        complete { (wordsActor ? FetchAll).mapTo[Words] }
      }
    }

  @POST
  @Path("/save")
  @Consumes(Array("application/json"))
  @Produces(Array("application/json"))
  @Operation(
    summary = "save a word to dictionary",
    description = "save a word to dictionary",
    requestBody = new RequestBody(content = Array(new Content(schema = new Schema(implementation = classOf[Word])))),
    responses = Array(
      new ApiResponse(responseCode = "200", description = "word is saved",
        content = Array(new Content(schema = new Schema(implementation = classOf[Saved])))),
      new ApiResponse(responseCode = "500", description = "Internal server error")
    )
  )
  def saveWord =
    path("dictionary" / "save") {
      post {
        entity(as[Word]) { word =>
          complete { (wordsActor ? Save(word)).mapTo[Saved] }
        }
      }
    }
}