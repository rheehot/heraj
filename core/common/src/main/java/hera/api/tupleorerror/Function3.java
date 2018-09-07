/*
 * @copyright defined in LICENSE.txt
 */

package hera.api.tupleorerror;

public interface Function3<T0, T1, T2, R> {

  /**
   * Applies this function to the given arguments.
   *
   * @param t0 the 1st argument
   * @param t1 the 2nd argument
   * @param t2 the 3rd argument
   * @return the function result
   */
  R apply(T0 t0, T1 t1, T2 t2);
}
