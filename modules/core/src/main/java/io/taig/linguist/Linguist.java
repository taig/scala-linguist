package io.taig.linguist;

import java.nio.file.Path;
import java.util.Optional;

public interface Linguist {
  Optional<String> detect(Path path, String content);
}
