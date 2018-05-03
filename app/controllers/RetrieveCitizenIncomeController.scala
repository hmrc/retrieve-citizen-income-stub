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

  def getJsonValue(file: String): JsValue = {

    Json.parse(Source.fromFile(file).getLines.mkString)
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

    val validator = new SchemaValidator()
    val schema = Json.fromJson[SchemaType](getJsonValue("resources/des-response-schema-v1.json")).get

    (json, status) match {
      case (Some(json), None) => {

        validator.validate(schema)(json) match {
          case JsSuccess(rci, _) =>
            Left(RetrieveCitizenIncomeEnvelope(OK, description, Some(rci), None))
          case JsError(errors) =>
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
