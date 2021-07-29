package io.taig.linguist;

import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.PolyglotAccess;
import org.graalvm.polyglot.Source;
import org.graalvm.polyglot.Value;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;

public class TruffleLinguist implements Linguist, AutoCloseable  {
  private final Context context;

  public TruffleLinguist() {
    this.context = Context.newBuilder("ruby").allowNativeAccess(true).allowIO(true).allowPolyglotAccess(PolyglotAccess.ALL).build();
  }

  @Override
  public Optional<String> detect(Path path, String content) {
    final Value bindings = context.getPolyglotBindings();
    bindings.putMember("path", path.toString());
    bindings.putMember("content", content);

    final Source source = Source.create("ruby",
        "require 'linguist'\n" +
          "blob = Linguist::Blob.new(Polyglot.import('path'), Polyglot.import('content'))\n" +
          "Linguist.detect(blob)&.name");

    final Value result = context.eval(source);

    if (result.isNull()) return Optional.empty();
    else if(result.isString()) return Optional.of(result.asString());
    else throw new IllegalStateException("Unexpected return type");
  }

  @Override
  public void close() throws Exception {
    context.close();
  }
}
