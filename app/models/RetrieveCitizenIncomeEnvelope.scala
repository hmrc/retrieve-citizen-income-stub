package models

import org.joda.time.DateTime
import play.api.libs.json._
import play.api.libs.json.Json
import uk.gov.hmrc.mongo.json.ReactiveMongoFormats.mongoEntity

case class RetrieveCitizenIncomeEnvelope(
  status: Int,
  id: String,
  retrieveCitizenIncome: Option[JsValue],
  activatedAt: Option[DateTime]
) extends StubDataEnvelope

object RetrieveCitizenIncomeEnvelope{

  implicit val formats = mongoEntity {

    Json.format[RetrieveCitizenIncomeEnvelope]
  }
}
