package io.taig.linguist;

import java.nio.file.Path

import cats.effect.std.{Queue, Semaphore}
import cats.effect.{Async, Resource, Sync}
import cats.syntax.all._
import org.graalvm.polyglot.{Context, PolyglotAccess, Value}

final class GraalVmRubyLinguist[F[_]](contexts: Resource[F, Context])(implicit F: Sync[F]) extends Linguist[F] {
  override val languages: F[List[Linguist.Language]] = contexts.use { context =>
    F.blocking {
      val result = context.eval("ruby", "Linguist::Language.all")

      val builder = List.newBuilder[Linguist.Language]
      val size = result.getArraySize
      var index = 0

      while (index < size) {
        val value = result.getArrayElement(index.toLong)
        val name = value.getMember("name").execute().asString()
        val extensions = unsafeToList(value.getMember("extensions").execute()).map(_.dropWhile(_ == '.'))
        builder += Linguist.Language(name, extensions)
        index += 1
      }

      builder.result()
    }
  }

  override def detect(path: Path, content: String): F[Option[String]] = contexts.use { context =>
    F.blocking {
      val bindings = context.getPolyglotBindings
      bindings.putMember("path", path.toString)
      bindings.putMember("content", content)

      val result = context.eval(
        "ruby",
        """blob = Linguist::Blob.new(Polyglot.import('path'), Polyglot.import('content'))
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

      val result: Value = context.eval(
        "ruby",
        """Linguist::Language
          |  .find_by_extension(Polyglot.import('path'))
          |  .map { |language| language.name }""".stripMargin
      )

      unsafeToList(result)
    }
  }

  def unsafeToList[G[_]](value: Value): List[String] = {
    val size = value.getArraySize
    if (size == 0) Nil
    else {
      val builder = List.newBuilder[String]
      var index = 0

      while (index < size) {
        builder += value.getArrayElement(index.toLong).asString()
        index += 1
      }

      builder.result()
    }
  }
}

object GraalVmRubyLinguist {
  // This is a hack around context initialization issues that seem to be caused by sbt's class loader layering
  // mechanism when executing tests
  try {
    val context = Context.create("ruby")
    context.initialize("ruby")
    context.close()
  } catch {
    case exception: Throwable =>
      new IllegalStateException("Failed to initialize polyglot ruby context", exception)
        .printStackTrace(System.err)
  }

  def apply[F[_]: Async](context: Context): F[Linguist[F]] =
    Semaphore[F](1).map(lock => new GraalVmRubyLinguist[F](lock.permit.as(context)))

  def default[F[_]: Async]: Resource[F, Linguist[F]] = context[F].evalMap(GraalVmRubyLinguist[F])

  def pooled[F[_]: Async](size: Int): Resource[F, Linguist[F]] =
    Resource.eval(Queue.unbounded[F, Context]).flatMap { queue =>
      val contexts = Resource.make(queue.take)(queue.offer)

      List
        .fill(size)(context[F])
        .parTraverse_(_.evalMap(queue.offer))
        .start
        .as(new GraalVmRubyLinguist[F](contexts))
    }

  def context[F[_]](implicit F: Sync[F]): Resource[F, Context] = Resource.fromAutoCloseable {
    F.blocking {
      val context = Context
        .newBuilder("ruby")
        .allowIO(true)
        .allowNativeAccess(true)
        .allowPolyglotAccess(PolyglotAccess.ALL)
        .build()

      context.eval("ruby", "require 'linguist'")
      context
    }
  }
}
