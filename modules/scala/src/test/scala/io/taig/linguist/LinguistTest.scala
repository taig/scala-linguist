package io.taig.linguist

import cats.effect.IO
import cats.syntax.all.*
import cats.effect.syntax.all.*
import cats.effect.Resource
import cats.syntax.parallel.catsSyntaxParallelTraverse1
import munit.CatsEffectSuite

import java.nio.file.Paths

abstract class LinguistTest extends CatsEffectSuite {
  def linguist: Resource[IO, Linguist[IO]]

  test("Java") {
    linguist.use(_.detect(Paths.get("Main.java"), HelloWord.Java))
      .map(obtained => assertEquals(obtained, expected = Some("Java")))
  }

  test("JavaScript") {
    linguist.use(_.detect(Paths.get("main.js"), HelloWord.JavaScript))
      .map(obtained => assertEquals(obtained, expected = Some("JavaScript")))
  }

  test("Scala") {
    linguist.use(_.detect(Paths.get("Main.scala"), HelloWord.Scala))
      .map(obtained => assertEquals(obtained, expected = Some("Scala")))
  }

  test("concurrent access") {
    val java = List.fill(100)(("Main.java", HelloWord.Java))
    val javascript = List.fill(100)(("main.js", HelloWord.JavaScript))
    val scala = List.fill(100)(("Main.scala", HelloWord.Scala))
    val all = java ++ javascript ++ scala

    linguist.use { linguist =>
      all
        .parTraverse { case (file, content) => linguist.detect(Paths.get(file), content) }
        .map(_.collect { case Some(language) => language })
    }.map { obtained =>
      assertEquals(obtained, expected = List.fill(100)("Java") ++ List.fill(100)("JavaScript") ++ List.fill(100)("Scala"))
    }
  }
}
