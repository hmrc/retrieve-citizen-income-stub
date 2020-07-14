import sbt.ModuleID
import play.core.PlayVersion
import play.sbt.PlayImport._
import sbt._

object AppDependencies {

  val compile: Seq[ModuleID] = Seq(
//    "uk.gov.hmrc" %% "play-reactivemongo" % "6.8.0", //TODO Deprecated please use simple-reactivemongo
    "uk.gov.hmrc" %% "simple-reactivemongo" % "7.30.0-play-26", //TODO Deprecated please use simple-reactivemongo
    ws,
    "uk.gov.hmrc" %% "bootstrap-play-26" % "1.13.0",
    "uk.gov.hmrc" %% "domain" % "5.9.0-play-26",
    "com.eclipsesource" %% "play-json-schema-validator" % "0.9.4",
    "com.typesafe.play" %% "play-json-joda" % "2.7.4"
  )

  val test: Seq[ModuleID] = Seq(
    "org.scalatest" %% "scalatest" % "3.0.8",
    "org.pegdown" % "pegdown" % "1.6.0",
    "com.typesafe.play" %% "play-test" % PlayVersion.current,
    "org.scalatestplus.play" %% "scalatestplus-play" % "3.1.3"
  ).map(_ % "test")

  val all: Seq[ModuleID] = compile ++ test

}
