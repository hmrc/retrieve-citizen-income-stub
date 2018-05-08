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
import play.api.libs.json._
import play.api.mvc.Controller
import services.RetrieveCitizenIncomeEnvelopeService
import uk.gov.hmrc.play.frontend.auth.Actions
import uk.gov.hmrc.play.frontend.auth.connectors.AuthConnector
import uk.gov.hmrc.play.frontend.controller.UnauthorisedAction

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.io.Source

class RetrieveCitizenIncomeController @Inject()(
  val authConnector: AuthConnector,
  val retrieveCitizenIncomeEnvelopeService: RetrieveCitizenIncomeEnvelopeService
) extends Controller with Actions {

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

  def getRetrieveCitizenIncome() = UnauthorisedAction.async { implicit request =>

    schemaValidationHandler(request.body.asJson) match {
      case Left(JsSuccess(_, _)) =>
        retrieveCitizenIncomeEnvelopeService.getActiveEnvelope map {
          case Some(RetrieveCitizenIncomeEnvelope(status, _, None, _)) =>
            Status(status)("")
          case Some(RetrieveCitizenIncomeEnvelope(status, _, Some(retrieveCitizenIncome), _)) =>
            Status(status)(Json.toJson(retrieveCitizenIncome))
          case None => NotFound
        }
      case Right(JsError(_)) => Future.successful(NotFound)
    }
  }

  def seedRetrieveCitizenIncome(status: Option[Int], description: String) = UnauthorisedAction.async { implicit request =>

    buildRetrieveCitizenIncomeEnvelope(request.body.asJson, status, description) match {

      case Left(retrieveCitizenIncomeEnvelope) =>
        retrieveCitizenIncomeEnvelopeService.putAndActivateEnvelope(retrieveCitizenIncomeEnvelope) map {
          case SuccessPutStubResponseResult =>
            Created
          case FailurePutStubResponseResult =>
            InternalServerError("Creation Failed\n")
        }
      case Right(errorMessage) =>
        Future.successful(BadRequest("Bad Request: " + errorMessage + "\n"))
    }
  }

  def buildRetrieveCitizenIncomeEnvelope(json: Option[JsValue], status: Option[Int], description: String): Either[RetrieveCitizenIncomeEnvelope, String] = {

    (json, status) match {
      case (Some(json), None) => {

        schemaValidationHandler(Some(json)) match {
          case Left(JsSuccess(rci, _)) =>
            Left(RetrieveCitizenIncomeEnvelope(OK, description, Some(rci), None))
          case Right(JsError(errors)) =>
            Right("supplied json did not validate against expected response schema: " + errors.mkString("\n"))
        }
      }
      case (None, Some(status)) =>
        Left( RetrieveCitizenIncomeEnvelope(status, description, None, None) )
      case _ =>
        Right("You must supply either valid retrieve citizen income json, or 'status' querystring parameters")
    }
  }
}
