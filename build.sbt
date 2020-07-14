import uk.gov.hmrc.DefaultBuildSettings.scalaSettings
import uk.gov.hmrc.sbtdistributables.SbtDistributablesPlugin.publishingSettings

val appName = "retrieve-citizen-income-stub"

lazy val microservice = Project(appName, file("."))
  .enablePlugins(play.sbt.PlayScala, SbtAutoBuildPlugin, SbtGitVersioning, SbtDistributablesPlugin, SbtArtifactory)
  .settings(
    scalaVersion := "2.12.10",
    libraryDependencies ++= AppDependencies.all,
    retrieveManaged := true,
    evictionWarningOptions in update := EvictionWarningOptions.default.withWarnScalaVersionEviction(false),
    scalaSettings,
    publishingSettings,
    PlayKeys.playDefaultPort := 9359,
    majorVersion := 0,
    resolvers ++= Seq(
      Resolver.bintrayRepo("hmrc", "releases"),
      Resolver.jcenterRepo,
      Resolver.bintrayRepo("emueller", "maven")
    )
  )
