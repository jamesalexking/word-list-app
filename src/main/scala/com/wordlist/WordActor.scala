package com.wordlist

import akka.actor.{ Actor, ActorLogging, Props }


object WordDomain {

  /* domain */
  final case class Word(name: String, description: String, example: String)

  /* requests */
  final case class WordsRequest()

  /* responses */
  final case class Words(words: Seq[Word])

  def props: Props = Props[WordActor]
}

class WordActor extends Actor with ActorLogging {

  override def receive: Receive = {
    case WordDomain.WordsRequest => sender() ! WordDomain.Words(Seq(WordDomain.Word("name","desc","example")))
  }
}
