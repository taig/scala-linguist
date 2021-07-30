package io.taig.linguist

import cats.effect.{IO, IOApp}

import java.nio.file.Paths

object App extends IOApp.Simple {
  override def run: IO[Unit] =
    GraalVmLinguist
      .default[IO]
      .use(_.detect(Paths.get("Main.scala"), "trait Foo {}"))
      .flatMap(IO.println)
}
