/*
 * @copyright defined in LICENSE.txt
 */

package hera.build.web.model;

import static java.util.UUID.randomUUID;

import lombok.Getter;
import lombok.Setter;

public class BuildSummary {
  @Getter
  @Setter
  protected String uuid = randomUUID().toString();

  @Getter
  @Setter
  protected long timestamp = System.currentTimeMillis();

  @Getter
  @Setter
  protected boolean success;

  public BuildSummary() {
  }

  /**
   * Constructor with {@link BuildDetails}.
   *
   * @param buildDetails build result
   */
  public BuildSummary(final BuildDetails buildDetails) {
    this.uuid = buildDetails.getUuid();
    this.timestamp = buildDetails.getTimestamp();
    this.success = buildDetails.isSuccess();
  }
}
