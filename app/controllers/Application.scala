package controllers

import javax.inject.Inject
import model.{ChangeLogItem, DateTimeFormat}
import play.api.libs.json.Json
import play.api.mvc.{AbstractController, ControllerComponents}
import services.ChangeLogService

import scala.concurrent.ExecutionContext.Implicits.global

class Application @Inject()(cc: ControllerComponents, changeLogService: ChangeLogService) extends AbstractController(cc){

  def changeLog = Action.async {
    implicit val df = DateTimeFormat
    implicit val cliw = Json.writes[ChangeLogItem]

    changeLogService.generateChangelog().map { changelogItems =>
      Ok(Json.toJson(changelogItems))
    }
  }

}
