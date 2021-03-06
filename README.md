# Scala Linguist

[![CI & CD](https://github.com/taig/scala-linguist/actions/workflows/main.yml/badge.svg)](https://github.com/taig/scala-linguist/actions/workflows/main.yml)
[![scala-linguist-core Scala version support](https://index.scala-lang.org/taig/scala-linguist/scala-linguist-core/latest-by-scala-version.svg)](https://index.scala-lang.org/taig/scala-linguist/scala-linguist-core)
[![Scala Steward badge](https://img.shields.io/badge/Scala_Steward-helping-blue.svg?style=flat&logo=data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAA4AAAAQCAMAAAARSr4IAAAAVFBMVEUAAACHjojlOy5NWlrKzcYRKjGFjIbp293YycuLa3pYY2LSqql4f3pCUFTgSjNodYRmcXUsPD/NTTbjRS+2jomhgnzNc223cGvZS0HaSD0XLjbaSjElhIr+AAAAAXRSTlMAQObYZgAAAHlJREFUCNdNyosOwyAIhWHAQS1Vt7a77/3fcxxdmv0xwmckutAR1nkm4ggbyEcg/wWmlGLDAA3oL50xi6fk5ffZ3E2E3QfZDCcCN2YtbEWZt+Drc6u6rlqv7Uk0LdKqqr5rk2UCRXOk0vmQKGfc94nOJyQjouF9H/wCc9gECEYfONoAAAAASUVORK5CYII=)](https://scala-steward.org)

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
  "io.taig" %%% "scala-linguist-core" % "x.y.z" :: 
  "io.taig" %%% "scala-linguist-naive" % "x.y.z" ::
  "io.taig" %% "scala-linguist-graalvm-ruby" % "x.y.z" ::
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
    GraalVmRubyLinguist.default[IO]
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
    GraalVmRubyLinguist.default[IO]
      .use(_.detect(Paths.get("Main.scala")))
      .flatMap(IO.println)
}
```

```
sbt> run 
List(Scala)
```

## Benchmarks

```shell
> benchmarks/Jmh/run -wi 10 -i 5 -f1 -t4
```
