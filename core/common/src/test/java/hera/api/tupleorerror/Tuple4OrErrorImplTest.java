/*
 * @copyright defined in LICENSE.txt
 */

package hera.api.tupleorerror;

import static hera.api.tupleorerror.FunctionChain.fail;
import static hera.api.tupleorerror.FunctionChain.seq;
import static hera.api.tupleorerror.FunctionChain.success;
import static java.util.UUID.randomUUID;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;

@SuppressWarnings("unchecked")
public class Tuple4OrErrorImplTest {

  @Test
  public void testGet() {
    final String result1 = randomUUID().toString();
    final String result2 = randomUUID().toString();
    final String result3 = randomUUID().toString();
    final String result4 = randomUUID().toString();
    final Tuple4OrError<String, String, String, String> tuple4OrError = seq(() -> success(result1),
        () -> success(result2), () -> success(result3), () -> success(result4));
    assertEquals(result1, tuple4OrError.get1());
    assertEquals(result2, tuple4OrError.get2());
    assertEquals(result3, tuple4OrError.get3());
    assertEquals(result4, tuple4OrError.get4());
    assertFalse(tuple4OrError.hasError());
    assertNull(tuple4OrError.getError());
  }

  @Test(expected = Exception.class)
  public void shouldThrowExceptionOn1stFail() {
    final String result = randomUUID().toString();
    final Tuple4OrError<String, String, String, String> tuple4OrError =
        seq(() -> fail(new UnsupportedOperationException()), () -> success(result),
            () -> success(result), () -> success(result));
    assertNotNull(tuple4OrError.getError());
    tuple4OrError.get1();
  }

  @Test(expected = Exception.class)
  public void shouldThrowExceptionOn2ndFail() {
    final String result = randomUUID().toString();
    final Tuple4OrError<String, String, String, String> tuple4OrError =
        seq(() -> success(result), () -> fail(new UnsupportedOperationException()),
            () -> success(result), () -> success(result));
    assertNotNull(tuple4OrError.getError());
    tuple4OrError.get2();
  }

  @Test(expected = Exception.class)
  public void shouldThrowExceptionOn3rdFail() {
    final String result = randomUUID().toString();
    final Tuple4OrError<String, String, String, String> tuple4OrError =
        seq(() -> success(result), () -> success(result),
            () -> fail(new UnsupportedOperationException()), () -> success(result));
    assertNotNull(tuple4OrError.getError());
    tuple4OrError.get3();
  }

  @Test(expected = Exception.class)
  public void shouldThrowExceptionOn4thFail() {
    final String result = randomUUID().toString();
    final Tuple4OrError<String, String, String, String> tuple4OrError =
        seq(() -> success(result), () -> success(result), () -> success(result),
            () -> fail(new UnsupportedOperationException()));
    assertNotNull(tuple4OrError.getError());
    tuple4OrError.get4();
  }

  @Test
  public void testIfPresentWithTuples() {
    final String expected1 = randomUUID().toString();
    final String expected2 = randomUUID().toString();
    final String expected3 = randomUUID().toString();
    final String expected4 = randomUUID().toString();
    final Tuple4OrError<String, String, String, String> tuple4OrError =
        seq(() -> success(expected1), () -> success(expected2), () -> success(expected3),
            () -> success(expected4));
    tuple4OrError.ifPresent((actual1, actual2, actual3, actual4) -> {
      assertEquals(expected1, actual1);
      assertEquals(expected2, actual2);
      assertEquals(expected3, actual3);
      assertEquals(expected4, actual4);
    });
  }

  @Test
  public void testIfPresentWithError() {
    final String result = randomUUID().toString();
    final Tuple4OrError<String, String, String, String> tuple4OrError =
        seq(() -> fail(new UnsupportedOperationException()), () -> success(result),
            () -> success(result), () -> success(result));
    assertNotNull(tuple4OrError.getError());
    tuple4OrError.ifPresent((a, b, c, d) -> fail("Should not called"));
  }

  @Test
  public void testFilterGeneratingTuple() {
    final String result1 = randomUUID().toString();
    final String result2 = randomUUID().toString();
    final String result3 = randomUUID().toString();
    final String result4 = randomUUID().toString();
    final Tuple4OrError<String, String, String, String> expected = seq(() -> success(result1),
        () -> success(result2), () -> success(result3), () -> success(result4));
    final Tuple4OrError<String, String, String, String> actual = expected.filter((a, b, c,
        d) -> a.equals(result1) && b.equals(result2) && c.equals(result3) && d.equals(result4));
    assertEquals(expected, actual);
  }

