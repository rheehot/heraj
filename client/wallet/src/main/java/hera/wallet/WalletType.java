/*
 * @copyright defined in LICENSE.txt
 */

package hera.wallet;

import hera.annotation.ApiAudience;
import hera.annotation.ApiStability;

@ApiAudience.Public
@ApiStability.Unstable
public enum WalletType {
  Naive,
  Secure,
  ServerKeyStore
}
