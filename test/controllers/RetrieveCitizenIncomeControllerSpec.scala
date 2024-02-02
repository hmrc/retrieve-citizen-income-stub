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

package controllers

import org.apache.pekko.stream.Materializer
import org.apache.pekko.stream.testkit.NoMaterializer
import org.mockito.ArgumentMatchers
import org.mockito.Mockito.{reset, when}
import org.scalacheck.{Arbitrary, Gen}
import org.scalatest.BeforeAndAfterEach
import org.scalatest.matchers.must.Matchers._
import org.scalatestplus.scalacheck.ScalaCheckPropertyChecks
import play.api.Application
import play.api.inject.bind
import play.api.inject.guice.GuiceApplicationBuilder
import play.api.libs.json.{JsValue, Json}
import play.api.mvc.Result
import play.api.mvc.Results._
import play.api.test.Helpers._
import play.api.test.{FakeHeaders, FakeRequest, Injecting}
import services.{CitizenIncomeService, SchemaValidator}
import test_utils.UnitSpec

import scala.concurrent.Future

class RetrieveCitizenIncomeControllerSpec
    extends UnitSpec
    with BeforeAndAfterEach
    with Injecting
    with ScalaCheckPropertyChecks {

  implicit val noMaterializer: Materializer = NoMaterializer

  val nino: String = "NINO"

  val validRequestJson: JsValue = Json.parse(
    """{
      |  "fromDate": "2016-12-31",
      |  "toDate": "2017-12-31",
      |  "surname": "Smith",
      |  "firstName": "John",
      |  "gender": "M",
      |  "initials": "J B",
      |  "dateOfBirth": "2000-03-29"
      |}""".stripMargin
  )

  def fakeRequest(json: JsValue = validRequestJson): FakeRequest[JsValue] =
    FakeRequest("POST", "/", FakeHeaders(Seq(("Content-type", "application/json"))), json)

  val mockStubService: CitizenIncomeService = mock[CitizenIncomeService]
  val mockSchemaValidation: SchemaValidator = mock[SchemaValidator]
  val SUT: RetrieveCitizenIncomeController  = inject[RetrieveCitizenIncomeController]

  override def beforeEach(): Unit = {
    super.beforeEach()
    reset(mockStubService)
    reset(mockSchemaValidation)
  }

  override def fakeApplication(): Application =
    GuiceApplicationBuilder()
      .overrides(
        bind[CitizenIncomeService].toInstance(mockStubService),
        bind[SchemaValidator].toInstance(mockSchemaValidation)
      )
      .build()

  "getRetrieveCitizenIncome" must {
    "return what the service provides" when {
      "request contains some valid json" in {
        val resultsGenerator: Gen[Result] = for {
          statusCode: Int   <- Gen.choose(min = 200, max = 599)
          genString: String <- Arbitrary.arbitrary[String]
          body: String = "Generated-string: " + genString
        } yield Status(statusCode)(body)

        forAll(resultsGenerator) { generatedResult: Result =>
          when(mockStubService.getRetrieveCitizenIncome(nino)).thenReturn(generatedResult)
          when(mockSchemaValidation.isJsonValid(ArgumentMatchers.any())).thenReturn(true)
          val result = SUT.getRetrieveCitizenIncome(nino)(fakeRequest())
          status(result) mustBe generatedResult.header.status
          contentAsString(result) mustBe contentAsString(Future.successful(generatedResult))
        }
      }
    }

    "Return BadRequest" when {
      "the Json does not match the schema" in {

        val expectedJson: JsValue = Json.parse(
          """ |{
            |"code":"INVALID_PAYLOAD",
            |"reason":"Submission has not passed validation. Invalid Payload."
            |}""".stripMargin
        )

        val invalidRequestJson: JsValue = Json.parse(
          """{
            |  "fromDate": "2016-12-31",
            |  "toDate": "2017-12-31",
            |  "surname": "Smith",
            |  "firstName": "John",
            |  "gender": "M"
            |}""".stripMargin
        )

        val invalidJsonFakeRequest = fakeRequest(invalidRequestJson)
        when(mockSchemaValidation.isJsonValid(ArgumentMatchers.any())).thenReturn(false)

        val response = SUT.getRetrieveCitizenIncome(nino)(invalidJsonFakeRequest)

        status(response) mustBe BAD_REQUEST
        contentAsJson(response) mustBe expectedJson
      }

    }
  }
}
