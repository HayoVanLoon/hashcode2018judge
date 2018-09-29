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

import static io.atlassian.fugue.Option.option;


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

    final Option<JsonNode> emailFilter = option(yaml.get(
        "email-filter"));

    allowByHost = emailFilter
        .flatMap(x -> option(x.get("by-host")))
        .map(JsonNode::asText).filter(x -> !x.isEmpty());

    allowByPattern = emailFilter
        .flatMap(x -> option(x.get("by-regex")))
        .map(JsonNode::asText).filter(x -> !x.isEmpty())
        .map(Pattern::compile);

    allowExact = emailFilter
        .flatMap(x -> option(x.get("exact")))
        .fold(ImmutableSet::of, addresses -> {
          final ImmutableSet.Builder<String> builder = ImmutableSet.builder();
          for (JsonNode address : addresses) {
            final String s = address.asText();
            if (!s.isEmpty()) {
              builder.add(s);
            }
          }
          return builder.build();
        });

    scoreSecret = option(yaml.get("score-secret")).map(JsonNode::asText);

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
