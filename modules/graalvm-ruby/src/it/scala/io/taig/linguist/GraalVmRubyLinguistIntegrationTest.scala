package io.taig.linguist

import scala.concurrent.duration._

import cats.effect.{IO, Resource}

abstract class GraalVmRubyLinguistIntegrationTest extends LinguistTest {
  def resource: Resource[IO, Linguist[IO]]

  override def munitTimeout: Duration = 60.seconds

  override val linguist: Fixture[Linguist[IO]] = new Fixture[Linguist[IO]]("linguist") {
    var instance: Linguist[IO] = _

    var release: IO[Unit] = IO.unit

    override def apply(): Linguist[IO] = instance

    override def beforeAll(): Unit = {
      val (instance, release) = resource.allocated.unsafeRunSync()
      this.instance = instance
      this.release = release
    }

    override def afterAll(): Unit = {
      this.release.unsafeRunSync()
      this.instance = null
      this.release = null
    }
  }
}

final class DefaultGraalVmRubyLinguistIntegrationTest extends GraalVmRubyLinguistIntegrationTest {
  override val resource: Resource[IO, Linguist[IO]] = GraalVmRubyLinguist.default[IO]
}

final class PooledGraalVmRubyLinguistIntegrationTest extends GraalVmRubyLinguistIntegrationTest {
  override val resource: Resource[IO, Linguist[IO]] = GraalVmRubyLinguist.pooled[IO](size = 5)
}
