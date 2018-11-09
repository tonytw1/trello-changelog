package services

import model.{TrelloAction, TrelloActionData, TrelloActionList, TrelloCard}
import org.joda.time.DateTime

class ChangelogCalculatorSpec extends org.specs2.mutable.Specification {

  "should ignore cards with no actions" >> {
    val card = TrelloCard(id = "not-done", name = "Not done", dateLastActivity = DateTime.now, desc = None)
    val cardsWithActions = Seq((card, Seq.empty))

    val changelogItems = new ChangeLogCalculator().calculateChangelogFrom(cardsWithActions)

    changelogItems.isEmpty must beTrue
  }

  "should include done items" >> {
    val card = TrelloCard(id = "done", name = "done", dateLastActivity = DateTime.now, desc = None)

    val inProgress = TrelloActionList(id = "in-progess", name = "In progress")
    val done = TrelloActionList(id = "done", name = "Done")
    val actions = Seq(TrelloAction(id = "123", date = DateTime.now, `type` = "update", data = TrelloActionData(listBefore = Some(inProgress), listAfter = Some(done))))
    val cardsWithActions = Seq((card, actions))

    val changelogItems = new ChangeLogCalculator().calculateChangelogFrom(cardsWithActions)

    changelogItems.nonEmpty must beTrue
  }

  "should ignore moved back items" >> {
    val card = TrelloCard(id = "done-then-pulled-back", name = "done-then-pulled-back", dateLastActivity = DateTime.now, desc = None)

    val inProgress = TrelloActionList(id = "in-progess", name = "In progress")
    val done = TrelloActionList(id = "done", name = "Done")
    val actions = Seq(
      TrelloAction(id = "123", date = DateTime.now, `type` = "update", data = TrelloActionData(listBefore = Some(done), listAfter = Some(inProgress))),
      TrelloAction(id = "123", date = DateTime.now, `type` = "update", data = TrelloActionData(listBefore = Some(inProgress), listAfter = Some(done)))
    )
    val cardsWithActions = Seq((card, actions))

    val changelogItems = new ChangeLogCalculator().calculateChangelogFrom(cardsWithActions)

    changelogItems.isEmpty must beTrue
  }

}
