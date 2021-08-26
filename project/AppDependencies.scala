import sbt.ModuleID
import play.core.PlayVersion
import play.sbt.PlayImport._
import sbt._

object AppDependencies {

  val compile: Seq[ModuleID] = Seq(
    ws,
    "uk.gov.hmrc"                       %% "bootstrap-backend-play-26" % "5.12.0",
    "uk.gov.hmrc"                       %% "domain"                    % "5.9.0-play-26",
    "com.github.everit-org.json-schema" %  "org.everit.json.schema"    % "1.13.0"
  )

  val test: Seq[ModuleID] = Seq(
    "org.scalatest"          %% "scalatest"          % "3.0.8",
    "org.pegdown"             % "pegdown"            % "1.6.0",
    "org.mockito"             % "mockito-core"       % "3.4.0",
    "org.scalacheck"         %% "scalacheck"         % "1.14.1",
    "com.typesafe.play"      %% "play-test"          % PlayVersion.current,
    "org.scalatestplus.play" %% "scalatestplus-play" % "3.1.3"
  ).map(_ % "test")

  val all: Seq[ModuleID] = compile ++ test

}
