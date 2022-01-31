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

package services

import org.scalatestplus.play.PlaySpec
import play.api.libs.json._

class SchemaValidationSpec extends PlaySpec {

  val testSchemaValidaton: SchemaValidator = new SchemaValidator

  "isJsonValid" must {
    "return true when given valid json that is supported by the schema" in {
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

      testSchemaValidaton.isJsonValid(validRequestJson) mustBe true
    }

    "return false when given invalid Json that will not pass schema validation" in {
      val invalidRequestJson: JsValue = Json.parse(
        """{
          |"unknownField": "unknown"
          |}""".stripMargin
      )

      testSchemaValidaton.isJsonValid(invalidRequestJson) mustBe false
    }
  }
}
