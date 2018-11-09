package services

import javax.inject.Inject
import model.{ChangeLogItem, TrelloAction, TrelloCard}

class ChangeLogCalculator @Inject()() {

  def calculateChangelogFrom(cardsWithActions: Seq[(TrelloCard, Seq[TrelloAction])]): Seq[ChangeLogItem] = {

    def moves(actions: Seq[TrelloAction]) = actions.filter(a => a.data.listBefore.nonEmpty && a.data.listAfter.nonEmpty)

    def currentlyMovedToDone(actions: Seq[TrelloAction]) = moves(actions).headOption.flatMap(_.data.listAfter.map(_.name)) == Some("Done")

    val cardsWithMoves = cardsWithActions.map(cwa => (cwa._1, moves(cwa._2)))

    val doneCards = cardsWithMoves.filter(cwa => currentlyMovedToDone(cwa._2))

    doneCards.map { cwa =>
      val releaseDate = cwa._2.headOption.map(a => a.date)
      releaseDate.map { rd =>
        ChangeLogItem(cwa._1.name, cwa._1.desc, rd)
      }
    }.flatten
  }

}
