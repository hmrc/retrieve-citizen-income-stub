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
import org.scalatestplus.play.OneAppPerSuite
import play.api.inject.guice.GuiceApplicationBuilder
import play.api.libs.json.Json
import play.api.test.FakeRequest
import play.api.test.Helpers.{POST, contentAsString, route, status}
import play.modules.reactivemongo.ReactiveMongoComponent
import play.api.test.Helpers._

import scala.concurrent.ExecutionContext.Implicits.global

class RetrieveCitizenIncomeControllerSpec extends WordSpec with OneAppPerSuite with Matchers with BeforeAndAfterEach  {

  override lazy val app = GuiceApplicationBuilder().configure(
    "mongodb.uri" -> "mongodb://localhost:27017/retrieve-citizen-income-test"
  ).build()


  override def beforeEach(): Unit = {
    app.injector.instanceOf[ReactiveMongoComponent].mongoConnector.db().drop()
    super.beforeEach()
  }

  "Calling POST /retrievecitizenincome" should {

    "return 201 when successful" in {

      val retrieveCitizenIncomeJson = Json.parse(
        """{
          |  "matchPattern": 63,
          |  "taxYears": [
          |    {
          |      "taxYear": "16-17",
          |      "taxYearIndicator": "P",
          |      "hmrcOfficeNumber": "099",
          |      "employerPayeRef": "A1B2c3d4e5",
          |      "employerName1": "Bugs Bunny",
          |      "nationalInsuranceNumber": "AB123456C",
          |      "surname": "Disney",
          |      "gender": "M",
          |      "uniqueEmploymentSequenceNumber": 9999,
          |      "taxablePayInPeriod": 999999.99,
          |      "taxDeductedOrRefunded": -12345.67,
          |      "grossEarningsForNICs": 888888.66,
          |      "taxablePayToDate": 999999.99,
          |      "totalTaxToDate": 654321.08,
          |      "numberOfNormalHoursWorked": "E",
          |      "payFrequency": "M1",
          |      "paymentDate": "2017-02-03",
          |      "earningsPeriodsCovered": 11,
          |      "uniquePaymentId": 666666,
          |      "paymentConfidenceStatus": "1",
          |      "taxCode": "11100L",
          |      "trivialCommutationPaymentTypeA": 99998,
          |      "hmrcReceiptTimestamp": "2018-04-16T09:23:55Z",
          |      "rtiReceivedDate": "2018-04-16",
          |      "apiAvailableTimestamp": "2018-04-16T09:23:55Z"
          |    },
          |    {
          |      "taxYear": "15-16",
          |      "taxYearIndicator": "P",
          |      "hmrcOfficeNumber": "099",
          |      "employerPayeRef": "A1B2c3d4e5",
          |      "employerName1": "Donald Duck",
          |      "nationalInsuranceNumber": "AB123456C",
          |      "surname": "Disney",
          |      "gender": "M",
          |      "uniqueEmploymentSequenceNumber": 6666,
          |      "taxablePayInPeriod": 666666.66,
          |      "taxDeductedOrRefunded": 12345.67,
          |      "grossEarningsForNICs": 777777.66,
          |      "taxablePayToDate": 999999.99,
          |      "totalTaxToDate": 43210,
          |      "numberOfNormalHoursWorked": "E",
          |      "payFrequency": "M3",
          |      "paymentDate": "2017-02-03",
          |      "earningsPeriodsCovered": 12,
          |      "uniquePaymentId": 654321,
          |      "paymentConfidenceStatus": "2",
          |      "taxCode": "K15432",
          |      "trivialCommutationPaymentTypeB": -99998,
          |      "hmrcReceiptTimestamp": "2018-04-16T10:34:55Z",
          |      "rtiReceivedDate": "2018-04-16",
          |      "apiAvailableTimestamp": "2018-04-16T09:23:55Z"
          |    }
          |  ]
          |}""".stripMargin)

      val Some(r) = route(app, FakeRequest(POST, "/seed/individuals/income?description=Description")
        .withJsonBody(retrieveCitizenIncomeJson)
      )
      status(r) shouldBe CREATED
      contentAsString(r) shouldBe ""
    }
  }



}
