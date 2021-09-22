# Changelog

## 0.0.14

_2021-09-22_

 * Propagate context initialization errors
 * Change `default` initialization to `pooled(1)` to leverage asynchronous start
 * Improve truffle ruby init hack to run in the background
 * Migrate tests to munit fixtures
 * Upgrade to cats-effect 3.2.9

## 0.0.13

_2021-09-20_

 * Reuse context engine in pooled instances

## 0.0.10

_2021-09-13_

 * Add Linguist.languages
 * Add benchmarks module

## 0.0.9

_2021-09-12_

* Initialize pooled instance in background

## 0.0.8

_2021-09-12_

 * Load linguist gem as part of context initialization
 * Upgrade to cats-effect 3.2.8
 * Upgrade to munit 0.7.29

## 0.0.7

_2021-08-30_

 * Upgrade to cats-effect 3.2.5

## 0.0.6

_2021-08-29_

 * [#2] Pooled instance (#3)
 * Upgrade to cats-effect 3.2.3
 * Add Dockerfile

## 0.0.5

_2021-08-13_

 * Rename module `scala-linguist-graalvm` to `scala-linguist-graalvm-ruby`
 * Rename `GraalVmLinguist` to `GraalVmRubyLinguist`
 * Upgrade to cats-effect 3.2.2
 * Upgrade to sbt-scalajs 1.7.0
 * Upgrade to cats-effect 3.2.1

## 0.0.4

_2021-08-01_

 * Add `Linguist.detect(Path)`

## 0.0.3

_2021-07-30_

 * Fix `GraalVmLinguist.apply` return type

## 0.0.2

_2021-07-30_

 * Run `GraalVmLinguist.detect` on the blocking context

## 0.0.1

_2021-07-30_

 * Initial release