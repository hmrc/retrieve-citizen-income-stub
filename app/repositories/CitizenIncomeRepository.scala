/*
 * Copyright 2022 HM Revenue & Customs
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

package repositories

import play.api.libs.json.{JsValue, Json}
import scala.io.Source

object CitizenIncomeRepository {

  val errorNotFound: JsValue                  = getJsValue("/404-no-data-nino.json")
  val errorNotFoundNino: JsValue              = getJsValue("/404-not-found-nino.json")
  val serverError: JsValue                    = getJsValue("/500-server-error.json")
  val serviceUnavailable: JsValue             = getJsValue("/503-service-unavailable.json")
  val successMatchOneElement: JsValue         = getJsValue("/200-success-matched-one-element.json")
  val successMatchTwoElements: JsValue        = getJsValue("/200-success-matched-two-elements.json")
  val successMatchTwoTaxYears: JsValue        = getJsValue("/200-success-matched-two-tax-years.json")
  val successNoMatch: JsValue                 = getJsValue("/200-success-no-match.json")
  val singleEmpSingleTaxYear: JsValue         = getJsValue("/AA777771A_rti.json")
  val multipleEmpSingleTaxYear: JsValue       = getJsValue("/AA777772A_rti.json")
  val multipleEmpMultipleTaxYears: JsValue    = getJsValue("/AA777773A_rti.json")
  val multipleEmpMultipleTaxYearsOp: JsValue  = getJsValue("/AA777774A_rti.json")
  val multipleEmpMultipleTaxYearsYdr: JsValue = getJsValue("/AA777775A_rti.json")
  val validNinoWithNoData: JsValue            = getJsValue("/AA777776A_rti.json")
  val successLimitedFields: JsValue           = getJsValue("/200-success-limited-fields.json")

  private def getJsValue(jsFilePath: String): JsValue = {
    val resource = getClass.getResourceAsStream("/resources" + jsFilePath)
    Json.parse(Source.fromInputStream(resource).getLines().mkString)
  }

}
