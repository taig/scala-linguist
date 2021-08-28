package io.taig.linguist;

import cats.effect.std.{Queue, Semaphore}
import cats.effect.{Async, Resource, Sync}
import cats.syntax.all._
import org.graalvm.polyglot.{Context, PolyglotAccess}

import java.nio.file.Path

final class GraalVmRubyLinguist[F[_]](contexts: Resource[F, Context])(implicit F: Sync[F]) extends Linguist[F] {
  override def detect(path: Path, content: String): F[Option[String]] = contexts.use { context =>
    F.blocking {
      val bindings = context.getPolyglotBindings
      bindings.putMember("path", path.toString)
      bindings.putMember("content", content)

      val result = context.eval(
        "ruby",
        """require 'linguist'
          |
          |blob = Linguist::Blob.new(Polyglot.import('path'), Polyglot.import('content'))
          |Linguist.detect(blob)&.name""".stripMargin
      )

      if (result.isNull) None
      else if (result.isString) Some(result.asString())
      else throw new IllegalStateException("Unexpected return type")
    }
  }

  override def detect(path: Path): F[List[String]] = contexts.use { context =>
    F.blocking {
      val bindings = context.getPolyglotBindings
      bindings.putMember("path", path.toString)

      val result = context.eval(
        "ruby",
        """require 'linguist'
          |
          |Linguist::Language
          |  .find_by_extension(Polyglot.import('path'))
          |  .map { |language| language.name }""".stripMargin
      )

      val size = result.getArraySize
      val builder = List.newBuilder[String]
      var index = 0

      while (index < size) {
        builder += result.getArrayElement(index.toLong).asString()
        index += 1
      }

      builder.result()
    }
  }
}

object GraalVmRubyLinguist {
  def apply[F[_]: Async](context: Context): F[Linguist[F]] =
    Semaphore[F](1).map(lock => new GraalVmRubyLinguist[F](lock.permit.as(context)))

  def default[F[_]: Async]: Resource[F, Linguist[F]] =
    context[F].evalMap(GraalVmRubyLinguist[F])

  def pooled[F[_]: Async](size: Int): Resource[F, Linguist[F]] =
    Resource.eval(Queue.bounded[F, Context](size)).flatMap { queue =>
      val contexts = Resource.make(queue.take)(queue.offer)

      List
        .fill(size)(context[F])
        .sequence
        .evalTap(_.traverse_(queue.offer))
        .as(new GraalVmRubyLinguist[F](contexts))
    }

  def context[F[_]](implicit F: Sync[F]): Resource[F, Context] = Resource.fromAutoCloseable {
    F.delay {
      Context
        .newBuilder("ruby")
        .allowIO(true)
        .allowNativeAccess(true)
        .allowPolyglotAccess(PolyglotAccess.ALL)
        .build()
    }
  }
}
