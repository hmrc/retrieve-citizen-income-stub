package services

import com.eclipsesource.schema.{SchemaType, SchemaValidator}
import com.google.inject.Inject
import play.api.libs.json.{JsError, JsResult, JsSuccess, JsValue, Json}

import scala.io.Source

class SchemaValidation @Inject() () {

  private val requestSchema: JsValue = {
    val resource = getClass.getResourceAsStream("/schemas/des-request-schema-v1.json")
    Json.parse(Source.fromInputStream(resource).mkString)
  }

  private val validator: JsValue => JsResult[JsValue] =
    SchemaValidator().validate(Json.fromJson[SchemaType](requestSchema).get)(_)

  def validateJSON(json: JsValue): Either[JsError, JsSuccess[JsValue]] =
    if (validator(json).isSuccess)
      Right(JsSuccess(json))
    else
      Left(JsError("Does not validate against any schema"))

}
