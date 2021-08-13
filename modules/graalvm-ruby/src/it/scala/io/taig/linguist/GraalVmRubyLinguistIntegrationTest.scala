package io.taig.linguist

import cats.effect.{IO, Resource}

final class GraalVmRubyLinguistIntegrationTest extends LinguistTest {
  override val linguist: Resource[IO, Linguist[IO]] = GraalVmRubyLinguist.default[IO]
}
