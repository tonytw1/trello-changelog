package services

import javax.inject.Inject
import model.TrelloCard
import model.ChangeLogItem
import play.api.Configuration

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class ChangeLogService @Inject()(configuration: Configuration, trelloService: TrelloService) {

  private val list = configuration.get[String]("trello.done.list")
  private val label = configuration.get[String]("trello.public.label")

  def generateChangelog(): Future[Seq[ChangeLogItem]] = {

    def releaseDate(card: TrelloCard) = trelloService.getCardActions(card.id).map { actions => // TODO doesn't deal with cards moved back from Done
      val movedToDone = actions.find(a => a.data.listAfter.fold(false)(la => la.name == list))
      movedToDone.map(a => a.date)
    }

    def generateChangelogFromCards(cards: Seq[TrelloCard]): Future[Seq[ChangeLogItem]] = {
      def changeLogItemsForDoneCards = Future.sequence(cards.map { c =>
        releaseDate(c).map(_.map(rd => ChangeLogItem(c.name, c.desc, rd)))
      }).map { cos =>
        cos.flatten
      }

      changeLogItemsForDoneCards.map(_.sortBy(_.date.toDate).reverse)
    }

    trelloService.getCards(label).flatMap(generateChangelogFromCards)
  }

}