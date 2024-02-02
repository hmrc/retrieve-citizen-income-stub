import sbt.ModuleID
import play.sbt.PlayImport._
import sbt._

object AppDependencies {

  private val bootstrapPlayVersion = "8.4.0"
  private val playVersion = "-play-30"

  val compile: Seq[ModuleID] = Seq(
    ws,
    "uk.gov.hmrc"                       %% s"bootstrap-backend$playVersion" % bootstrapPlayVersion,
    "uk.gov.hmrc"                       %% s"domain$playVersion"             % "9.0.0",
    "com.github.everit-org.json-schema" %  "org.everit.json.schema"         % "1.14.4"
  )

  val test: Seq[ModuleID] = Seq(
    "uk.gov.hmrc"       %% s"bootstrap-test$playVersion" % bootstrapPlayVersion,
    "org.pegdown"       % "pegdown"                      % "1.6.0",
    "org.mockito"       % "mockito-core"                 % "5.10.0",
    "org.scalacheck"    %% "scalacheck"                  % "1.17.0",
    "org.scalatestplus" %% "scalacheck-1-17"             % "3.2.17.0",
    "org.playframework" %% "play-test"                   % "3.0.1"
  ).map(_ % "test")

  val all: Seq[ModuleID] = compile ++ test
}
