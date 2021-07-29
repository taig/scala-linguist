val Version = new {
  val CatsEffect = "3.2.0"
  val MunitCatsEffect = "1.0.5"
  val Scala2 = "2.13.6"
  val Scala3 = "3.0.1"
}

ThisBuild / scalaVersion := Version.Scala2
ThisBuild / crossScalaVersions := Version.Scala2 :: Version.Scala3 :: Nil

noPublishSettings

lazy val core = project
  .in(file("modules/core"))
  .settings(
    name := "scala-linguist",
    libraryDependencies ++=
      "org.typelevel" %% "cats-effect" % Version.CatsEffect % "test" ::
        "org.typelevel" %% "munit-cats-effect-3" % Version.MunitCatsEffect % "test" ::
        Nil
  )

lazy val truffle = project
  .in(file("modules/truffle"))
  .settings(
    name := "scala-linguist-truffle",
    libraryDependencies ++=
      "org.typelevel" %% "cats-effect" % Version.CatsEffect ::
        Nil
  )
  .dependsOn(core % "compile->compile;test->test")
