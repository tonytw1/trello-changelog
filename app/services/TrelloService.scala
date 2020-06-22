package services

import io.lemonlabs.uri._
import io.lemonlabs.uri.dsl._
import javax.inject.Inject
import model._
import play.api.{Configuration, Logging}
import play.api.libs.json.{JodaReads, Json}
import play.api.libs.ws.WSClient

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class TrelloService @Inject()(configuration: Configuration, ws: WSClient) extends JodaReads with Logging {

  private val TrelloApi = "https://api.trello.com/1"
  private val apiKey = configuration.get[String]("trello.api.key")
  private val apiToken = configuration.get[String]("trello.api.token")

  implicit val talr = Json.reads[TrelloActionList]
  implicit val tadr = Json.reads[TrelloActionData]
  implicit val tar = Json.reads[TrelloAction]
  implicit val tcr = Json.reads[TrelloCard]
  implicit val tsrr = Json.reads[TrelloSearchResult]

  def getCards(label: String): Future[Seq[TrelloCard]] = {
    val search = TrelloApi + "/search".
      addParam("query", "label:" + label).
      addParam("modelTypes", "cards").
      addParam("created", "180").
      addParam("cards_limit", "50")

    ws.url(withTrelloAuth(search)).get.map { r =>
      r.status match {
        case 200 =>
          Json.parse(r.body).as[TrelloSearchResult].cards
        case _ =>
          Seq()
      }
    }
  }

  def getCardActions(cardId: String): Future[Seq[TrelloAction]] = {
    val cardActions = TrelloApi + "/cards/" + cardId + "/actions"
    ws.url(withTrelloAuth(cardActions)).get.map { r =>
      r.status match {
        case 200 =>
          Json.parse(r.body).as[Seq[TrelloAction]]
        case _ =>
          logger.warn("Trello API call failed: " + r.status + " / " + r.body)
          Seq()
      }
    }
  }

  private def withTrelloAuth(uri: String): Uri = {
    uri.addParam("key", apiKey).addParam("token", apiToken)
  }

}
