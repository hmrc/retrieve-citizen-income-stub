import play.sbt.routes.RoutesKeys
import uk.gov.hmrc.sbtdistributables.SbtDistributablesPlugin.publishingSettings
import scoverage.ScoverageKeys

val appName = "retrieve-citizen-income-stub"

lazy val scoverageSettings = {
  val scoverageExcludesPattens = List(
    ".*Routes.*",
    ".*Reverse.*Controller"
  )
  Seq(
    ScoverageKeys.coverageExcludedPackages := scoverageExcludesPattens.mkString(";", ";", ""),
    ScoverageKeys.coverageMinimumStmtTotal := 95,
    ScoverageKeys.coverageFailOnMinimum := true,
    ScoverageKeys.coverageHighlighting := true
  )
}

lazy val microservice = Project(appName, file("."))
  .enablePlugins(play.sbt.PlayScala, SbtAutoBuildPlugin, SbtGitVersioning, SbtDistributablesPlugin, SbtArtifactory)
  .settings(
    scalaVersion := "2.13.8",
    libraryDependencies ++= AppDependencies.all,
    retrieveManaged := true,
    publishingSettings,
    scoverageSettings,
    RoutesKeys.routesImport := Nil,
    PlayKeys.playDefaultPort := 9359,
    scalacOptions += "-Xfatal-warnings",
    majorVersion := 1,
    resolvers += Resolver.jcenterRepo,
    resolvers += "jitpack" at "https://jitpack.io"
  )
