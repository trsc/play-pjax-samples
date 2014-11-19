package models

import java.sql.Connection
import anorm.SqlParser._
import anorm._

import scala.language.postfixOps

case class Something(id: Long, value: String)

object Somethings {

  val rowParser = long("id") ~ str("value") map flatten map Something.tupled

  def findAll(implicit c: Connection): List[Something] = {
    SQL"""SELECT * FROM somethings""" as (rowParser *)
  }

  def create(value: String)(implicit c: Connection): Option[Long] = {
    SQL"""INSERT INTO somethings (value) VALUES ($value)""" executeInsert()
  }

}
