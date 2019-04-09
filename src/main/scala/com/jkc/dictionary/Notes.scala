package com.jkc.dictionary

class Notes {

  // due to type erasure of inner types for scala.Option[T] when T is a primitive type like Int,
  // the only way to ensure generated swagger doc has int type for number2 is to use an annotation
  // case class AddOptionRequest(number: Int,@Schema(required = false, implementation = classOf[Int]) number2: Option[Int] = None)

}
