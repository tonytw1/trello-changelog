package model

import org.joda.time.DateTime

case class ChangeLogItem(title: String, description: Option[String], date: DateTime)
