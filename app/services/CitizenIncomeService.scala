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

import com.google.inject.Inject
import play.api.Logger
import play.api.mvc.Result
import play.api.mvc.Results.{NotFound, Ok, InternalServerError}
import repositories.CitizenIncomeRepository._

class CitizenIncomeService @Inject()() {

  private val logger: Logger = Logger(this.getClass)

  private val ninoResults: Map[String, Result] = Map(
    "AA111111A" -> Ok(successMatchOneElement),
    "AA222222A" -> Ok(successMatchTwoElements),
    "AA333333A" -> Ok(successMatchTwoTaxYears),
    "AA444444A" -> Ok(successNoMatch),
    "AA555555A" -> NotFound(errorNotFound),
    "AA666666A" -> NotFound(errorNotFoundNino),
    "AA777777A" -> InternalServerError(serverError),
    "AA888888A" -> InternalServerError(serviceUnavailable),
    "AA777771A" -> Ok(singleEmpSingleTaxYear),
    "AA777772A" -> Ok(multipleEmpSingleTaxYear),
    "AA777773A" -> Ok(multipleEmpMultipleTaxYears),
    "AA777774A" -> Ok(multipleEmpMultipleTaxYearsOp),
    "AA777775A" -> Ok(multipleEmpMultipleTaxYearsYdr),
    "AA777776A" -> Ok(validNinoWithNoData)
  ).withDefault( nino => {
    logger.debug(s"Nino $nino was not found.")
    NotFound
  })

  def getRetrieveCitizenIncome(nino: String): Result = ninoResults(nino)

}
