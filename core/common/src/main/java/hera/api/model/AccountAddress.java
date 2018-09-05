/*
 * @copyright defined in LICENSE.txt
 */

package hera.api.model;

import java.util.Optional;

public class AccountAddress extends BytesValue {

  public static AccountAddress of(final byte[] bytes) {
    return new AccountAddress(bytes);
  }

  public AccountAddress(final byte[] value) {
    super(value);
  }

  @SuppressWarnings("unchecked")
  @Override
  public <T> Optional<T> adapt(Class<T> adaptor) {
    if (adaptor.isAssignableFrom(AccountAddress.class)) {
      return (Optional<T>) Optional.of(this);
    }
    return Optional.empty();
  }

}
