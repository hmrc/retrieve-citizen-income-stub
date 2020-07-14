/*
 * Copyright 2020 HM Revenue & Customs
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

  def errorNotFound = getJsValue("/resources/404-no-data-nino.json")
  def errorNotFoundNino = getJsValue("/resources/404-not-found-nino.json")
  def serverError = getJsValue("/resources/500-server-error.json")
  def serviceUnavailable = getJsValue("/resources/503-service-unavailable.json")
  def successMatchOneElement = getJsValue("/resources/200-success-matched-one-element.json")
  def successMatchTwoElements = getJsValue("/resources/200-success-matched-two-elements.json")
  def successMatchTwoTaxYears = getJsValue("/resources/200-success-matched-two-tax-years.json")
  def successNoMatch = getJsValue("/resources/200-success-no-match.json")
  def singleEmpSingleTaxYear = getJsValue("/resources/AA777771A_rti.json")
  def multipleEmpSingleTaxYear = getJsValue("/resources/AA777772A_rti.json")
  def multipleEmpMultipleTaxYears = getJsValue("/resources/AA777773A_rti.json")
  def multipleEmpMultipleTaxYearsOp = getJsValue("/resources/AA777774A_rti.json")
  def multipleEmpMultipleTaxYearsYdr = getJsValue("/resources/AA777775A_rti.json")
  def validNinoWithNoData = getJsValue("/resources/AA777776A_rti.json")

  override def getRetrieveCitizenIncome(nino: String): Future[(Option[JsValue], Option[Int])] = {
    nino match {
      case "AA111111A" => Future.successful(Some(successMatchOneElement), Some(200))
      case "AA222222A" => Future.successful(Some(successMatchTwoElements), Some(200))
      case "AA333333A" => Future.successful(Some(successMatchTwoTaxYears), Some(200))
      case "AA444444A" => Future.successful(Some(successNoMatch), Some(200))
      case "AA555555A" => Future.successful(Some(errorNotFound), Some(404))
      case "AA666666A" => Future.successful(Some(errorNotFoundNino), Some(404))
      case "AA777777A" => Future.successful(Some(serverError), Some(500))
      case "AA888888A" => Future.successful(Some(serviceUnavailable), Some(500))
      case "AA777771A" => Future.successful(Some(singleEmpSingleTaxYear), Some(200))
      case "AA777772A" => Future.successful(Some(multipleEmpSingleTaxYear), Some(200))
      case "AA777773A" => Future.successful(Some(multipleEmpMultipleTaxYears), Some(200))
      case "AA777774A" => Future.successful(Some(multipleEmpMultipleTaxYearsOp), Some(200))
      case "AA777775A" => Future.successful(Some(multipleEmpMultipleTaxYearsYdr), Some(200))
      case "AA777776A" => Future.successful(Some(validNinoWithNoData), Some(200))

      case _ => Future.successful(None, Some(500))
    }
  }

  override def seedRetrieveCitizenIncome(retrieveCitizenIncome: Option[JsValue], status: Option[Int], description: String): Future[Nothing] = {
    Future.failed(throw new Exception("Cannot use seed endpoint in static stub mode, please refer to README"))
  }

  def getJsValue(jsFilePath:String): JsValue ={
    val resource = getClass.getResourceAsStream(jsFilePath)
    Json.parse(Source.fromInputStream(resource).getLines().mkString)
  }
}
