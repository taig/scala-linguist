package io.taig.linguist

import cats.{Id => Identity}

import java.nio.file.Path
import java.util.regex.Pattern
import scala.collection.immutable.HashMap

object NaiveLinguist extends Linguist[Identity] {
  override val languages: List[Linguist.Language] = Languages.All

  private[linguist] val extensions: HashMap[String, Set[String]] = {
    val builder = collection.mutable.HashMap.empty[String, Set[String]]

    languages.foreach { language =>
      language.extensions.foreach { extension =>
        builder.updateWith(extension) {
          case Some(languages) => Some(languages + language.name)
          case None            => Some(Set(language.name))
        }
      }
    }

    builder.to(HashMap)
  }

  private[linguist] val heuristics: HashMap[String, List[(String, Pattern)]] = {
    val cpp = List(
      "^\\s*#\\s*include <(cstdint|string|vector|map|list|array|bitset|queue|stack|forward_list|unordered_map|unordered_set|(i|o|io)stream)>",
      "^\\s*template\\s*<",
      "^[ \\t]*(try|constexpr)",
      "^[ \\t]*catch\\s*\\(",
      "^[ \\t]*(class|(using[ \\t]+)?namespace)\\s+\\w+",
      "^[ \\t]*(private|public|protected):$",
      "std::\\w+"
    ).mkString("|")

    val objectiveC = Pattern.compile(
      "^\\s*(@(interface|class|protocol|property|end|synchronised|selector|implementation)\\b|#import\\s+.+\\.h[\">])"
    )

    val wildcard = Pattern.compile(".*")

    HashMap(
      "h" -> List(
        Languages.ObjectiveC.name -> objectiveC,
        Languages.Cpp.name -> Pattern.compile(cpp),
        Languages.C.name -> wildcard
      ),
      "m" -> List(
        Languages.MATLAB.name -> Pattern.compile("^\\s*%"),
        Languages.ObjectiveC.name -> objectiveC
      )
    )
  }

  override def detect(path: Path, content: String): Option[String] = {
    val extension = this.extension(path)

    extensions.get(extension).flatMap { languages =>
      if (languages.isEmpty) None
      else if (languages.sizeIs == 1) Some(languages.head)
      else
        heuristics.get(extension) match {
          case Some(heuristics) =>
            heuristics.collectFirst {
              case (language, pattern) if pattern.matcher(content).find() => language
            }
          case None => None
        }
    }
  }

  override def detect(path: Path): List[String] = extensions.get(extension(path)).map(_.toList).getOrElse(Nil)

  private def extension(path: Path): String = {
    val value = path.toString

    val name = value.lastIndexOf('/') match {
      case -1    => value
      case index => value.substring(index + 1)
    }

    name.indexOf('.') match {
      case -1    => name
      case index => name.substring(index + 1)
    }
  }
}
