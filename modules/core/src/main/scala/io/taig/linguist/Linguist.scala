package io.taig.linguist

import cats.~>

import java.nio.file.Path

abstract class Linguist[F[_]] { self =>
  def languages: F[List[Linguist.Language]]

  def detect(path: Path, content: String): F[Option[String]]

  def detect(path: Path): F[List[String]]

  final def mapK[G[_]](fK: F ~> G): Linguist[G] = new Linguist[G] {
    override def languages: G[List[Linguist.Language]] = fK(self.languages)

    override def detect(path: Path, content: String): G[Option[String]] = fK(self.detect(path, content))

    override def detect(path: Path): G[List[String]] = fK(self.detect(path))
  }
}

object Linguist {
  final case class Language(name: String, extensions: List[String])
}
