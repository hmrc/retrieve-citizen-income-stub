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

package services

import models.{PutStubResponseResult, StubDataEnvelope}
import repositories.StubDataEnvelopeRepository

import scala.concurrent.{ExecutionContext, Future}

class StubDataEnvelopeService[T <: StubDataEnvelope](val repo: StubDataEnvelopeRepository[T]) {

  def getActiveEnvelope(implicit ec: ExecutionContext): Future[Option[T]] = {

    repo.getActiveEnvelope()
  }

  def putAndActivateEnvelope(tcr: T)(implicit ec: ExecutionContext): Future[PutStubResponseResult] = {

    repo.putEnvelope(tcr) map { r =>
      repo.activateEnvelope(tcr.id)
      r
    }
  }

  def activateEnvelope(id: Option[String])(implicit ec: ExecutionContext): Option[Future[Boolean]] = {

    id.map(repo.activateEnvelope(_))
  }

  def getEnvelopeList()(implicit ec: ExecutionContext): Future[(Option[String], Seq[T])] = {

    repo.getActiveEnvelope() flatMap { active =>

      repo.getEnvelopeList() map { list =>

        (active.map(_.id), list)
      }
    }
  }
}
