package model

import org.joda.time.format.{DateTimeFormatter, ISODateTimeFormat}
import org.joda.time.{DateTime, DateTimeZone}
import play.api.libs.json._

class DateTimeFormat extends Format[DateTime] { // TODO is there a better existing option in Play?

  val isoDateTime : DateTimeFormatter = ISODateTimeFormat.dateTime().withZone(DateTimeZone.UTC)

  override def writes(o: DateTime): JsValue = JsString(isoDateTime.print(o))

  override def reads(json: JsValue): JsResult[DateTime] = {
    json match {
      case JsString(s) => JsSuccess(isoDateTime.parseDateTime(s))
      case _ => throw new RuntimeException()
    }
  }
}

object DateTimeFormat extends DateTimeFormat
