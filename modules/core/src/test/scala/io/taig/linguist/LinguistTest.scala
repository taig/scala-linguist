package io.taig.linguist

import cats.effect.{IO, Resource}
import cats.syntax.parallel.catsSyntaxParallelTraverse1
import munit.CatsEffectSuite

import java.nio.file.Paths

abstract class LinguistTest extends CatsEffectSuite {
  def linguist: Resource[IO, Linguist[IO]]

  test("Detect with code: Java") {
    linguist
      .use(_.detect(Paths.get("Main.java"), HelloWord.Java))
      .map(obtained => assertEquals(obtained, expected = Some("Java")))
  }

  test("Detect with code: JavaScript") {
    linguist
      .use(_.detect(Paths.get("main.js"), HelloWord.JavaScript))
      .map(obtained => assertEquals(obtained, expected = Some("JavaScript")))
  }

  test("Detect with code: Scala") {
    linguist
      .use(_.detect(Paths.get("Main.scala"), HelloWord.Scala))
      .map(obtained => assertEquals(obtained, expected = Some("Scala")))
  }

  test("Detect with code: MATLAB") {
    linguist
      .use(_.detect(Paths.get("Main.m"), HelloWord.MatLab))
      .map(obtained => assertEquals(obtained, expected = Some("MATLAB")))
  }

  test("Detect with path: Java") {
    linguist
      .use(_.detect(Paths.get("Main.java")))
      .map(obtained => assertEquals(obtained, expected = List("Java")))
  }

  test("concurrent access") {
    val java = List.fill(500)(("Main.java", HelloWord.Java))
    val javascript = List.fill(500)(("main.js", HelloWord.JavaScript))
    val scala = List.fill(500)(("Main.scala", HelloWord.Scala))
    val all = java ++ javascript ++ scala

    linguist
      .use { linguist =>
        all
          .parTraverse { case (file, content) => linguist.detect(Paths.get(file), content) }
          .map(_.collect { case Some(language) => language })
      }
      .map { obtained =>
        assertEquals(
          obtained,
          expected = List.fill(500)("Java") ++ List.fill(500)("JavaScript") ++ List.fill(500)("Scala")
        )
      }
  }
}
