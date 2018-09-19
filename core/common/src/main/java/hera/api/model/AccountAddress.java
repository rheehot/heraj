/*
 * @copyright defined in LICENSE.txt
 */

package hera.api.model;

import hera.util.Base58Utils;
import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;

public class AccountAddress extends BytesValue {

  public static final byte VERSION = 0x42;

  /**
   * Create {@code AccountAddress} with a raw bytes array.
   *
   * @param bytes value
   * @return created {@link AccountAddress}
   */
  public static AccountAddress of(final byte[] bytes) {
    if (null == bytes) {
      return new AccountAddress(null);
    }
    return new AccountAddress(Arrays.copyOf(bytes, bytes.length));
  }

  /**
   * Create {@code AccountAddress} with a base58 with checksum encoded value.
   *
   * @param encoded base58 encoded value
   * @return created {@link AccountAddress}
   * @throws IOException when decoding error
   */
  public static AccountAddress of(final String encoded) throws IOException {
    if (null == encoded) {
      return new AccountAddress(null);
    }
    final byte[] withVersion = Base58Utils.decodeWithCheck(encoded);
    return of(Arrays.copyOfRange(withVersion, 1, withVersion.length));
  }

  public AccountAddress(final byte[] value) {
    super(value);
  }

  @Override
  public String getEncodedValue() throws IOException {
    final byte[] withVersion = new byte[value.length + 1];
    withVersion[0] = VERSION;
    System.arraycopy(value, 0, withVersion, 1, value.length);
    return Base58Utils.encodeWithCheck(withVersion);
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
