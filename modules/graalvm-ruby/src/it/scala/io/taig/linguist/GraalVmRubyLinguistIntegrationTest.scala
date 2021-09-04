package io.taig.linguist

import cats.effect.{IO, Resource}

final class DefaultGraalVmRubyLinguistIntegrationTest extends LinguistTest {
  override val linguist: Resource[IO, Linguist[IO]] = GraalVmRubyLinguist.default[IO]
}

final class PooledGraalVmRubyLinguistIntegrationTest extends LinguistTest {
  override val linguist: Resource[IO, Linguist[IO]] = GraalVmRubyLinguist.pooled[IO](size = 3)
}
