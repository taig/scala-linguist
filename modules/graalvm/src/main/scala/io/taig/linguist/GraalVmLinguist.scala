package io.taig.linguist;

import cats.effect.std.Semaphore
import cats.effect.{Async, Resource, Sync}
import cats.syntax.all._
import org.graalvm.polyglot.{Context, PolyglotAccess}

import java.nio.file.Path

final class GraalVmLinguist[F[_]](lock: Semaphore[F])(context: Context)(implicit F: Sync[F]) extends Linguist[F] {
  override def detect(path: Path, content: String): F[Option[String]] = lock.permit.surround {
    F.blocking {
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
  }
}

object GraalVmLinguist {
  def apply[F[_]](context: Context)(implicit F: Async[F]): F[GraalVmLinguist[F]] =
    Semaphore[F](1).map(new GraalVmLinguist[F](_)(context))

  def default[F[_]](implicit F: Async[F]): Resource[F, Linguist[F]] = {
    val context = F.delay {
      Context
        .newBuilder("ruby")
        .allowIO(true)
        .allowNativeAccess(true)
        .allowPolyglotAccess(PolyglotAccess.ALL)
        .build()
    }

    Resource.fromAutoCloseable(context).evalMap(GraalVmLinguist[F])
  }
}
