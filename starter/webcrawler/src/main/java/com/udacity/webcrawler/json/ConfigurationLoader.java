package com.udacity.webcrawler.json;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * A static utility class that loads a JSON configuration file.
 */
public final class ConfigurationLoader {

  private final Path path;

  /**
   * Create a {@link ConfigurationLoader} that loads configuration from the given {@link Path}.
   */
  public ConfigurationLoader(Path path) {
    this.path = Objects.requireNonNull(path);
  }

  /**
   * Loads configuration from this {@link ConfigurationLoader}'s path
   *
   * @return the loaded {@link CrawlerConfiguration}.
   */
  public CrawlerConfiguration load() {
    // Using try-with-resources which implements the autocloseable feature
    try (Reader reader = Files.newBufferedReader(path)) {
      return read(reader);
    } catch (IOException ex) {
      ex.printStackTrace();
      return null;
    }


    // TODO: Fill in this method.


  }

  /**
   * Loads crawler configuration from the given reader.
   *
   * @param reader a Reader pointing to a JSON string that contains crawler configuration.
   * @return a crawler configuration
   */
  public static CrawlerConfiguration read(Reader reader) {
    // This is here to get rid of the unused variable warning.
    Objects.requireNonNull(reader);
    ObjectMapper objectMapper = new ObjectMapper();
    CrawlerConfiguration crawlerConfiguration;
    objectMapper.disable(JsonParser.Feature.AUTO_CLOSE_SOURCE);
    try {
      crawlerConfiguration =objectMapper.readValue(reader,CrawlerConfiguration.Builder.class).build();
      return crawlerConfiguration;
    } catch (IOException e) {
      e.printStackTrace();
      return null;
    }

  }
}
