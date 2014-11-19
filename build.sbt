name := """play-pjax-samples"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.4"

libraryDependencies ++= Seq(
  jdbc,
  anorm,
  "org.postgresql" % "postgresql" % "9.3-1102-jdbc41",
  "org.webjars" % "webjars-play_2.11" % "2.3.0-2",
  "org.webjars" % "jquery" % "2.1.1",
  "org.webjars" % "bootstrap" % "3.3.0",
  "org.webjars" % "jquery-pjax" % "1.9.2"
)
