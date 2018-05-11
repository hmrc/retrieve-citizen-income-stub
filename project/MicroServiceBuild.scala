import sbt._

object MicroServiceBuild extends Build with MicroService {
  import scala.util.Properties.envOrElse

  val appName = "retrieve-citizen-income-stub"
  val appVersion = envOrElse("RETRIEVE_CITIZEN_INCOME_STUB_VERSION", "999-SNAPSHOT")

  override lazy val appDependencies: Seq[ModuleID] = AppDependencies()
}

private object AppDependencies {
  import play.sbt.PlayImport._
  import play.core.PlayVersion


  val compile = Seq(

    ws,
    "uk.gov.hmrc" %% "domain" % "5.1.0",
    "uk.gov.hmrc" %% "play-reactivemongo" % "6.2.0",
    "uk.gov.hmrc" %% "frontend-bootstrap" % "8.19.0",
    "com.eclipsesource" %% "play-json-schema-validator" % "0.8.9"
  )

  val test = Seq(
    "uk.gov.hmrc" %% "hmrctest" % "2.3.0" % "test,it",
    "org.scalatest" %% "scalatest" % "2.2.6" % "test,it",
    "org.scalatestplus.play" %% "scalatestplus-play" % "2.0.0" % "test,it",
    "org.pegdown" % "pegdown" % "1.4.2" % "test,it",
    "com.typesafe.play" %% "play-test" % PlayVersion.current % "test,it",
    "org.jsoup" % "jsoup" % "1.8.3" % "test,it"
  )

  def apply() = compile ++ test
}
