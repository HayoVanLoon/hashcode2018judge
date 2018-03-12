package nl.hayovanloon.hashcode2018.models;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.google.common.collect.ImmutableSet;
import io.atlassian.fugue.Option;

import java.io.IOException;
import java.io.InputStream;
import java.util.Set;
import java.util.regex.Pattern;

public class Settings {

  private final Option<String> allowByHost;
  private final Option<Pattern> allowByPattern;
  private final Set<String> allowExact;
  private final Option<String> scoreSecret;

  public Settings(Option<String> allowByHost,
                  Option<Pattern> allowByPattern,
                  Set<String> allowExact,
                  Option<String> scoreSecret) {
    this.allowByHost = allowByHost;
    this.allowByPattern = allowByPattern;
    this.allowExact = allowExact;
    this.scoreSecret = scoreSecret;
  }

  public static Settings read(Object caller) throws IOException {
    final Option<String> allowByHost;
    final Option<Pattern> allowByPattern;
    final Set<String> allowExact;
    final Option<String> scoreSecret;

    final JsonNode yaml;
    final InputStream is = caller.getClass().getClassLoader()
        .getResourceAsStream("settings.yaml");
    if (is != null) {
      final ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
      yaml = mapper.readTree(is);
    } else {
      yaml = JsonNodeFactory.instance.objectNode();
    }

    allowByHost = Option.option(yaml.get("email-filter"))
        .map(x -> x.get("by-host"))
        .map(JsonNode::asText);

    allowByPattern = Option.option(yaml.get("email-filter"))
        .map(x -> x.get("by-regex"))
        .map(JsonNode::asText)
        .map(Pattern::compile);

    allowExact = Option.option(yaml.get("email-filter"))
        .map(x -> x.get("exact"))
        .fold(ImmutableSet::of, adresses -> {
          final ImmutableSet.Builder<String> builder = ImmutableSet.builder();
          for (JsonNode address : adresses) {
            builder.add(address.asText());
          }
          return builder.build();
        });

    scoreSecret = Option.option(yaml.get("score-secret")).map(JsonNode::asText);

    return new Settings(allowByHost, allowByPattern, allowExact, scoreSecret);
  }

  public Option<String> getAllowByHost() {
    return allowByHost;
  }

  public Option<Pattern> getAllowByPattern() {
    return allowByPattern;
  }

  public Set<String> getAllowExact() {
    return allowExact;
  }

  public Option<String> getScoreSecret() {
    return scoreSecret;
  }
}
