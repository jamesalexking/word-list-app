package com.jkc.dictionary.service

import akka.actor.{Actor, ActorLogging, ActorRef}
import com.jkc.dictionary.database.entity.Word

import scala.concurrent.ExecutionContext


object DictionaryActor {
  case class Save(word: Word)
  case class Saved(status: Boolean)
  case class Fetch(word: String)
  case class FetchAll()
  case class Words(wordsSeq: Seq[Word])
  case class RandomWord()
}

class DictionaryActor(userRegistryActor: ActorRef)(implicit executionContext: ExecutionContext) extends Actor with ActorLogging {
  import DictionaryActor._

  def receive: Receive = {
    case Save(word) => {

      ???
    }
    case Fetch(wordName) => {
      ???
    }
    case FetchAll => {
      //sender ! Words(database.WordModel.select.all().)
      ???
    }
    case RandomWord => {
      //sender ! datastore.random().getOrElse(Word("","", "", ""))
      ???
    }
  }
}