package controllers

import java.util.concurrent.TimeUnit

import com.google.common.cache.{Cache, CacheBuilder}
import javax.inject.Inject
import model.{ChangeLogItem, DateTimeFormat}
import play.api.libs.json.Json
import play.api.mvc.{AbstractController, Action, AnyContent, ControllerComponents}
import services.ChangeLogService

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class Application @Inject()(cc: ControllerComponents, changeLogService: ChangeLogService) extends AbstractController(cc) {

  private val cache: Cache[String, Seq[ChangeLogItem]] = CacheBuilder.newBuilder().
    expireAfterWrite(10, TimeUnit.MINUTES).
    build()

  private val CACHE_KEY = "CHANGELOG"

  def changeLog: Action[AnyContent] = Action.async {
    val cached = Option(cache.getIfPresent(CACHE_KEY))
    val changelog = cached.map { cachedChangelog =>
      Future.successful(cachedChangelog)

    }.getOrElse {
      changeLogService.generateChangelog().map { changelogItems =>
        cache.put(CACHE_KEY, changelogItems)
        changelogItems
      }
    }

    implicit val df = DateTimeFormat
    implicit val cliw = Json.writes[ChangeLogItem]
    changelog.map { cl =>
      Ok(Json.toJson(cl))
    }
  }

}
