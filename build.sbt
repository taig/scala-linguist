val Version = new {
  val Cats = "2.7.0"
  val CatsEffect = "3.3.4"
  val Munit = "0.7.29"
  val MunitCatsEffect = "1.0.7"
  val Scala2 = "2.13.7"
  val Scala3 = "3.1.0"
}

ThisBuild / crossScalaVersions := Version.Scala2 :: Version.Scala3 :: Nil
ThisBuild / developers := List(Developer("taig", "Niklas Klein", "mail@taig.io", url("https://taig.io/")))
ThisBuild / dynverVTagPrefix := false
ThisBuild / homepage := Some(url("https://github.com/taig/scala-linguist/"))
ThisBuild / licenses := List("MIT" -> url("https://raw.githubusercontent.com/taig/scala-linguist/main/LICENSE"))
ThisBuild / scalaVersion := Version.Scala2
ThisBuild / versionScheme := Some("early-semver")

lazy val root = project
  .in(file("."))
  .settings(noPublishSettings)
  .settings(
    name := "scala-linguist"
  )
  .aggregate(core.jvm, core.js, graalvmRuby, naive.jvm, naive.js, benchmarks)

lazy val core = crossProject(JVMPlatform, JSPlatform)
  .crossType(CrossType.Pure)
  .in(file("modules/core"))
  .settings(
    name := "scala-linguist-core",
    libraryDependencies ++=
      "org.typelevel" %%% "cats-core" % Version.Cats ::
        "org.scalameta" %%% "munit" % Version.Munit % "test" ::
        "org.typelevel" %%% "cats-effect" % Version.CatsEffect % "test" ::
        "org.typelevel" %%% "munit-cats-effect-3" % Version.MunitCatsEffect % "test" ::
        Nil
  )

lazy val graalvmRuby = project
  .in(file("modules/graalvm-ruby"))
  .settings(
    name := "scala-linguist-graalvm-ruby",
    libraryDependencies ++=
      "org.typelevel" %% "cats-effect" % Version.CatsEffect ::
        Nil
  )
  .dependsOn(core.jvm % "compile->compile;test->test")

lazy val naive = crossProject(JVMPlatform, JSPlatform)
  .crossType(CrossType.Pure)
  .in(file("modules/naive"))
  .settings(
    name := "scala-linguist-naive"
  )
  .dependsOn(core % "compile->compile;test->test")

lazy val benchmarks = project
  .in(file("modules/benchmarks"))
  .enablePlugins(JmhPlugin)
  .settings(noPublishSettings)
  .settings(
    name := "scala-linguist-benchmarks"
  )
  .dependsOn(graalvmRuby, naive.jvm)
