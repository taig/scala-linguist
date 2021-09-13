package io.taig.linguist

import cats.effect.IO
import cats.effect.kernel.Resource

class GraalVmRubyDetectBenchmark extends DetectBenchmark {
  override val linguist: Resource[IO, Linguist[IO]] = GraalVmRubyLinguist.default[IO]
}
