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

import com.eclipsesource.schema.{SchemaType, SchemaValidator}
import javax.inject.Inject
import models.{FailurePutStubResponseResult, SuccessPutStubResponseResult}
import play.api.libs.json.{JsError, JsSuccess, JsValue, Json}
import services.StubService
import uk.gov.hmrc.play.bootstrap.controller.{BaseController, UnauthorisedAction}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.io.Source

class RetrieveCitizenIncomeController @Inject()(
  stubService: StubService
) extends BaseController {

  def schemaValidationHandler(jsonToValidate: Option[JsValue]): Either[JsSuccess[JsValue], JsError] = {

    val validator = new SchemaValidator()

    val requestSchema: JsValue = {
      val resource = getClass.getResourceAsStream("/schemas/des-error-response-schema-v1.json")
      Json.parse(Source.fromInputStream(resource).mkString)
    }

    val errorSeedSchema: JsValue = {
      val resource = getClass.getResourceAsStream("/schemas/des-request-schema-v1.json")
      Json.parse(Source.fromInputStream(resource).mkString)
    }

    val successSeedSchema: JsValue = {
      val resource = getClass.getResourceAsStream("/schemas/des-response-schema-v1.json")
      Json.parse(Source.fromInputStream(resource).mkString)
    }

    val schemas: List[JsValue] = List(requestSchema, errorSeedSchema, successSeedSchema)

    jsonToValidate match {
      case Some(json) => {
        if(schemas.exists(schema => validator.validate(Json.fromJson[SchemaType](schema).get)(json).isSuccess))
          Left(JsSuccess(json))
        else
          Right(JsError("Does not validate against any schema"))
      }
      case None => Right(JsError("No json was supplied"))
    }
  }

  def getRetrieveCitizenIncome(nino: String) = UnauthorisedAction.async { implicit request =>

    schemaValidationHandler(request.body.asJson) match {
      case Left(JsSuccess(_, _)) =>
        stubService.getRetrieveCitizenIncome(nino).map {
          case (None, Some(404)) =>
            NotFound
          case (Some(json), Some(200)) =>
            Ok(json)
          case (Some(json), Some(404)) =>
            NotFound(json)
          case (Some(json), Some(500)) =>
            InternalServerError(json)
          case _ =>
            NotFound

        }
      case Right(JsError(_)) => Future.successful(BadRequest(Json.parse("{\"code\":\"INVALID_PAYLOAD\",\"reason\":\"Submission has not passed validation. Invalid Payload.\"}")))
    }
  }

  def seedRetrieveCitizenIncome(status: Option[Int], description: String) = UnauthorisedAction.async { implicit request =>

    schemaValidationHandler(request.body.asJson) match {
      case Left(JsSuccess(_, _)) =>
        stubService.seedRetrieveCitizenIncome(request.body.asJson, status, description).map {
          case SuccessPutStubResponseResult =>
            Created
          case FailurePutStubResponseResult =>
            InternalServerError("Creation failed\n")
        }
      case Right(JsError(errors)) =>
        Future.successful(BadRequest("supplied json did not validate against expected response schema: " + errors.mkString("\n")))
    }
  }
}
