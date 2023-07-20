import sbt.ModuleID
import play.sbt.PlayImport._
import sbt._

object AppDependencies {

  private val bootstrapPlayVersion = "7.19.0"
  private val playVersion = "-play-28"

  val compile: Seq[ModuleID] = Seq(
    ws,
    "uk.gov.hmrc"                       %% s"bootstrap-backend$playVersion" % bootstrapPlayVersion,
    "uk.gov.hmrc"                       %% "domain"                         % s"8.3.0$playVersion",
    "com.github.everit-org.json-schema" %  "org.everit.json.schema"         % "1.13.0"
  )

  val test: Seq[ModuleID] = Seq(
    "uk.gov.hmrc"       %% s"bootstrap-test$playVersion" % bootstrapPlayVersion,
    "org.pegdown"       % "pegdown"                      % "1.6.0",
    "org.mockito"       % "mockito-core"                 % "4.11.0",
    "org.scalacheck"    %% "scalacheck"                  % "1.16.0",
    "org.scalatestplus" %% "scalacheck-1-14"             % "3.2.2.0",
    "com.typesafe.play" %% "play-test"                   % "2.8.19"
  ).map(_ % "test")

  val all: Seq[ModuleID] = compile ++ test
}
