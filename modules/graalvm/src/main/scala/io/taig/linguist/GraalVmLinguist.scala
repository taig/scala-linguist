package io.taig.linguist;

import cats.effect.{Async, Resource}
import org.graalvm.polyglot.{Context, PolyglotAccess}

import java.nio.file.Path
import java.util.concurrent.{Executors, TimeUnit}
import scala.concurrent.ExecutionContext

final class GraalVmLinguist[F[_]](context: Context, execution: ExecutionContext)(implicit F: Async[F])
    extends Linguist[F] {
  override def detect(path: Path, content: String): F[Option[String]] = {
    val fa = F.delay {
      val bindings = context.getPolyglotBindings
      bindings.putMember("path", path.toString)
      bindings.putMember("content", content)

      val result = context.eval(
        "ruby",
        s"""require 'linguist'
           |
           |blob = Linguist::Blob.new(Polyglot.import('path'), Polyglot.import('content'))
           |Linguist.detect(blob)&.name""".stripMargin
      )

      if (result.isNull) None
      else if (result.isString) Some(result.asString())
      else throw new IllegalStateException("Unexpected return type")
    }

    F.evalOn(fa, execution)
  }
}

object GraalVmLinguist {
  def apply[F[_]](context: Context)(implicit F: Async[F]): Resource[F, Linguist[F]] =
    Resource
      .make(F.delay(Executors.newSingleThreadExecutor())) { executor =>
        F.delay {
          executor.shutdown()
          executor.awaitTermination(10, TimeUnit.SECONDS)
          ()
        }
      }
      .map(ExecutionContext.fromExecutorService)
      .map(new GraalVmLinguist[F](context, _))

  def default[F[_]](implicit F: Async[F]): Resource[F, Linguist[F]] = {
    val context = F.delay {
      Context
        .newBuilder("ruby")
        .allowIO(true)
        .allowNativeAccess(true)
        .allowPolyglotAccess(PolyglotAccess.ALL)
        .build()
    }

    Resource.fromAutoCloseable(context).flatMap(GraalVmLinguist[F])
  }
}
