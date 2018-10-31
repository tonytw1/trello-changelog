package model

import org.joda.time.DateTime

case class TrelloCard(id: String, name: String, dateLastActivity: DateTime, desc: Option[String])
case class TrelloSearchResult(cards: Seq[TrelloCard])

case class TrelloActionList(id: String, name: String)
case class TrelloActionData(listAfter: Option[TrelloActionList], listBefore: Option[TrelloActionList])
case class TrelloAction(id: String, date: DateTime, `type`: String, data: TrelloActionData)