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

  val errorNotFound = getJsValue("/responses/404-no-data-nino.json")
  val errorNotFoundNino = getJsValue("/responses/404-not-found-nino.json")
  val serverError = getJsValue("/responses/500-server-error.json")
  val serviceUnavailable = getJsValue("/responses/503-service-unavailable.json")
  val successMatchOneElement = getJsValue("/responses/200-success-matched-one-element.json")
  val successMatchTwoElements = getJsValue("/responses/200-success-matched-two-elements.json")
  val successMatchTwoTaxYears = getJsValue("/responses/200-success-matched-two-tax-years.json")
  val successNoMatch = getJsValue("/responses/200-success-no-match.json")

  override def getRetrieveCitizenIncome(nino: String): Future[Option[JsValue]] = {
    nino match {
      case "AA111111A" => Future.successful(Some(successMatchOneElement))
      case "AA222222A" => Future.successful(Some(successMatchTwoElements))
      case "AA333333A" => Future.successful(Some(successMatchTwoTaxYears))
      case "AA444444A" => Future.successful(Some(successNoMatch))
      case "AA555555A" => Future.successful(Some(errorNotFound))
      case "AA666666A" => Future.successful(Some(errorNotFoundNino))
      case "AA777777A" => Future.successful(Some(serverError))
      case "AA888888A" => Future.successful(Some(serviceUnavailable))
      case _ => Future.successful(None)
    }
  }

  override def seedRetrieveCitizenIncome(retrieveCitizenIncome: Option[JsValue], status: Option[Int], description: String): Future[Nothing] = {
    Future.failed(throw new Exception("Cannot use seed endpoint in static stub mode, please refer to README"))
  }

  def getJsValue(jsFilePath:String): JsValue ={
    val jsonFilePath = getClass.getResource(jsFilePath)
    jsonFilePath match {
      case null => throw new Exception("Broken filepath")
      case _ => Json.parse(Source.fromURL(jsonFilePath).getLines.mkString("\n"))
    }
  }
}
