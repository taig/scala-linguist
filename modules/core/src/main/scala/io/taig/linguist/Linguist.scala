package io.taig.linguist

import java.nio.file.Path

abstract class Linguist[F[_]] {
  def detect(path: Path, content: String): F[Option[String]]
}
