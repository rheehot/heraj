/*
 * @copyright defined in LICENSE.txt
 */

package hera.api.model;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class Authentication {

  public static Authentication of(final AccountAddress address, final String password) {
    return new Authentication(address, password);
  }

  @Getter
  @Setter
  protected AccountAddress address;

  @Getter
  @Setter
  protected String password;
}