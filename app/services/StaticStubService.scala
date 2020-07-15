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
import play.api.mvc.Result
import play.api.mvc.Results._

import scala.io.Source
//TODO rename this
class StaticStubService extends StubService {

  def errorNotFound: JsValue = getJsValue("/resources/404-no-data-nino.json")
  def errorNotFoundNino: JsValue = getJsValue("/resources/404-not-found-nino.json")
  def serverError: JsValue = getJsValue("/resources/500-server-error.json")
  def serviceUnavailable: JsValue = getJsValue("/resources/503-service-unavailable.json")
  def successMatchOneElement: JsValue = getJsValue("/resources/200-success-matched-one-element.json")
  def successMatchTwoElements: JsValue = getJsValue("/resources/200-success-matched-two-elements.json")
  def successMatchTwoTaxYears: JsValue = getJsValue("/resources/200-success-matched-two-tax-years.json")
  def successNoMatch: JsValue = getJsValue("/resources/200-success-no-match.json")
  def singleEmpSingleTaxYear: JsValue = getJsValue("/resources/AA777771A_rti.json")
  def multipleEmpSingleTaxYear: JsValue = getJsValue("/resources/AA777772A_rti.json")
  def multipleEmpMultipleTaxYears: JsValue = getJsValue("/resources/AA777773A_rti.json")
  def multipleEmpMultipleTaxYearsOp: JsValue = getJsValue("/resources/AA777774A_rti.json")
  def multipleEmpMultipleTaxYearsYdr: JsValue = getJsValue("/resources/AA777775A_rti.json")
  def validNinoWithNoData: JsValue = getJsValue("/resources/AA777776A_rti.json")

  override def getRetrieveCitizenIncome(nino: String): Result =
    nino match {
      case "AA111111A" => Ok(successMatchOneElement)
      case "AA222222A" => Ok(successMatchTwoElements)
      case "AA333333A" => Ok(successMatchTwoTaxYears)
      case "AA444444A" => Ok(successNoMatch)
      case "AA555555A" => NotFound(errorNotFound)
      case "AA666666A" => NotFound(errorNotFoundNino)
      case "AA777777A" => InternalServerError(serverError)
      case "AA888888A" => InternalServerError(serviceUnavailable)
      case "AA777771A" => Ok(singleEmpSingleTaxYear)
      case "AA777772A" => Ok(multipleEmpSingleTaxYear)
      case "AA777773A" => Ok(multipleEmpMultipleTaxYears)
      case "AA777774A" => Ok(multipleEmpMultipleTaxYearsOp)
      case "AA777775A" => Ok(multipleEmpMultipleTaxYearsYdr)
      case "AA777776A" => Ok(validNinoWithNoData)
      case _ => NotFound
    }

  private def getJsValue(jsFilePath: String): JsValue = {
    val resource = getClass.getResourceAsStream(jsFilePath)
    Json.parse(Source.fromInputStream(resource).getLines().mkString)
  }
}
