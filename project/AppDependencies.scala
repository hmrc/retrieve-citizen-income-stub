import sbt.ModuleID
import play.sbt.PlayImport.*
import sbt.*

object AppDependencies {

  private val bootstrapPlayVersion = "10.8.0"
  private val playVersion = "play-30"

  val compile: Seq[ModuleID] = Seq(
    ws,
    "uk.gov.hmrc"                       %% s"bootstrap-backend-$playVersion" % bootstrapPlayVersion,
    "uk.gov.hmrc"                       %% s"domain-$playVersion"            % "13.0.0",
    "com.github.everit-org.json-schema" %  "org.everit.json.schema"          % "1.14.4"
  )

  val test: Seq[ModuleID] = Seq(
    "uk.gov.hmrc"       %% s"bootstrap-test-$playVersion" % bootstrapPlayVersion,
    "org.mockito"       %  "mockito-core"                 % "5.23.0",
    "org.scalacheck"    %% "scalacheck"                   % "1.19.0",
    "org.scalatestplus" %% "scalacheck-1-17"              % "3.2.18.0"
  ).map(_ % "test")

  val all: Seq[ModuleID] = compile ++ test
}
