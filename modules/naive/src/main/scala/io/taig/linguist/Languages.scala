package io.taig.linguist

object Languages {
  val JavaScript: Linguist.Language = Linguist.Language("JavaScript", List("js"))
  val Python: Linguist.Language = Linguist.Language("Python", List("py", "py3"))
  val Java: Linguist.Language = Linguist.Language("Java", List("java"))
  val Go: Linguist.Language = Linguist.Language("Go", List("go"))
  val Ruby: Linguist.Language = Linguist.Language("Ruby", List("rb"))
  val TypeScript: Linguist.Language = Linguist.Language("TypeScript", List("ts"))
  val Cpp: Linguist.Language = Linguist.Language("C++", List("cpp", "c++", "h", "h++"))
  val PHP: Linguist.Language = Linguist.Language("PHP", List("php", "php3", "php4", "php5"))
  val CSharp: Linguist.Language = Linguist.Language("C#", List("cs"))
  val C: Linguist.Language = Linguist.Language("C", List("c", "h"))
  val Scala: Linguist.Language = Linguist.Language("Scala", List("scala"))
  val Shell: Linguist.Language = Linguist.Language("Shell", List("sh", "bash", "zsh"))
  val Dart: Linguist.Language = Linguist.Language("Dart", List("dart"))
  val Rust: Linguist.Language = Linguist.Language("Rust", List("rs"))
  val Kotlin: Linguist.Language = Linguist.Language("Kotlin", List("kt"))
  val Swift: Linguist.Language = Linguist.Language("Swift", List("swift"))
  val PowerShell: Linguist.Language = Linguist.Language("PowerShell", List("ps1", "psd1", "psm1"))
  val Groovy: Linguist.Language = Linguist.Language("Groovy", List("groovy"))
  val Elixir: Linguist.Language = Linguist.Language("Elixir", List("ex"))
  val DM: Linguist.Language = Linguist.Language("DM", List("dm"))
  val ObjectiveC: Linguist.Language = Linguist.Language("Objective-C", List("m", "h"))
  val CoffeeScript: Linguist.Language = Linguist.Language("CoffeeScript", List("coffee"))
  val Perl: Linguist.Language = Linguist.Language("Perl", List("pl", "al", "cgi"))
  val Lua: Linguist.Language = Linguist.Language("Lua", List("lua"))
  val EmacsLisp: Linguist.Language = Linguist.Language("Emacs Lisp", List("el", "emacs", "emacs.desktop"))
  val OCaml: Linguist.Language = Linguist.Language("OCaml", List("ml"))
  val Haskell: Linguist.Language = Linguist.Language("Haskell", List("hs"))
  val Clojure: Linguist.Language = Linguist.Language("Clojure", List("clj"))
  val Erlang: Linguist.Language = Linguist.Language("Erlang", List("erl"))
  val TSQL: Linguist.Language = Linguist.Language("TSQL", List("sql"))
  val VimScript: Linguist.Language = Linguist.Language("Vim script", List("vim"))
  val R: Linguist.Language = Linguist.Language("R", List("r"))
  val Julia: Linguist.Language = Linguist.Language("Julia", List("jl"))
  val Coq: Linguist.Language = Linguist.Language("Coq", List("coq", "v"))
  val Roff: Linguist.Language = Linguist.Language("Roff", List("roff"))
  val Jsonnet: Linguist.Language = Linguist.Language("Jsonnet", List("jsonnet", "libsonnet"))
  val MATLAB: Linguist.Language = Linguist.Language("MATLAB", List("matlab", "m"))
  val Puppet: Linguist.Language = Linguist.Language("Puppet", List("pp"))
  val Fortran: Linguist.Language = Linguist.Language("Fortran", List("f", "f77", "for", "fpp"))
  val SystemVerilog: Linguist.Language = Linguist.Language("SystemVerilog", List("sv"))
  val VisualBasicNet: Linguist.Language = Linguist.Language("Visual Basic .NET", List("vb"))
  val FSharp: Linguist.Language = Linguist.Language("F#", List("fs"))
  val Elm: Linguist.Language = Linguist.Language("Elm", List("elm"))
  val Smalltalk: Linguist.Language = Linguist.Language("Smalltalk", List("st"))
  val WebAssembly: Linguist.Language = Linguist.Language("WebAssembly", List("wast", "wat"))
  val PureScript: Linguist.Language = Linguist.Language("PureScript", List("purs"))
  val ObjectiveCpp: Linguist.Language = Linguist.Language("Objective-C++", List("mm"))
  val Vala: Linguist.Language = Linguist.Language("Vala", List("vala", "vapi"))
  val CommonLisp: Linguist.Language = Linguist.Language("Common Lisp", List("lisp"))
  val NASL: Linguist.Language = Linguist.Language("NASL", List("nasl"))

  val All: List[Linguist.Language] = List(
    JavaScript,
    Python,
    Java,
    Go,
    Ruby,
    TypeScript,
    Cpp,
    PHP,
    CSharp,
    C,
    Scala,
    Shell,
    Dart,
    Rust,
    Kotlin,
    Swift,
    PowerShell,
    Groovy,
    Elixir,
    DM,
    ObjectiveC,
    CoffeeScript,
    Perl,
    Lua,
    EmacsLisp,
    OCaml,
    Haskell,
    Clojure,
    Erlang,
    TSQL,
    VimScript,
    R,
    Julia,
    Coq,
    Roff,
    Jsonnet,
    MATLAB,
    Puppet,
    Fortran,
    SystemVerilog,
    VisualBasicNet,
    FSharp,
    Elm,
    Smalltalk,
    WebAssembly,
    PureScript,
    ObjectiveCpp,
    Vala,
    CommonLisp,
    NASL
  )
}
