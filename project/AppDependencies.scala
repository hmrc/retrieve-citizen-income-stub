import sbt.ModuleID
import play.core.PlayVersion
import play.sbt.PlayImport._
import sbt._

object AppDependencies {

  val compile: Seq[ModuleID] = Seq(
    ws,
    "uk.gov.hmrc"                       %% "bootstrap-backend-play-28" % "5.12.0",
    "uk.gov.hmrc"                       %% "domain"                    % "6.2.0-play-28",
    "com.github.everit-org.json-schema" %  "org.everit.json.schema"    % "1.13.0"
  )

  val test: Seq[ModuleID] = Seq(
    "org.scalatest"          %% "scalatest"          % "3.2.9",
    "org.pegdown"             % "pegdown"            % "1.6.0",
    "org.mockito"             % "mockito-core"       % "3.12.4",
    "org.scalacheck"         %% "scalacheck"         % "1.15.4",
    "org.scalatestplus" %% "scalacheck-1-14" % "3.1.1.1",
    "com.typesafe.play"      %% "play-test"          % PlayVersion.current,
    "org.scalatestplus.play" %% "scalatestplus-play" % "5.1.0",
    "com.vladsch.flexmark" % "flexmark-all" % "0.35.10"
  ).map(_ % "test")

  val all: Seq[ModuleID] = compile ++ test

}
