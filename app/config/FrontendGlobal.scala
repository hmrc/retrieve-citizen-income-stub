package config

import com.typesafe.config.Config
import net.ceedubs.ficus.Ficus._
import play.api._
import uk.gov.hmrc.play.config.{AppName, ControllerConfig, RunMode}
import uk.gov.hmrc.play.frontend.filters.{FrontendAuditFilter, FrontendLoggingFilter, MicroserviceFilterSupport}

object LocalControllerConfig extends ControllerConfig {
  lazy val controllerConfigs = Play.current.configuration.underlying.as[Config]("controllers")
}

object LocalLoggingFilter extends FrontendLoggingFilter with MicroserviceFilterSupport {
  override def controllerNeedsLogging(controllerName: String) = LocalControllerConfig.paramsForController(controllerName).needsLogging
}

object LocalFrontendAuditFilter extends FrontendAuditFilter with RunMode with AppName with MicroserviceFilterSupport {
  override lazy val maskedFormFields: Seq[String] = Seq.empty[String]
  override lazy val applicationPort: Option[Int] = None
  override lazy val auditConnector = LocalAuditConnector
  override def controllerNeedsAuditing(controllerName: String) = LocalControllerConfig.paramsForController(controllerName).needsAuditing
}
