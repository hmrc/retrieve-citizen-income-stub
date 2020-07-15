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

import org.scalatestplus.play.PlaySpec
import play.api.libs.json.JsValue
import play.api.mvc.Result
import play.api.test.Helpers._

import scala.concurrent.Future

class StubServiceSpec extends PlaySpec {

  val SUT = new  StaticStubService

  "getRetrieveCitizenIncome" must {
    Map(
      200 -> List(
          "AA111111A" -> SUT.successMatchOneElement,
          "AA222222A" -> SUT.successMatchTwoElements,
          "AA333333A" -> SUT.successMatchTwoTaxYears,
          "AA444444A" -> SUT.successNoMatch,
          "AA777771A" -> SUT.singleEmpSingleTaxYear,
          "AA777772A" -> SUT.multipleEmpSingleTaxYear,
          "AA777773A" -> SUT.multipleEmpMultipleTaxYears,
          "AA777774A" -> SUT.multipleEmpMultipleTaxYearsOp,
          "AA777775A" -> SUT.multipleEmpMultipleTaxYearsYdr,
          "AA777776A" -> SUT.validNinoWithNoData,
      ),
      404 -> List(
        "AA555555A" -> SUT.errorNotFound,
        "AA666666A" -> SUT.errorNotFoundNino,
        "an unmatched Nino" -> null
      ),
      500 -> List(
        "AA777777A" -> SUT.serverError,
        "AA888888A" -> SUT.serviceUnavailable
      )
    ) foreach {
      case (expectedStatus: Int, l: List[(String, JsValue)]) =>
        s"return $expectedStatus" when {
          l.foreach {
            case (nino: String, body: JsValue) =>
              s"called with $nino" in {
                val result: Result = SUT.getRetrieveCitizenIncome(nino)
                result.header.status mustBe expectedStatus
                contentAsJson(Future.successful(result)) mustBe body
              }
            case (nino: String, _) =>
              s"called with $nino" in {
                val result: Result = SUT.getRetrieveCitizenIncome(nino)
                result.header.status mustBe expectedStatus
              }
          }
        }
    }
  }

}