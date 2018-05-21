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

import java.io.File
import javax.inject.Inject

import com.eclipsesource.schema.{SchemaType, SchemaValidator}
import models.{FailurePutStubResponseResult, RetrieveCitizenIncomeEnvelope, SuccessPutStubResponseResult}
import play.api.libs.json.{JsError, JsSuccess, JsValue, Json}
import services.{RetrieveCitizenIncomeEnvelopeService, StubService}
import uk.gov.hmrc.play.bootstrap.controller.{BaseController, UnauthorisedAction}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.io.Source

class RetrieveCitizenIncomeController @Inject()(
  stubService: StubService
) extends BaseController {

  def schemaValidationHandler(jsonToValidate: Option[JsValue]): Either[JsSuccess[JsValue], JsError] = {

    val validator = new SchemaValidator()

    val schemas: List[JsValue] = {
      val dir = new File("resources")
      dir.listFiles.filter(x => x.isFile && x.getName.endsWith(".json")).toList.map { file =>
        Json.parse(Source.fromFile(file).getLines().mkString)
      }
    }

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
          case None =>
            NotFound
          case Some(json) =>
            Ok(json)
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
