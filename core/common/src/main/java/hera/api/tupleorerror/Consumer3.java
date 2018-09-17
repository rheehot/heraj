/*
 * @copyright defined in LICENSE.txt
 */

package hera.api.tupleorerror;

public interface Consumer3<T1, T2, T3> {

  /**
   * Performs this operation on the given argument.
   *
   * @param t1 the 1st argument
   * @param t2 the 2nd argument
   * @param t3 the 3rd argument
   */
  void accept(T1 t1, T2 t2, T3 t3);
}
