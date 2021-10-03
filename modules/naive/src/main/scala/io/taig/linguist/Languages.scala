package io.taig.linguist

object Languages {
  val C: Linguist.Language = Linguist.Language("C", List("c", "h"))
  val CSharp: Linguist.Language = Linguist.Language("C#", List("cs"))
  val Clojure: Linguist.Language = Linguist.Language("Clojure", List("clj"))
  val CoffeeScript: Linguist.Language = Linguist.Language("CoffeeScript", List("coffee"))
  val CommonLisp: Linguist.Language = Linguist.Language("Common Lisp", List("lisp"))
  val Coq: Linguist.Language = Linguist.Language("Coq", List("coq", "v"))
  val Cpp: Linguist.Language = Linguist.Language("C++", List("cpp", "c++", "h", "h++"))
  val DM: Linguist.Language = Linguist.Language("DM", List("dm"))
  val Dart: Linguist.Language = Linguist.Language("Dart", List("dart"))
  val Elixir: Linguist.Language = Linguist.Language("Elixir", List("ex"))
  val Elm: Linguist.Language = Linguist.Language("Elm", List("elm"))
  val EmacsLisp: Linguist.Language = Linguist.Language("Emacs Lisp", List("el", "emacs", "emacs.desktop"))
  val Erlang: Linguist.Language = Linguist.Language("Erlang", List("erl"))
  val FSharp: Linguist.Language = Linguist.Language("F#", List("fs"))
  val Fortran: Linguist.Language = Linguist.Language("Fortran", List("f", "f77", "for", "fpp"))
  val Go: Linguist.Language = Linguist.Language("Go", List("go"))
  val Groovy: Linguist.Language = Linguist.Language("Groovy", List("groovy"))
  val Haskell: Linguist.Language = Linguist.Language("Haskell", List("hs"))
  val Html: Linguist.Language = Linguist.Language("Html", List("html", "htm"))
  val Java: Linguist.Language = Linguist.Language("Java", List("java"))
  val JavaScript: Linguist.Language = Linguist.Language("JavaScript", List("js"))
  val Jsonnet: Linguist.Language = Linguist.Language("Jsonnet", List("jsonnet", "libsonnet"))
  val Julia: Linguist.Language = Linguist.Language("Julia", List("jl"))
  val Kotlin: Linguist.Language = Linguist.Language("Kotlin", List("kt"))
  val Lua: Linguist.Language = Linguist.Language("Lua", List("lua"))
  val MATLAB: Linguist.Language = Linguist.Language("MATLAB", List("matlab", "m"))
  val NASL: Linguist.Language = Linguist.Language("NASL", List("nasl"))
  val OCaml: Linguist.Language = Linguist.Language("OCaml", List("ml"))
  val ObjectiveC: Linguist.Language = Linguist.Language("Objective-C", List("m", "h"))
  val ObjectiveCpp: Linguist.Language = Linguist.Language("Objective-C++", List("mm"))
  val PHP: Linguist.Language = Linguist.Language("PHP", List("php", "php3", "php4", "php5"))
  val Perl: Linguist.Language = Linguist.Language("Perl", List("pl", "al", "cgi"))
  val PowerShell: Linguist.Language = Linguist.Language("PowerShell", List("ps1", "psd1", "psm1"))
  val Puppet: Linguist.Language = Linguist.Language("Puppet", List("pp"))
  val PureScript: Linguist.Language = Linguist.Language("PureScript", List("purs"))
  val Python: Linguist.Language = Linguist.Language("Python", List("py", "py3"))
  val R: Linguist.Language = Linguist.Language("R", List("r"))
  val Roff: Linguist.Language = Linguist.Language("Roff", List("roff"))
  val Ruby: Linguist.Language = Linguist.Language("Ruby", List("rb"))
  val Rust: Linguist.Language = Linguist.Language("Rust", List("rs"))
  val Scala: Linguist.Language = Linguist.Language("Scala", List("scala"))
  val Shell: Linguist.Language = Linguist.Language("Shell", List("sh", "bash", "zsh"))
  val Smalltalk: Linguist.Language = Linguist.Language("Smalltalk", List("st"))
  val Swift: Linguist.Language = Linguist.Language("Swift", List("swift"))
  val SystemVerilog: Linguist.Language = Linguist.Language("SystemVerilog", List("sv"))
  val TSQL: Linguist.Language = Linguist.Language("TSQL", List("sql"))
  val TypeScript: Linguist.Language = Linguist.Language("TypeScript", List("ts"))
  val Vala: Linguist.Language = Linguist.Language("Vala", List("vala", "vapi"))
  val VimScript: Linguist.Language = Linguist.Language("Vim script", List("vim"))
  val VisualBasicNet: Linguist.Language = Linguist.Language("Visual Basic .NET", List("vb"))
  val WebAssembly: Linguist.Language = Linguist.Language("WebAssembly", List("wast", "wat"))

  val All: List[Linguist.Language] = List(
    C,
    CSharp,
    Clojure,
    CoffeeScript,
    CommonLisp,
    Coq,
    Cpp,
    DM,
    Dart,
    Elixir,
    Elm,
    EmacsLisp,
    Erlang,
    FSharp,
    Fortran,
    Go,
    Groovy,
    Haskell,
    Html,
    Java,
    JavaScript,
    Jsonnet,
    Julia,
    Kotlin,
    Lua,
    MATLAB,
    NASL,
    OCaml,
    ObjectiveC,
    ObjectiveCpp,
    PHP,
    Perl,
    PowerShell,
    Puppet,
    PureScript,
    Python,
    R,
    Roff,
    Ruby,
    Rust,
    Scala,
    Shell,
    Smalltalk,
    Swift,
    SystemVerilog,
    TSQL,
    TypeScript,
    Vala,
    VimScript,
    VisualBasicNet,
    WebAssembly
  )
}
