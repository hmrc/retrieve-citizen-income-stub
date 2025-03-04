import play.sbt.routes.RoutesKeys
import scoverage.ScoverageKeys

val appName = "retrieve-citizen-income-stub"

ThisBuild / majorVersion := 1
ThisBuild / scalaVersion := "3.6.3"

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
  .enablePlugins(play.sbt.PlayScala, SbtDistributablesPlugin)
  .settings(
    libraryDependencies ++= AppDependencies.all,
    libraryDependencySchemes += "org.scala-lang.modules" %% "scala-xml" % VersionScheme.Always,
    retrieveManaged := true,
    scoverageSettings,
    RoutesKeys.routesImport := Nil,
    PlayKeys.playDefaultPort := 9359,
    scalacOptions ++= Seq(
      "-Xfatal-warnings",
      "-Wconf:msg=Flag.*repeatedly:s"
    ),
    resolvers += Resolver.jcenterRepo,
    resolvers += "jitpack" at "https://jitpack.io"
  )
