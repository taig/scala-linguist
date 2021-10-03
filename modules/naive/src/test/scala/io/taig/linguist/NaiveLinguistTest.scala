package io.taig.linguist

import cats.effect.IO

final class NaiveLinguistTest extends LinguistTest {
  override val linguist: Fixture[Linguist[IO]] = new Fixture[Linguist[IO]]("linguist") {
    val instance: Linguist[IO] = NaiveLinguist[IO]

    override def apply(): Linguist[IO] = instance
  }

  test("heuristics for all ambiguous extensions") {
    NaiveLinguist.extensions.foreach { case (extension, languages) =>
      if (languages.sizeIs >= 2) {
        val languagesWithHeuristic = NaiveLinguist.heuristics
          .get(extension)
          .toList
          .flatMap(_.map { case (language, _) => language })

        languages.foreach { language =>
          assert(languagesWithHeuristic.contains(language), s"Missing heuristic for $language")
        }
      }
    }
  }
}
