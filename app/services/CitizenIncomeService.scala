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
import play.api.mvc.Results._
import repositories.CitizenIncomeRepository._

class CitizenIncomeService @Inject()() {

  private val logger: Logger = Logger(this.getClass)

  def getRetrieveCitizenIncome(nino: String): Result =
    nino match {
      case "AA111111A" => Ok(successMatchOneElement)
      case "AA222222A" => Ok(successMatchTwoElements)
      case "AA333333A" => Ok(successMatchTwoTaxYears)
      case "AA444444A" => Ok(successNoMatch)
      case "AA555555A" => NotFound(errorNotFound)
      case "AA666666A" => NotFound(errorNotFoundNino)
      case "AA777777A" => InternalServerError(serverError)
      case "AA888888A" => InternalServerError(serviceUnavailable)
      case "AA777771A" => Ok(singleEmpSingleTaxYear)
      case "AA777772A" => Ok(multipleEmpSingleTaxYear)
      case "AA777773A" => Ok(multipleEmpMultipleTaxYears)
      case "AA777774A" => Ok(multipleEmpMultipleTaxYearsOp)
      case "AA777775A" => Ok(multipleEmpMultipleTaxYearsYdr)
      case "AA777776A" => Ok(validNinoWithNoData)
      case _ =>
        logger.debug(s"Nino $nino was not found.")
        NotFound
    }
}
