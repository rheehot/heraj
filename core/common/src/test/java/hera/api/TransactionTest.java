/*
 * @copyright defined in LICENSE.txt
 */

package hera.api;

import static java.util.UUID.randomUUID;
import static org.junit.Assert.assertEquals;
import static org.slf4j.LoggerFactory.getLogger;

import hera.api.encode.Base58;
import hera.api.encode.Base58WithCheckSum;
import hera.api.model.AccountAddress;
import hera.api.model.BytesValue;
import hera.api.model.Signature;
import hera.api.model.Transaction;
import hera.api.model.TransactionType;
import hera.util.Base58Utils;
import java.io.IOException;
import java.util.Arrays;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;

public class TransactionTest {

  protected final transient Logger logger = getLogger(getClass());

  protected static final Base58 base58Encoded =
      () -> "AtmxbVvjDN5LYwaf5QrCZPc3FoAqUCMVegVXjf8CMCz59wL21X6j";

  protected static final Base58WithCheckSum base58WithCheckSum =
      () -> "AtmxbVvjDN5LYwaf5QrCZPc3FoAqUCMVegVXjf8CMCz59wL21X6j";

  protected static final String HASH_WITHOUT_SIGN = "CNmX88skCK1hPdmmqV6KKWYUCPwahTVfMmkmh477cNYt";

  protected static final String HASH_WITH_SIGN = "4qcmY2sVSshi7awqxNUzo2MQihcJntk9gZX9e2Q6FkPU";


  protected Transaction transaction = null;

  @Before
  public void setup() throws IOException {
    transaction = new Transaction();
    transaction.setNonce(1L);
    transaction.setSender(AccountAddress.of(base58WithCheckSum));
    transaction.setRecipient(AccountAddress.of(base58WithCheckSum));
    transaction.setAmount(1L);
    transaction.setPayload(base58Encoded.decode());
    transaction.setLimit(1L);
    transaction.setPrice(1L);
    transaction.setTxType(TransactionType.NORMAL);
  }

  @Test
  public void testEquals() {
    final byte[] byteValue1 = randomUUID().toString().getBytes();
    final byte[] byteValue2 = Arrays.copyOf(byteValue1, byteValue1.length);

    final BytesValue value1 = new BytesValue(byteValue1);
    final BytesValue value2 = new BytesValue(byteValue2);

    assertEquals(value1, value2);
  }

  @Test
  public void testCalculateHashWithoutSign() throws Exception {
    final String expected = HASH_WITHOUT_SIGN;
    final String actual =
        Base58Utils.encode(transaction.calculateHash().getBytesValue().getValue());
    assertEquals(expected, actual);
  }

  @Test
  public void testCalculateHashWithign() throws Exception {
    transaction.setSignature(Signature.of(base58Encoded.decode(), null));
    final String expected = HASH_WITH_SIGN;
    final String actual =
        Base58Utils.encode(transaction.calculateHash().getBytesValue().getValue());
    assertEquals(expected, actual);
  }

}