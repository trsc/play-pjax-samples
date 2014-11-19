package models

import java.sql.Connection

import anorm.SqlParser._
import anorm._

import scala.language.postfixOps

case class Anything(id: Long, value: String)

object Anythings {

  val rowParser = long("id") ~ str("value") map flatten map Anything.tupled

  def findAll(implicit c: Connection): List[Anything] = {
    SQL"""SELECT * FROM anythings""" as (rowParser *)
  }

  def create(value: String)(implicit c: Connection): Option[Long] = {
    SQL"""INSERT INTO anythings (value) VALUES ($value)""" executeInsert()
  }

}
