/*
 * Copyright 2023 HM Revenue & Customs
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

import org.scalatestplus.play.PlaySpec
import play.api.libs.json.JsValue
import play.api.mvc.Result
import play.api.test.Helpers._
import repositories.CitizenIncomeRepository._

import scala.concurrent.Future

class CitizenIncomeServiceSpec extends PlaySpec {

  val SUT = new CitizenIncomeService

  "getRetrieveCitizenIncome" must {
    Seq(
      OK -> Seq(
        "AA111111A" -> successMatchOneElement,
        "AA222222A" -> successMatchTwoElements,
        "AA333333A" -> successMatchTwoTaxYears,
        "AA444444A" -> successNoMatch,
        "AA777771A" -> singleEmpSingleTaxYear,
        "AA777772A" -> multipleEmpSingleTaxYear,
        "AA777773A" -> multipleEmpMultipleTaxYears,
        "AA777773 " -> multipleEmpMultipleTaxYears,
        "AA777774A" -> multipleEmpMultipleTaxYearsOp,
        "AA777775A" -> multipleEmpMultipleTaxYearsYdr,
        "AA777776A" -> validNinoWithNoData
      ),
      NOT_FOUND -> Seq(
        "AA555555A" -> errorNotFound,
        "AA666666A" -> errorNotFoundNino
      ),
      INTERNAL_SERVER_ERROR -> Seq(
        "AA777777A" -> serverError,
        "AA888888A" -> serviceUnavailable
      )
    ) foreach {
      case (expectedStatus: Int, testCases: Seq[(String, JsValue)]) =>
        s"return $expectedStatus" when {
          testCases.foreach {
            case (nino: String, body: JsValue) =>
              s"called with $nino" in {
                val result: Result = SUT.getRetrieveCitizenIncome(nino)
                result.header.status mustBe expectedStatus
                contentAsJson(Future.successful(result)) mustBe body
              }

          }
        }
    }
    "Return default not found" when {
      "The nino does not match" in {
        val result: Result = SUT.getRetrieveCitizenIncome("unmatched nino")
        result.header.status mustBe NOT_FOUND
        contentAsJson(Future.successful(result)) mustBe errorNotFoundNino
      }
    }
  }
}
