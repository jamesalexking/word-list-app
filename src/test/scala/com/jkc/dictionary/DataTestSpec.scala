package com.jkc.dictionary

import java.util.UUID

import akka.http.scaladsl.common.StrictForm.Field
import akka.http.scaladsl.testkit.ScalatestRouteTest
import com.jkc.dictionary.database.connector.Connector
import com.jkc.dictionary.database.entity.Word
import com.jkc.dictionary.database.table.WordTable
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.{Matchers, WordSpec}

class DataTestSpec extends WordSpec with Matchers with ScalaFutures with ScalatestRouteTest {

  "create keyspace" should {
    "create keyspace and tables" in {
      val connector = Connector()
      connector.createKeyspace shouldBe true
      connector.createTables shouldBe true
    }
  }

  "insert new word" should {
    "new word is inserted" in {
      val id = UUID.randomUUID().toString
      val toInsert = Word(id, "test", "to verify", "to test a system")
      val inserted = WordTable.insert(toInsert)
      inserted shouldBe true

      WordTable.delete(Word(id))
    }
  }

  "select word" should {
    "word is fetched" in {
      val id = UUID.randomUUID().toString
      val expectedWord = Word(id, "test", "to verify", "to test a system")
      WordTable.insert(expectedWord)

      val actaulWord = WordTable.select(Word(id))
      actaulWord == expectedWord

      WordTable.delete(Word(id))
    }
  }

  "update word" should {
    "word is updated" in {
      val id = UUID.randomUUID().toString
      val expectedWord = Word(id, "test", "to verify", "to test a system")
      WordTable.insert(expectedWord)

      WordTable.select(Word(id)) match {
        case Some(actaulWord) => {

          actaulWord == expectedWord

          val toUpdate = Word(id, actaulWord.name, actaulWord.description, actaulWord.example + "- update")
          val updated = WordTable.update(toUpdate)
          updated shouldBe true

          WordTable.select(Word(id)) match {
            case Some(actaulUpdatedWord) => {
              actaulUpdatedWord shouldBe toUpdate
            }
            case _ =>
          }
        }
        case _ => fail("word should have been found")
      }

      WordTable.delete(Word(id))
    }
  }

  "delete word" should {
    "word is deleted" in {
      val id = UUID.randomUUID().toString
      val expectedWord = Word(id, "test", "to verify", "to test a system")
      WordTable.insert(expectedWord)

      val actaulWord = WordTable.select(Word(id))
      actaulWord == expectedWord

      val toDelete = Word(id)
      val deleted = WordTable.delete(toDelete)
      deleted shouldBe true

      WordTable.select(Word(id)) match {
        case Some(_) => fail("word should have been deleted")
        case None => /* do nothing */
      }
    }
  }

  "drop keyspace" should {
    "keyspace is dropped" in {

      //WordTable.dropTable
      //Connector().dropKeyspace

      val w = Word("123","abc","efg","hij")

      val f: java.lang.reflect.Field = w.getClass.getDeclaredField("id")

      f.setAccessible(true)

      val v = f.get(w)



    }
  }

}
