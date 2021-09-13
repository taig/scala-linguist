package io.taig.linguist

import java.nio.file.Path

abstract class Linguist[F[_]] {
  def languages: F[List[Linguist.Language]]

  def detect(path: Path, content: String): F[Option[String]]

  def detect(path: Path): F[List[String]]
}

object Linguist {
  final case class Language(name: String, extensions: List[String])
}
