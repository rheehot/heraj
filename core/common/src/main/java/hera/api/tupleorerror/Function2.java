/*
 * @copyright defined in LICENSE.txt
 */

package hera.api.tupleorerror;

public interface Function2<T0, T1, R> {

  /**
   * Applies this function to the given arguments.
   *
   * @param t0 the 1st argument
   * @param t1 the 2nd argument
   * @return the function result
   */
  R apply(T0 t0, T1 t1);
}
