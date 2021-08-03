# Scala Linguist

[![CI & CD](https://github.com/taig/scala-linguist/actions/workflows/main.yml/badge.svg)](https://github.com/taig/scala-linguist/actions/workflows/main.yml)
[![scala-linguist-graalvm Scala version support](https://index.scala-lang.org/taig/scala-linguist/scala-linguist-graalvm/latest-by-scala-version.svg)](https://index.scala-lang.org/taig/scala-linguist/scala-linguist-graalvm)

> A Scala wrapper around [github/linguist](https://github.com/github/linguist) based on GraalVM's [TruffleRuby](https://www.graalvm.org/reference-manual/ruby/)

## Prerequisites

GraalVM with support for TruffleRuby must be used as the Java runtime  

```
gu install ruby
```

The github/linguist Ruby gem must be installed ([instructions](https://github.com/github/linguist#installation))  

```
gem install github-linguist
```

## Installation

**sbt**

```scala
libraryDependencies ++=
  "io.taig" %% "scala-linguist-core" % "x.y.z" :: 
  "io.taig" %% "scala-linguist-graalvm" % "x.y.z" ::
  Nil
```

## Usage

- Detect the language when given a path and source code

```scala
import cats.effect.{IO, IOApp}
import io.taig.linguist.GraalVmLinguist

import java.nio.file.Paths

object App extends IOApp.Simple {
  override def run: IO[Unit] =
    GraalVmLinguist.default[IO]
      .use(_.detect(Paths.get("Main.scala"), "trait Foo {}"))
      .flatMap(IO.println)
}
```

```
sbt> run 
Some(Scala)
```

- Detect the potential languages when only given a path

```scala
import cats.effect.{IO, IOApp}
import io.taig.linguist.GraalVmLinguist

import java.nio.file.Paths

object App extends IOApp.Simple {
  override def run: IO[Unit] =
    GraalVmLinguist.default[IO]
      .use(_.detect(Paths.get("Main.scala")))
      .flatMap(IO.println)
}
```

```
sbt> run 
List(Scala)
```