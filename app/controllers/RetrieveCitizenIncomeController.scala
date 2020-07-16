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

import com.eclipsesource.schema.{SchemaType, SchemaValidator}
import javax.inject.Inject
import play.api.libs.json._
import play.api.mvc.{Action, AnyContent, ControllerComponents}
import services.CitizenIncomeService
import uk.gov.hmrc.play.bootstrap.controller.BackendController

import scala.io.Source

class RetrieveCitizenIncomeController @Inject()(
                                                 stubService: CitizenIncomeService,
                                                 cc: ControllerComponents
                                               ) extends BackendController(cc) {


  private val requestSchema: JsValue = {
    val resource = getClass.getResourceAsStream("/schemas/des-request-schema-v1.json")
    Json.parse(Source.fromInputStream(resource).mkString)
  }

  private val validator: JsValue => JsResult[JsValue] = SchemaValidator().validate(Json.fromJson[SchemaType](requestSchema).get)(_)

  def schemaValidationHandler(jsonToValidate: Option[JsValue]): Either[JsError, JsSuccess[JsValue]] = {
    jsonToValidate match {
      case Some(json) => {
        if (validator(json).isSuccess)
          Right(JsSuccess(json))
        else
          Left(JsError("Does not validate against any schema"))
      }
      case None => Left(JsError("No json was supplied"))
    }
  }

  def getRetrieveCitizenIncome(nino: String): Action[AnyContent] = Action { implicit request =>
    schemaValidationHandler(request.body.asJson) match {
      case Right(JsSuccess(_, _)) =>
        stubService.getRetrieveCitizenIncome(nino)
      case Left(JsError(_)) => BadRequest.apply(
        Json.parse("""{"code":"INVALID_PAYLOAD","reason":"Submission has not passed validation. Invalid Payload."}""")
      )
    }
  }
}
