package com.wordlist

import akka.actor.{ Actor, ActorLogging, Props }

/* domain */
final case class Word(name: String, description: String, example: String)

object WordActor {

  /* requests */
  final case class WordList()

  /* responses */
  final case class Words(words: Seq[Word])

  def props: Props = Props[WordActor]
}

class WordActor extends Actor with ActorLogging {

  override def receive: Receive = {
    case WordActor.WordList => sender() ! WordActor.Words(Seq())
  }
}
