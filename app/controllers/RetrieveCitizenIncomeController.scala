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

package controllers

import javax.inject.Inject
import play.api.libs.json._
import play.api.mvc.{Action, ControllerComponents}
import services.{CitizenIncomeService, SchemaValidator}
import uk.gov.hmrc.play.bootstrap.backend.controller.BackendController

class RetrieveCitizenIncomeController @Inject() (
    citizenIncomeService: CitizenIncomeService,
    schemaValidator: SchemaValidator,
    cc: ControllerComponents
) extends BackendController(cc) {

  def getRetrieveCitizenIncome(nino: String): Action[JsValue] =
    Action(parse.json) { implicit request =>
      if (schemaValidator.isJsonValid(request.body))
        citizenIncomeService.getRetrieveCitizenIncome(nino)
      else
        BadRequest(
          JsObject(
            Map(
              "code"   -> JsString("INVALID_PAYLOAD"),
              "reason" -> JsString("Submission has not passed validation. Invalid Payload.")
            )
          )
        )
    }

}
