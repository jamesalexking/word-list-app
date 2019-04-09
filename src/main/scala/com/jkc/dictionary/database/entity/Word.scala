package com.jkc.dictionary.database.entity

case class Word(
                 id: String,
                 name: String = null,
                 description: String = null,
                 example: String= null
               )
