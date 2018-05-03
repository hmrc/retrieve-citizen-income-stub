package modules

import com.google.inject.AbstractModule
import config.{LocalAuditConnector, LocalAuthConnector}
import filters.{CSRFExceptionsFilterProvider, DeviceIdCookieFilterProvider, SessionTimeoutFilterProvider}
import uk.gov.hmrc.play.audit.http.connector.AuditConnector
import uk.gov.hmrc.play.frontend.auth.connectors.AuthConnector
import uk.gov.hmrc.play.frontend.filters._


class LocalGuiceModule extends AbstractModule {

  override def configure() = {

    //These library components must be bound in this way, or using providers
    bind(classOf[AuthConnector]).toInstance(LocalAuthConnector)
    bind(classOf[AuditConnector]).toInstance(LocalAuditConnector)
    bind(classOf[CookieCryptoFilter]).toInstance(SessionCookieCryptoFilter)
    bind(classOf[HeadersFilter]).toInstance(HeadersFilter)
    bind(classOf[DeviceIdFilter]).toProvider(classOf[DeviceIdCookieFilterProvider])
    bind(classOf[CSRFExceptionsFilter]).toProvider(classOf[CSRFExceptionsFilterProvider])
    bind(classOf[SessionTimeoutFilter]).toProvider(classOf[SessionTimeoutFilterProvider])
    bind(classOf[CacheControlFilter]).toInstance(CacheControlFilter.fromConfig("caching.allowedContentTypes"))
  }
}
