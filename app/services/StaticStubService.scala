/*
 * Copyright 2018 HM Revenue & Customs
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package services
import play.api.libs.json.{JsValue, Json}

import scala.concurrent.Future
import scala.io.Source

class StaticStubService extends StubService {

  val invalidNino = readJson("resources/json/400-invalid-nino.json")
  val invalidCorrelationId = readJson("resources/json/400-invalid-correlation-id.json")
  val invalidPayload = readJson("resources/json/400-invalid-payload.json")
  val invalidDateRange = readJson("resources/json/400-invalid-date-range.json")
  val invalidDatesEqual = readJson("resources/json/400-invalid-dates-equal.json")
  val errorNotFound = readJson("resources/json/404-no-data-nino.json") //?
  val errorNotFoundNino = readJson("resources/json/404-not-found-nino.json")
  val serverError = readJson("resources/json/500-server-error.json")
  val serviceUnavailable = readJson("resources/json/503-service-unavailable.json")
  val successMatchOneYear = readJson("resources/json/200-success-matched-one-year.json")
  val successMatchTwoYear = readJson("resources/json/200-success-matched-two-years.json")

  override def getRetrieveCitizenIncome(nino: String): Future[Option[JsValue]] = {
      nino match {
        case "QQ123456A" => Future.successful(Some(invalidNino))
        case "AC111111B" => Future.successful(Some(invalidCorrelationId))
        case "AC111111C" => Future.successful(Some(invalidPayload))
        case "AC222222C" => Future.successful(Some(invalidDateRange))
        case "AC333333C" => Future.successful(Some(invalidDatesEqual))
        case "AC333333D" => Future.successful(Some(serverError))
        case "AC333333E" => Future.successful(Some(serviceUnavailable))
        case "AA222222A" => Future.successful(Some(errorNotFound))
        case "AA111111A" => Future.successful(Some(errorNotFoundNino))
        case "AB123456C" => Future.successful(Some(successMatchOneYear))
        case "AA123456C" => Future.successful(Some(successMatchTwoYear))
        case _ => Future.successful(None)
      }
  }

  override def seedRetrieveCitizenIncome(retrieveCitizenIncome: Option[JsValue], status: Option[Int], description: String) = {
    Future.failed(throw new Exception("Cannot use seed endpoint in static stub mode, please refer to README"))
  }

  private def readJson(path: String) = {
    Json.parse(Source.fromFile(path).getLines().mkString)
  }
}
