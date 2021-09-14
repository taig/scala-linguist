package io.taig.linguist

import java.nio.file.Paths
import java.util.concurrent.TimeUnit

import cats.effect.IO
import cats.effect.kernel.Resource
import cats.effect.unsafe.implicits.global
import org.openjdk.jmh.annotations._

@OutputTimeUnit(TimeUnit.MILLISECONDS)
@BenchmarkMode(Array(Mode.Throughput))
@State(Scope.Benchmark)
abstract class DetectBenchmark {
  def linguist: Resource[IO, Linguist[IO]]

  private var instance: Linguist[IO] = _

  private var release: IO[Unit] = _

  @Setup(Level.Trial)
  def setup(): Unit = {
    val (instance, release) = GraalVmRubyLinguist.default[IO].allocated.unsafeRunSync()
    this.instance = instance
    this.release = release
  }

  @TearDown(Level.Trial)
  def teardown(): Unit = {
    release.unsafeRunSync()
    instance = null
    release = null
  }

  @Benchmark
  def detectExtension(): Unit = {
    instance.detect(Paths.get("Main.scala")).unsafeRunSync()
    ()
  }

  @Benchmark
  def detectCode(): Unit = {
    instance.detect(Paths.get("Main.m"), DetectBenchmark.MatlabHelloWorld).unsafeRunSync()
    ()
  }
}

object DetectBenchmark {
  val MatlabHelloWorld =
    """% Hello World in MATLAB.
      |
      |disp('Hello World');""".stripMargin
}

class GraalVmRubyDefaultDetectBenchmark extends DetectBenchmark {
  override val linguist: Resource[IO, Linguist[IO]] = GraalVmRubyLinguist.default[IO]
}

class GraalVmRubyPooledDetectBenchmark extends DetectBenchmark {
  override val linguist: Resource[IO, Linguist[IO]] = GraalVmRubyLinguist.pooled[IO](size = 4)
}

