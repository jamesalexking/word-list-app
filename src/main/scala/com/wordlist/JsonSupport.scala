package com.wordlist

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import spray.json.DefaultJsonProtocol
import com.wordlist.UserRegistryActor.ActionPerformed
import com.wordlist.WordActor.Words

trait JsonSupport extends SprayJsonSupport {
  // import the default encoders for primitive types (Int, String, Lists etc)
  import DefaultJsonProtocol._

  implicit val userJsonFormat = jsonFormat3(User)
  implicit val usersJsonFormat = jsonFormat1(Users)

  implicit val wordJsonFormat = jsonFormat3(Word)
  implicit val wordsJsonFormat = jsonFormat1(Words)

  implicit val actionPerformedJsonFormat = jsonFormat1(ActionPerformed)
}
