package io.taig.linguist

import java.nio.file.Paths

import cats.effect.IO
import cats.syntax.all._
import munit.CatsEffectSuite

abstract class LinguistTest extends CatsEffectSuite {
  def linguist: Fixture[Linguist[IO]]

  override def munitFixtures: Seq[Fixture[_]] = List(linguist)

  test("Roundtrip") {
    linguist().languages.flatMap { languages =>
      languages.traverse { language =>
        language.extensions.traverse { extension =>
          linguist().detect(Paths.get(s"Main.$extension")).map { languages =>
            assert(languages.contains(language.name), s"${language.name} with .$extension")
          }
        }
      }
    }
  }

  test("Detect with code: Java") {
    linguist()
      .detect(Paths.get("Main.java"), Samples.Java)
      .map(obtained => assertEquals(obtained, expected = Some("Java")))
  }

  test("Detect with code: JavaScript") {
    linguist()
      .detect(Paths.get("main.js"), Samples.JavaScript)
      .map(obtained => assertEquals(obtained, expected = Some("JavaScript")))
  }

  test("Detect with code: Scala") {
    linguist()
      .detect(Paths.get("Main.scala"), Samples.Scala)
      .map(obtained => assertEquals(obtained, expected = Some("Scala")))
  }

  test("Detect with code: MATLAB") {
    linguist()
      .detect(Paths.get("Main.m"), Samples.Matlab)
      .map(obtained => assertEquals(obtained, expected = Some("MATLAB")))
  }

  test("Detect with code: Objective-C") {
    linguist()
      .detect(Paths.get("Main.m"), Samples.ObjectiveC)
      .map(obtained => assertEquals(obtained, expected = Some("Objective-C")))
  }

  test("Detect with code: C++") {
    linguist()
      .detect(Paths.get("Main.h"), Samples.Cpp)
      .map(obtained => assertEquals(obtained, expected = Some("C++")))
  }

  test("Detect with path: Java") {
    linguist()
      .detect(Paths.get("Main.java"))
      .map(obtained => assertEquals(obtained, expected = List("Java")))
  }

  test("concurrent access") {
    val java = List.fill(500)(("Main.java", Samples.Java))
    val javascript = List.fill(500)(("main.js", Samples.JavaScript))
    val scala = List.fill(500)(("Main.scala", Samples.Scala))
    val all = java ++ javascript ++ scala

    all
      .parTraverse { case (file, content) => linguist().detect(Paths.get(file), content) }
      .map(_.collect { case Some(language) => language })
      .map { obtained =>
        assertEquals(
          obtained,
          expected = List.fill(500)("Java") ++ List.fill(500)("JavaScript") ++ List.fill(500)("Scala")
        )
      }
  }
}
