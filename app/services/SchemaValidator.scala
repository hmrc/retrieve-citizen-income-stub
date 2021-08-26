/*
 * Copyright 2021 HM Revenue & Customs
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

import com.google.inject.Inject
import org.everit.json.schema.Schema
import org.everit.json.schema.loader.SchemaLoader
import org.json.{JSONObject, JSONTokener}
import play.api.Logger
import play.api.libs.json.{JsValue, Json}

import scala.io.Source
import scala.util.{Failure, Success, Try}

class SchemaValidator @Inject() () {

  private val logger: Logger = Logger(this.getClass)

  private val requestSchema: Schema = {
    val resource = getClass.getResourceAsStream("/schemas/des-request-schema-v1.json")
    val json     = new JSONObject(new JSONTokener(Source.fromInputStream(resource).mkString))
    SchemaLoader.load(json)
  }

  def isJsonValid(json: JsValue): Boolean =
    Try(requestSchema.validate(new JSONObject(Json.stringify(json)))) match {
      case Success(_) => true
      case Failure(error) =>
        logger.debug(s"Json request is not valid: $error")
        false
    }

}
