import com.typesafe.sbt.packager.archetypes.JavaAppPackaging
import sbt._
import Keys._

object PubmedClientBuild extends Build {
  val Organization = "com.springer"
  val Name = "pubmed-client"
  val Version = "0.0.1"
  val ScalaVersion = "2.11.7"

  lazy val root = Project(
    "root",
    file("."),
    settings = Defaults.defaultSettings ++ Seq(
      organization := Organization,
      name := Name,
      version := Version,
      scalaVersion := ScalaVersion,
      resolvers += Classpaths.typesafeReleases,
      libraryDependencies ++= Seq(
        "org.rogach" %% "scallop" % "0.9.5",
        "org.apache.httpcomponents" % "httpclient" % "4.5"/* exclude("commons-logging", "commons-logging")*/,
        "org.apache.jena" % "jena-core" % "2.13.0",
        "org.slf4j" % "slf4j-api" % "1.7.12",
        "org.slf4j" % "jcl-over-slf4j" % "1.7.12",
        "ch.qos.logback" % "logback-classic" % "1.1.3",
        "org.scala-lang.modules" % "scala-xml_2.11" % "1.0.4",
        "org.scalatest" % "scalatest_2.11" % "3.0.0-M6" % "test"
      )
    )
  ).enablePlugins(JavaAppPackaging).settings(net.virtualvoid.sbt.graph.Plugin.graphSettings: _*)

}
