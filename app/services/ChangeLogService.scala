package services

import javax.inject.Inject
import model.{ChangeLogItem, TrelloAction, TrelloCard}
import play.api.Configuration

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class ChangeLogService @Inject()(configuration: Configuration, trelloService: TrelloService, changeLogCalculator: ChangeLogCalculator) {

  private val list = configuration.get[String]("trello.done.list")
  private val label = configuration.get[String]("trello.public.label")

  def generateChangelog(): Future[Seq[ChangeLogItem]] = {

    val eventualCardsWithActions = trelloService.getCards(label).flatMap { cards =>
      Future.sequence(cards.map { card =>
        trelloService.getCardActions(card.id).map { actions =>
          (card, actions)
        }
      })
    }

    eventualCardsWithActions.map { cardsWithActions: Seq[(TrelloCard, Seq[TrelloAction])] =>
      changeLogCalculator.calculateChangelogFrom(cardsWithActions)
    }
  }

}