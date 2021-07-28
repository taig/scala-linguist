val Version = new {
  val CatsEffect = "3.2.0"
  val MunitCatsEffect = "1.0.5"
  val Scala2 = "2.13.6"
  val Scala3 = "3.0.1"
}

val javaOnly = Def.settings(
  autoScalaLibrary := false,
  crossPaths := false,
  crossScalaVersions := Nil
)

ThisBuild / scalaVersion := Version.Scala3

noPublishSettings

lazy val core = project
  .in(file("modules/core"))
  .settings(javaOnly)
  .settings(
    name := "java-linguist-core"
  )

lazy val scala = project
  .in(file("modules/scala"))
  .settings(
    crossScalaVersions := Version.Scala2 :: Version.Scala3 :: Nil,
    libraryDependencies ++=
      "org.typelevel" %% "cats-effect" % Version.CatsEffect ::
        Nil,
    name := "java-linguist-scala"
  )
  .dependsOn(core)

lazy val truffle = project
  .in(file("modules/truffle"))
  .settings(javaOnly)
  .settings(
    libraryDependencies ++=
      "org.typelevel" %% "munit-cats-effect-3" % Version.MunitCatsEffect % "test" ::
        Nil,
    name := "java-linguist-truffle"
  )
  .dependsOn(core, scala % "test->test")