  @Test
  public void testFilterGeneratingNoTuple() {
    final String result1 = randomUUID().toString();
    final String result2 = randomUUID().toString();
    final String result3 = randomUUID().toString();
    final String result4 = randomUUID().toString();
    final Tuple4OrError<String, String, String, String> tuple4OrError = seq(() -> success(result1),
        () -> success(result2), () -> success(result3), () -> success(result4));
    final Tuple4OrError<String, String, String, String> actual = tuple4OrError.filter((a, b, c,
        d) -> !a.equals(result1) || !b.equals(result2) || !c.equals(result3) || !d.equals(result4));
    assertTrue(actual.hasError());
  }

  @Test
  public void testFilterWithError() {
    final String result = randomUUID().toString();
    final Tuple4OrError<String, String, String, String> expected =
        seq(() -> success(result), () -> success(result), () -> success(result),
            () -> fail(new UnsupportedOperationException()));
    final Tuple4OrError<String, String, String, String> actual =
        expected.filter((a, b, c, d) -> a.isEmpty() && b.isEmpty() && c.isEmpty() && d.isEmpty());
    assertEquals(expected, actual);
  }

  @Test
  public void testFilterWithErrorOnNext() {
    final String result1 = randomUUID().toString();
    final String result2 = randomUUID().toString();
    final String result3 = randomUUID().toString();
    final String result4 = randomUUID().toString();
    final Tuple4OrError<String, String, String, String> base = seq(() -> success(result1),
        () -> success(result2), () -> success(result3), () -> success(result4));
    final Tuple4OrError<String, String, String, String> actual = base.filter((a, b, c, d) -> {
      throw new UnsupportedOperationException();
    });
    assertTrue(actual.hasError());
  }

  @Test
  public void testMap() {
    final String result1 = randomUUID().toString();
    final String result2 = randomUUID().toString();
    final String result3 = randomUUID().toString();
    final String result4 = randomUUID().toString();
    final Tuple4OrError<String, String, String, String> tuple4OrError = seq(() -> success(result1),
        () -> success(result2), () -> success(result3), () -> success(result4));
    final int expected = result1.length() + result2.length() + result3.length() + result4.length();
    final int actual = tuple4OrError
        .map((a, b, c, d) -> a.length() + b.length() + c.length() + d.length()).getResult();
    assertEquals(expected, actual);
  }

  @Test(expected = Exception.class)
  public void testMapWithError() {
    final String result = randomUUID().toString();
    final Tuple4OrError<String, String, String, String> tuple4OrError =
        seq(() -> success(result), () -> success(result), () -> success(result),
            () -> fail(new UnsupportedOperationException()));
    tuple4OrError.map((a, b, c, d) -> a.length() + b.length() + c.length() + d.length())
        .getResult();
  }

  @Test(expected = Exception.class)
  public void testMapWithErrorOnNext() {
    final String result1 = randomUUID().toString();
    final String result2 = randomUUID().toString();
    final String result3 = randomUUID().toString();
    final String result4 = randomUUID().toString();
    final Tuple4OrError<String, String, String, String> tuple4OrError = seq(() -> success(result1),
        () -> success(result2), () -> success(result3), () -> success(result4));
    tuple4OrError.map((a, b, c, d) -> {
      throw new UnsupportedOperationException();
    }).getResult();
  }

  @Test
  public void testEquals() {
    final String result1 = randomUUID().toString();
    final String result2 = randomUUID().toString();
    final String result3 = randomUUID().toString();
    final String result4 = randomUUID().toString();
    final Tuple4OrError<String, String, String, String> left = seq(() -> success(result1),
        () -> success(result2), () -> success(result3), () -> success(result4));
    final Tuple4OrError<String, String, String, String> right = seq(() -> success(result1),
        () -> success(result2), () -> success(result3), () -> success(result4));
    assertEquals(left, right);
  }

  @Test
  public void testHashCode() {
    final String result1 = randomUUID().toString();
    final String result2 = randomUUID().toString();
    final String result3 = randomUUID().toString();
    final String result4 = randomUUID().toString();
    final Tuple4OrError<String, String, String, String> tuple4OrError = seq(() -> success(result1),
        () -> success(result2), () -> success(result3), () -> success(result4));
    assertTrue(0 != tuple4OrError.hashCode());
  }

}