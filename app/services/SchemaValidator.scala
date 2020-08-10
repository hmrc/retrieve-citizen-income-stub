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

import com.eclipsesource.schema
import schema.SchemaType
import com.google.inject.Inject
import play.api.Logger
import play.api.libs.json.{JsError, JsSuccess, JsValue, Json}

import scala.io.Source

class SchemaValidator @Inject() () {

  private val logger: Logger = Logger(this.getClass)

  private val requestSchema: SchemaType = {
    val resource = getClass.getResourceAsStream("/schemas/des-request-schema-v1.json")
    val json     = Json.parse(Source.fromInputStream(resource).mkString)
    json.validate[SchemaType].getOrElse(throw new RuntimeException("Json Schema is not valid Json"))
  }

  private val validator: schema.SchemaValidator = schema.SchemaValidator()

  def isJsonValid(json: JsValue): Boolean =
    validator.validate(requestSchema)(json) match {
      case JsSuccess(_, _) => true
      case JsError(error) =>
        logger.debug(s"Json request is not valid: $error")
        false
    }

}
