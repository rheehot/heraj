/*
 * @copyright defined in LICENSE.txt
 */

package hera.key;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import hera.AbstractTestCase;
import hera.api.model.Signature;
import hera.util.HexUtils;
import hera.util.pki.ECDSASignature;
import java.math.BigInteger;
import java.util.Arrays;
import org.junit.Test;

public class SignatureSpecTest extends AbstractTestCase {

  @Test
  public void testSerialize() throws Exception {
    final ECDSASignature ecdsaSignature = ECDSASignature.of(
        new BigInteger(
            "77742016982977049819968937189730099006007209897399569418319639670259283246582"),
        new BigInteger(
            "24080111729304174841921585755879357193051484773881703660717104599905026449822"));
    final Signature signature = SignatureSpec.serialize(ecdsaSignature);
    assertTrue(Arrays.equals(HexUtils.decode(
        "3045022100ABE06C1B99DE0C51B4790D24EE52674F532D9057744ED9EEF3F61425F9D1BDF60220353CDC395B12ABB6E297085B4D6F1A9DF7783DB66F95A7E0CE28246FC538219E"),
        signature.getSign().getValue()));
  }

  @Test
  public void testSerializeAndParse() throws Exception {
    final ECDSASignature expected = ECDSASignature.of(
        new BigInteger(
            "77742016982977049819968937189730099006007209897399569418319639670259283246582"),
        new BigInteger(
            "24080111729304174841921585755879357193051484773881703660717104599905026449822"));
    final Signature signature = SignatureSpec.serialize(expected);
    final ECDSASignature actual = SignatureSpec.deserialize(signature);
    assertEquals(expected, actual);
  }


}
