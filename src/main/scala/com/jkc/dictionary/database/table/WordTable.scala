package com.jkc.dictionary.database.table

import com.jkc.dictionary.database.entity.Word


object WordTable extends BaseTable[Word] {

  override val tableName: String = "Word"

  val id = Column("id", "text", true)
  val name = Column("name", "text")
  val description = Column("description", "text")
  val example = Column("example", "text")

  override val columns: Seq[Column] = Seq(
    id, name, description, example
  )

  override def update(entity: Word): Boolean = {
    val csql = s"update ${connector.keyspace}.$tableName SET " +
      s"name = '${entity.name}', " +
      s"description = '${entity.description}', " +
      s"example='${entity.example}' " +
    s"WHERE id = '${entity.id}'"

    connector.session.execute(csql).isExhausted
  }

  override def select(templateEntity: Word): Option[Word] = {

    val sb: StringBuilder =  StringBuilder.newBuilder

    sb.append(" SELECT ").append(columnsNames)
    sb.append(" FROM ").append(s"${connector.keyspace}.$tableName")
    sb.append(  WHERE  )

    sb.append(id.equalsTo(templateEntity.id).getOrElse(""))
    sb.append(name.equalsTo("and", templateEntity.name).getOrElse(""))
    sb.append(description.equalsTo("and", templateEntity.description).getOrElse(""))
    sb.append(example.equalsTo("and", templateEntity.example).getOrElse(""))

    if(sb.endsWith(WHERE)) sb.setLength(sb.length - WHERE.length)

    val csql = sb.toString()

    Option(connector.session.execute(csql).one()) match {
      case Some(row) => {
        val idCol = row.getString("id")
        val nameCol = row.getString("name")
        val descriptionCol = row.getString("description")
        val exampleCol = row.getString("example")

        Option(Word(idCol, nameCol, descriptionCol, exampleCol))
      }
      case _ => None
    }
  }

  override def createTable: Boolean = {
    val csql = s"CREATE TABLE IF NOT EXISTS ${connector.keyspace}.$tableName ($columnTypes)"
    connector.session.execute(csql).isExhausted
  }
}
