package config

import uk.gov.hmrc.http._
import uk.gov.hmrc.play.audit.http.connector.AuditConnector
import uk.gov.hmrc.play.config.{RunMode, ServicesConfig}
import uk.gov.hmrc.play.frontend.auth.connectors.AuthConnector
import uk.gov.hmrc.play.frontend.config.LoadAuditingConfig
import uk.gov.hmrc.play.http.ws._

object LocalAuditConnector extends AuditConnector with RunMode {
  override lazy val auditingConfig = LoadAuditingConfig(s"$env.auditing")
}

trait WSHttp extends HttpGet with WSGet with HttpPut with WSPut with HttpPost with WSPost with HttpDelete with WSDelete with HttpPatch with WSPatch

object WSHttp extends WSHttp {
  override val hooks = NoneRequired
}

object LocalAuthConnector extends AuthConnector with ServicesConfig with WSHttp {
  override val hooks = NoneRequired
  override lazy val serviceUrl: String = baseUrl("auth")

  override val http: CoreGet = WSHttp
}
