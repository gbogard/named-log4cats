lazy val scala212 = "2.12.13"
lazy val scala213 = "2.13.4"
lazy val supportedScalaVersions = List(scala212, scala213)

ThisBuild / scalaVersion     := scala213
ThisBuild / version          := "0.1.0-SNAPSHOT"
ThisBuild / organization     := "dev.guillaumebogard"

lazy val root = (project in file("."))
  .settings(
    name := "named-log4cats",
    crossScalaVersions := supportedScalaVersions,
    libraryDependencies ++= Seq(
      "org.typelevel" %% "log4cats-core",
      "org.typelevel" %% "log4cats-slf4j"
    ).map(_ % "2.1.0")
  )

lazy val exampleSettings = Seq(
  libraryDependencies += "ch.qos.logback" % "logback-classic" % "1.2.3"
)

lazy val basicExample = (project in file("examples/basic")).settings(exampleSettings).dependsOn(root)
lazy val autoExample = (project in file("examples/auto")).settings(exampleSettings).dependsOn(root)
