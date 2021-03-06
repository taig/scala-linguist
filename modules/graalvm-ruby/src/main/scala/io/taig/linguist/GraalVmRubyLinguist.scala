package io.taig.linguist;

import cats.effect.std.{Queue, Semaphore}
import cats.effect.{Async, Resource, Sync}
import cats.syntax.all._
import org.graalvm.polyglot.{Context, PolyglotAccess, Value}

import java.nio.file.Path

final class GraalVmRubyLinguist[F[_]](contexts: Resource[F, Either[Throwable, Context]])(implicit F: Sync[F])
    extends Linguist[F] {
  override val languages: F[List[Linguist.Language]] = contexts.rethrow.use { context =>
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

  override def detect(path: Path, content: String): F[Option[String]] = contexts.rethrow.use { context =>
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

  override def detect(path: Path): F[List[String]] = contexts.rethrow.use { context =>
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
  // This is a costly hack around context initialization issues that seem to be caused by sbt's class loader layering
  // mechanism when executing tests
  @volatile private var ready: Either[Throwable, Boolean] = Right(false)

  new Thread() {
    override def run(): Unit = try {
      val context = Context.create("ruby")
      context.initialize("ruby")
      context.close()
      ready = Right(true)
    } catch { case throwable: Throwable => ready = Left(throwable) }
  }.start()

  private def awaitInitialization[F[_]](implicit F: Sync[F]): F[Unit] =
    if (ready.contains(true)) F.unit
    else
      F.blocking { while (ready.contains(false)) {}; ready }.flatMap {
        case Right(true)     => F.unit
        case Left(throwable) => F.raiseError(throwable)
        case Right(false)    => F.raiseError(new IllegalStateException("Unreachable"))
      }

  def apply[F[_]: Async](context: Context): F[Linguist[F]] =
    awaitInitialization[F] *> Semaphore[F](1).map(lock => new GraalVmRubyLinguist[F](lock.permit.as(context.asRight)))

  def default[F[_]: Async]: Resource[F, Linguist[F]] = pooled(1)

  def pooled[F[_]: Async](size: Int): Resource[F, Linguist[F]] =
    Resource.eval(awaitInitialization[F] *> Queue.unbounded[F, Either[Throwable, Context]]).flatMap { queue =>
      val contexts = Resource.make(queue.take)(queue.offer)
      List
        .fill(size)(context[F].attempt)
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
