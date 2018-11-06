package controllers

import java.util.concurrent.TimeUnit

import com.google.common.cache.{Cache, CacheBuilder}
import javax.inject.Inject
import model.{ChangeLogItem, DateTimeFormat}
import play.api.Logger
import play.api.libs.json.Json
import play.api.mvc.{AbstractController, ControllerComponents}
import services.ChangeLogService

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class Application @Inject()(cc: ControllerComponents, changeLogService: ChangeLogService) extends AbstractController(cc) {

  private val cache: Cache[String, Seq[ChangeLogItem]] = CacheBuilder.newBuilder().
    expireAfterWrite(10, TimeUnit.MINUTES).
    build()

  private val CACHE_KEY = "CHANGELOG"

  def changeLog = Action.async {
    val cached = cache.getIfPresent(CACHE_KEY)
    val changelog = if (cached != null) {
      Logger.info("Changelog returned from cache")
      Future.successful(cached)

    } else {
      changeLogService.generateChangelog().map { changelogItems =>
        Logger.info("Caching changelog")
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
