package io.taig.linguist

import cats.effect.{IO, Resource}
import munit.CatsEffectSuite

import java.nio.file.Paths

final class TruffleLinguistIntegrationTest extends CatsEffectSuite {
  val linguist: Resource[IO, ScalaLinguist[IO]] = Resource
    .fromAutoCloseable(IO(new TruffleLinguist()))
    .map(ScalaLinguist.fromJavaLinguist[IO])

  test("Java") {
    linguist.use { linguist =>
      linguist
        .detect(
          Paths.get("Main.java"),
          """class HelloWorld {
            |  static public void main( String args[] ) {
            |    System.out.println( "Hello World!" );
            |  }
            |}""".stripMargin
        )
        .map { obtained =>
          assertEquals(obtained, expected = Some("Java"))
        }
    }
  }

  test("JavaScript") {
    linguist.use { linguist =>
      linguist
        .detect(
          Paths.get("main.js"),
          """console.log("Hello World");""".stripMargin
        )
        .map { obtained =>
          assertEquals(obtained, expected = Some("JavaScript"))
        }
    }
  }

  test("Scala") {
    linguist.use { linguist =>
      linguist
        .detect(
          Paths.get("Main.scala"),
          """object HelloWorld extends App {
            |  println("Hello world!")
            |}""".stripMargin
        )
        .map { obtained =>
          assertEquals(obtained, expected = Some("Scala"))
        }
    }
  }
}
