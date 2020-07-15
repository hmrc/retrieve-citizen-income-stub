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

package controllers

import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito.{reset, when}
import org.scalacheck.{Arbitrary, Gen}
import org.scalatest.BeforeAndAfterEach
import org.scalatestplus.mockito.MockitoSugar
import org.scalatestplus.play.PlaySpec
import org.scalatestplus.play.guice.GuiceOneAppPerSuite
import org.scalatestplus.scalacheck.ScalaCheckPropertyChecks
import play.api.Application
import play.api.inject.bind
import play.api.inject.guice.GuiceApplicationBuilder
import play.api.libs.json.{JsValue, Json}
import play.api.mvc.Result
import play.api.mvc.Results._
import play.api.test.Helpers._
import play.api.test.{FakeRequest, Injecting}
import services.StaticStubService

import scala.concurrent.Future

class RetrieveCitizenIncomeControllerSpec extends PlaySpec with GuiceOneAppPerSuite with BeforeAndAfterEach with MockitoSugar with Injecting with ScalaCheckPropertyChecks {


  override def beforeEach() = {
    super.beforeEach()
    reset(mockStubService)
  }

  val resultsGenerator: Gen[Result] = for {
    statusCode: Int <- Gen.choose(200, 599)
    body: String <- Arbitrary.arbitrary[String]
  } yield {
    Status(statusCode)("Generated-string: " + body)
  }

  "thing response is" in {
    val fakeRequest = FakeRequest(POST, "")
      .withJsonBody(exampleRequest)

    forAll(resultsGenerator) {
      generatedResult: Result =>
        val nino: String = "AA111111A"
        when(mockStubService.getRetrieveCitizenIncome(nino)).thenReturn(generatedResult)
        val result = SUT.getRetrieveCitizenIncome(nino)(fakeRequest)
        status(result) mustBe generatedResult.header.status
        contentAsString(result) mustBe contentAsString(Future.successful(generatedResult))
    }
  }


  val exampleRequest: JsValue = Json.parse(
    """{
      |  "fromDate": "2016-12-31",
      |  "toDate": "2017-12-31",
      |  "surname": "Smith",
      |  "firstName": "John",
      |  "gender": "M",
      |  "initials": "J B",
      |  "dateOfBirth": "2000-03-29"
      |}""".stripMargin)

  override def fakeApplication: Application =
    GuiceApplicationBuilder().overrides(
      bind[StaticStubService].toInstance(mockStubService)
    ).build()

  val mockStubService = mock[StaticStubService]
  val SUT: RetrieveCitizenIncomeController = inject[RetrieveCitizenIncomeController]

  "getRetrieveCitizenIncome" must {
    "return 200 response" when {
      "there is a match with one element" in {
        val fakeRequest = FakeRequest(POST, "")
          .withJsonBody(exampleRequest)
        when(mockStubService.getRetrieveCitizenIncome(any())).thenReturn(Ok("Hello"))

        val response = SUT.getRetrieveCitizenIncome("AA111111A")(fakeRequest)

        status(response) mustBe OK
        contentAsString(response) mustBe "Hello"
      }

      "Return BadRequest" when {
        "Invalid Json" in {
          val fakeRequest = FakeRequest(POST, "")
            .withJsonBody(Json.parse("""{}"""))

          val response = SUT.getRetrieveCitizenIncome("AA111111A")(fakeRequest)

          status(response) mustBe BAD_REQUEST
          contentAsJson(response) mustBe Json.parse("""{"code":"INVALID_PAYLOAD","reason":"Submission has not passed validation. Invalid Payload."}""")
        }

        "No Json is provided" in {
          val fakeRequest = FakeRequest(POST, "")

          val response = SUT.getRetrieveCitizenIncome("AA111111A")(fakeRequest)

          status(response) mustBe BAD_REQUEST
          contentAsJson(response) mustBe Json.parse("""{"code":"INVALID_PAYLOAD","reason":"Submission has not passed validation. Invalid Payload."}""")
        }
      }
    }
  }
}
