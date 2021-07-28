package io.taig.linguist

import cats.effect.Sync
import cats.syntax.all._

import java.nio.file.Path
import scala.jdk.OptionConverters._

abstract class ScalaLinguist[F[_]] {
  def detect(path: Path, content: String): F[Option[String]]
}

object ScalaLinguist {
  def fromJavaLinguist[F[_]](linguist: Linguist)(implicit F: Sync[F]): ScalaLinguist[F] = new ScalaLinguist[F] {
    override def detect(path: Path, content: String): F[Option[String]] =
      F.blocking(linguist.detect(path, content)).map(_.toScala)
  }
}
