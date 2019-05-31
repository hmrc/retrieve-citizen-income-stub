import play.core.PlayVersion
import play.sbt.PlayImport._
import sbt._

object MicroServiceBuild extends Build with MicroService {

  val appName = "retrieve-citizen-income-stub"

  override lazy val appDependencies: Seq[ModuleID] = compile ++ test()

  val compile = Seq(
    "uk.gov.hmrc" %% "play-reactivemongo" % "6.2.0",
    ws,
    "uk.gov.hmrc" %% "bootstrap-play-25" % "4.11.0",
    "uk.gov.hmrc" %% "domain" % "5.3.0",
    "com.eclipsesource" %% "play-json-schema-validator" % "0.8.9"
  )

  def test(scope: String = "test,it") = Seq(
    "uk.gov.hmrc" %% "hmrctest" % "3.3.0" % scope,
    "org.scalatest" %% "scalatest" % "3.0.0" % scope,
    "org.pegdown" % "pegdown" % "1.6.0" % scope,
    "com.typesafe.play" %% "play-test" % PlayVersion.current % scope
  )

}
