import sbt.ModuleID
import play.core.PlayVersion
import play.sbt.PlayImport._
import sbt._

object AppDependencies {

  val bootstrapPlayVersion = "7.0.0"

  val compile: Seq[ModuleID] = Seq(
    ws,
    "uk.gov.hmrc"                       %% "bootstrap-backend-play-28" % bootstrapPlayVersion,
    "uk.gov.hmrc"                       %% "domain"                    % "6.2.0-play-28",
    "com.github.everit-org.json-schema" %  "org.everit.json.schema"    % "1.13.0"
  )

  val test: Seq[ModuleID] = Seq(
    "uk.gov.hmrc"            %% "bootstrap-test-play-28" % bootstrapPlayVersion,
    "org.scalatest"          %% "scalatest"          % "3.2.9",
    "org.pegdown"             % "pegdown"            % "1.6.0",
    "org.mockito"             % "mockito-core"       % "4.7.0",
    "org.scalacheck"         %% "scalacheck"         % "1.15.4",
    "org.scalatestplus" %% "scalacheck-1-14" % "3.1.1.1",
    "com.typesafe.play"      %% "play-test"          % PlayVersion.current
  ).map(_ % "test")

  val all: Seq[ModuleID] = compile ++ test

}
