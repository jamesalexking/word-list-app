package com.jkc.dictionary.database.table

import java.lang.reflect.Field

import com.jkc.dictionary.database.connector.Connector

case class Column(name:String, typeName:String, isPrimaryKey:Boolean = false) {

  def equalsTo(op:String, value:String):Option[String] = {
    val template = equalsTo(value)
    template match {
      case Some(v) => Some(s" $op $v")
      case None => None
    }
  }

  def equalsTo(value:String):Option[String] = Option(value) match {
    case Some(v) => Some(s" $name = '$value' ")
    case _ => None
  }

  def equalsTo(value:Int):Option[String] = {
    equalsTo(value.toString)
  }

  def columnType = {
    val primaryKey = if(isPrimaryKey) " PRIMARY KEY" else ""
    s"$name $typeName $primaryKey".trim
  }
}

case class ColumnTemplate(name:Column, value: Any) {
  def columnName = name.name
  def getValueInt:Int = value.toString.toInt
  def getValueString:String = value.toString
}

trait BaseTable[T] {
  val connector = Connector()
  val tableName: String
  val columns: Seq[Column]
  def columnsNames = columns.map(c => c.name).mkString(", ")

  def values(entity: T) = {
    columns.map { column => {
      entity.getClass.getDeclaredField(column.name) match {
        case f:Field => {
          f.setAccessible(true)
          val value:String = f.get(entity).toString
          if(f.isInstanceOf[Int]) value else value.mkString("'","","'")
        }
        case _ => throw new RuntimeException("")
      }
    }
    }.mkString(",")
  }

  def columnTypes = {
    columns.map(column => column.columnType).mkString(",")
  }

  val WHERE = " WHERE "
  val AND = " AND "

  def selectAll: String = {
    return s"select * from ${Connector.keyspace}.$tableName"
  }

  def insert(newEnity: T): Boolean = {

    val sb: StringBuilder = StringBuilder.newBuilder

    sb.append(s"INSERT INTO ${connector.keyspace}.$tableName ($columnsNames) ")
    sb.append("VALUES (")
    sb.append(values(newEnity))
    sb.append(")")

    val csql = sb.toString
    connector.session.execute(csql).isExhausted
  }

  def update(entity: T): Boolean

  def select(templateEntity: T): Option[T]

  def delete(templateEntity: T): Boolean = {
    val csql = s"DELETE FROM ${connector.keyspace}.$tableName " +
      s"WHERE id = '${entityId(templateEntity)}'"
    connector.session.execute(csql).isExhausted
  }

  def entityId(entity:T): String = {
    entity.getClass.getDeclaredField("id") match {
      case f:Field => {
        f.setAccessible(true)
        f.get(entity).toString
      }
      case _ => throw new RuntimeException("Not a Field")
    }
  }

  def createTable: Boolean

  def dropTable: Boolean = {
    val csql = s"DROP TABLE IF EXISTS ${connector.keyspace}.$tableName"
    connector.session.execute(csql).isExhausted
  }

  def and(value:Any) = Option(value) match {
    case Some(value) => " AND "
    case _ => ""
  }
}
