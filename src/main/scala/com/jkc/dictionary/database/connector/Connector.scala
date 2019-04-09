package com.jkc.dictionary.database.connector

import com.datastax.driver.core.{Cluster, Session}
import com.jkc.dictionary.database.table.WordTable
import com.jkc.dictionary.database.table.WordTable.{connector, tableName}
import com.typesafe.config.ConfigFactory

class Connector {

  var cluster: Cluster = _
  var session: Session = _
  var keyspace: String = _

  def getSession: Session = this.session

  def close(): Unit = {
    session.close
    cluster.close
  }

  def createKeyspace: Boolean = {

    val sb = new StringBuilder("CREATE KEYSPACE IF NOT EXISTS ")
      .append(keyspace).append(" WITH replication = {")
      .append("'class':'")
      .append("SimpleStrategy")
      .append("','replication_factor':")
      .append("1")
      .append("};")

    session.execute(sb.toString).isExhausted
  }

  def dropKeyspace: Boolean = {
    val csql = s"DROP KEYSPACE IF EXISTS ${connector.keyspace}"
    connector.session.execute(csql).isExhausted
  }

  def createTables: Boolean = {
    WordTable.createTable
  }
}

object Connector extends Connector {

  def apply() : Connector = {
    val connector = new Connector()

    val config = ConfigFactory.load()
    connector.keyspace = config.getString("cassandra.keyspace")

    val hosts = config.getStringList("cassandra.host").get(0)
    val port = config.getInt("cassandra.port")
    connector.cluster = Cluster.builder.addContactPoint(hosts).withPort(port).build()
    connector.session = connector.cluster.connect()

    connector
  }
}