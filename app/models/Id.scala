package models

import play.api.libs.json.{JsResult, JsString, JsValue}

case class Id(id: String)

object Id {

  import play.api.libs.json.{Format, Reads, Writes}

  implicit def stringToId(s: String) = new Id(s)

  private val idWrite: Writes[Id] = new Writes[Id] {
    override def writes(value: Id): JsValue = JsString(value.id)
  }

  private val idRead: Reads[Id] = new Reads[Id] {
    override def reads(js: JsValue): JsResult[Id] = js match {
      case v: JsString => v.validate[String].map(Id.apply)
      case noParsed => throw new Exception(s"Could not read Json value of 'id' in $noParsed")
    }
  }
  implicit val idFormats = Format(idRead, idWrite)
}
