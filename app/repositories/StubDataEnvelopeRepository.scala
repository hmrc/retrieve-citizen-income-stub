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

package repositories

import models.{FailurePutStubResponseResult, PutStubResponseResult, StubDataEnvelope, SuccessPutStubResponseResult, _}
import org.joda.time.DateTime
import play.api.Logger
import play.api.libs.json.{JsObject, Json}
import play.modules.reactivemongo.MongoDbConnection
import reactivemongo.bson.BSONObjectID
import reactivemongo.play.json.ImplicitBSONHandlers._
import uk.gov.hmrc.mongo.ReactiveRepository

import scala.concurrent.{ExecutionContext, Future}

class StubDataEnvelopeRepository[T <: StubDataEnvelope](
  db: () => reactivemongo.api.DefaultDB,
  collectionName: String,
  domainFormat : play.api.libs.json.Format[T]
)(implicit m: Manifest[T]) extends ReactiveRepository[T, BSONObjectID](collectionName, db, domainFormat) with MongoDbConnection {

  def activateEnvelope(id: String)(implicit ec: ExecutionContext): Future[Boolean] = {
    this.collection.update(
      selector = Json.obj("_id" -> id),
      update = Json.obj("$set" -> Json.obj("activatedAt" -> DateTime.now))
    ) map {
      case _ => true
    } recover {
      case _ => false
    }
  }

  def putEnvelope(profile: T)(implicit ec: ExecutionContext): Future[PutStubResponseResult] = {

    this.collection.update(
      selector = Json.obj("_id" -> profile.id.toString),
      update = Json.toJson(profile).as[JsObject],
      upsert = true
    )  map {
      case _ =>
        SuccessPutStubResponseResult
    } recover {
      case wr =>
        Logger.warn(wr.getMessage)
        FailurePutStubResponseResult
    }
  }

  def getActiveEnvelope()(implicit ec: ExecutionContext): Future[Option[T]] =
    collection.find(Json.obj()).sort(Json.obj("activatedAt" -> -1)).one[T]

  def getEnvelopeList()(implicit ec: ExecutionContext): Future[Seq[T]] =
    find()

  import reactivemongo.api.indexes.{Index, IndexType}

  override def indexes: Seq[Index] = Seq(
    Index(Seq("activatedAt" -> IndexType.Descending), name = Some("activatedAtUniqueIdx"), unique = true, sparse = true)
  )
}
