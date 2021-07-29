package io.taig.linguist

import cats.effect.{IO, Resource}

final class TruffleLinguistIntegrationTest extends LinguistTest {
  override val linguist: Resource[IO, Linguist[IO]] = TruffleLinguist.default[IO]
}
