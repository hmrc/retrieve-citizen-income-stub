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

package controllers

import org.scalatest.{BeforeAndAfterEach, Matchers, WordSpec}
import play.api.inject.guice.GuiceApplicationBuilder
import play.api.libs.json.Json
import play.api.test.FakeRequest
import play.api.test.Helpers.{status, _}
import services.StaticStubService
import uk.gov.hmrc.play.test.WithFakeApplication

class StaticRetrieveCitizenIncomeControllerSpec  extends WordSpec with WithFakeApplication with Matchers with BeforeAndAfterEach {

  override lazy val fakeApplication = new GuiceApplicationBuilder()
    .build()

  val exampleRequest = Json.parse(
    """{
      |  "fromDate": "2016-12-31",
      |  "toDate": "2017-12-31",
      |  "surname": "Smith",
      |  "firstName": "John",
      |  "gender": "M",
      |  "initials": "J B",
      |  "dateOfBirth": "2000-03-29"
      |}""".stripMargin)

  "StaticRetrieveCitizenIncomeControllerSpec" should {
    "return 404 response" when {
      "stub has no data for given nino" in {
        val Some(r) = route(fakeApplication, FakeRequest(POST, "/individuals/AA555555A/income")
          .withJsonBody(exampleRequest))

        status(r) shouldBe NOT_FOUND
      }

      "stub doesn't have the given nino" in {
        val Some(r) = route(fakeApplication, FakeRequest(POST, "/individuals/AA999999A/income")
          .withJsonBody(exampleRequest))

        status(r) shouldBe NOT_FOUND
      }

    }

    "return 200 response" when {
      "there is a match with one element" in {
        val Some(r) = route(fakeApplication, FakeRequest(POST, "/individuals/AA111111A/income")
          .withJsonBody(exampleRequest))

        status(r) shouldBe OK
      }

      "there is a match with two elements" in {
        val Some(r) = route(fakeApplication, FakeRequest(POST, "/individuals/AA222222A/income")
          .withJsonBody(exampleRequest))

        status(r) shouldBe OK
      }

      "there is a match with two tax years of monthly data" in {
        val Some(r) = route(fakeApplication, FakeRequest(POST, "/individuals/AA333333A/income")
          .withJsonBody(exampleRequest))

        status(r) shouldBe OK
      }

      "the nino is valid but there is no match in citizen details" in {
        val Some(r) = route(fakeApplication, FakeRequest(POST, "/individuals/AA444444A/income")
          .withJsonBody(exampleRequest))

        status(r) shouldBe OK
      }
    }

 "serve a 500 server error" when {
      "des is currently experiencing technical difficulties" in {
        val Some(r) = route(fakeApplication, FakeRequest(POST, "/individuals/AA777777A/income")
          .withJsonBody(exampleRequest))

        status(r) shouldBe INTERNAL_SERVER_ERROR
      }
    }

    "serve a 503 service unavailable" when {
      "dependant systems are currently not responding." in {
        val Some(r) = route(fakeApplication, FakeRequest(POST, "/individuals/AA888888A/income")
          .withJsonBody(exampleRequest))

        status(r) shouldBe INTERNAL_SERVER_ERROR
      }
    }
  }
  val SUT = new StaticStubService {}
}
