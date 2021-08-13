package io.taig.linguist

import cats.effect.{IO, Resource}

final class GraalVmLinguistIntegrationTest extends LinguistTest {
  override val linguist: Resource[IO, Linguist[IO]] = GraalVmLinguist.default[IO]
}
