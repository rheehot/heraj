/*
 * @copyright defined in LICENSE.txt
 */

package hera.api.tupleorerror;

public interface Consumer4<T0, T1, T2, T3> {

  /**
   * Performs this operation on the given argument.
   *
   * @param t0 the 1st argument
   * @param t1 the 2nd argument
   * @param t2 the 3rd argument
   * @param t3 the 4th argument
   */
  void accept(T0 t0, T1 t1, T2 t2, T3 t3);
}
