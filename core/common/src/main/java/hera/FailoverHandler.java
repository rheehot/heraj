/*
 * @copyright defined in LICENSE.txt
 */

package hera;

import hera.annotation.ApiAudience;
import hera.annotation.ApiStability;

@ApiAudience.Private
@ApiStability.Unstable
public interface FailoverHandler {

  <T> Response<T> handle(Invocation<T> invocation, Response<T> response);

}
