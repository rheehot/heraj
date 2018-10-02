/*
 * @copyright defined in LICENSE.txt
 */

package hera.util.conf;

import static java.util.Collections.unmodifiableMap;

import hera.util.Configuration;
import java.util.HashMap;
import java.util.Map;

public class InMemoryConfiguration extends AbstractConfiguration {
  protected Map<String, Configuration> subconfigurations = new HashMap<>();

  protected Map<String, Object> key2value = new HashMap<>();

  public InMemoryConfiguration() {
    this(false);
  }

  public InMemoryConfiguration(final boolean readOnly) {
    super(readOnly);
  }

  @Override
  public Map<String, Object> asMap() {
    return unmodifiableMap(key2value);
  }

  @Override
  public void define(final String key, final Object value) {
    logger.debug("{}: {}", key, value);

    final int dotIndex = key.indexOf('.');
    if (dotIndex < 0) {
      key2value.put(key, value);
    } else {
      final String subconfigurationName = key.substring(0, dotIndex);
      final String remainder = key.substring(dotIndex + 1);
      final Configuration subconfiguration = subconfigurations.get(subconfigurationName);
      if (null == subconfiguration) {
        final Configuration newSubconfiguration = new InMemoryConfiguration();
        newSubconfiguration.define(remainder, value);
        subconfigurations.put(subconfigurationName, newSubconfiguration);
      } else {
        subconfiguration.define(remainder, value);
      }
    }
  }

  @Override
  public Configuration getSubconfiguration(final String key) {
    logger.trace("Key: {}", key);
    final Configuration subconfiguration = subconfigurations.get(key);
    if (null == subconfiguration) {
      logger.info("The subconfiguration for {} is null", key);
      logger.debug("Subconfigurations: {}", subconfigurations);
    }
    return subconfiguration;
  }

  @Override
  protected Object getValue(final String key) {
    logger.trace("Key: {}", key);
    return key2value.get(key);
  }
  
  @Override
  public String toString() {
    Map<String, Object> merged = new HashMap<>();
    merged.putAll(subconfigurations);
    merged.putAll(key2value);
    return merged.toString();
  }
}