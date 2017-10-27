package io.leonis.algieba.algebra;

import static org.testng.Assert.assertTrue;

import org.testng.annotations.Test;

/**
 * The Test RealRingUnitTest.
 *
 * @author Rimon Oz
 */
public class RealRingUnitTest {

  private final RealRing realRing = new RealRing();
  private final Double firstValue = 3d;
  private final Double secondValue = -2d;
  private final Double thirdValue = 8d;

  /**
   * Add.
   *
   * @throws Exception the exception
   */
  @Test
  public void add() throws Exception {
    //  *   - Closure:        if a and b are elements of F, then a + b is an element of F
    // proven by type system
    //  *   - Associativity:  if a, b, and c are elements of F, then (a+b)+c = a+(b+c)
    assertTrue(
        this.realRing.add(this.realRing.add(this.firstValue, this.secondValue), this.thirdValue)
            .equals(this.realRing
                .add(this.firstValue, this.realRing.add(this.secondValue, this.thirdValue))));
    //  *                     if a, b, and c are elements of F, then (a*b)*c = a*(b*c)
    assertTrue(this.realRing
        .multiply(this.realRing.multiply(this.firstValue, this.secondValue), this.thirdValue)
        .equals(this.realRing.multiply(this.firstValue,
            this.realRing.multiply(this.secondValue, this.thirdValue))));
    //  *   - Identity:       there exists an element e of F such that for all a in F: e+a = a+e = a+0
    assertTrue(
        this.realRing.add(0d, this.firstValue).equals(this.realRing.add(this.firstValue, 0d)));
    //  *   - Inverse:        for every element, a, in F there exists another element, b, such that a*b=1
    // assertTrue(this.realRing.multiply(this.firstValue, this.firstValue))
    //  *   - Distributivity: if a, b, and c are elements of F, then a*(b+c) = a*b + a*c
  }

  /**
   * Gets additive inverse.
   *
   * @throws Exception the exception
   */
  @Test
  public void getAdditiveInverse() throws Exception {
    assertTrue(this.realRing.getAdditiveInverse(this.firstValue).equals(-1 * this.firstValue));
    assertTrue(this.realRing.getAdditiveInverse(this.secondValue).equals(-1 * this.secondValue));
    assertTrue(this.realRing.getAdditiveInverse(this.thirdValue).equals(-1 * this.thirdValue));
    assertTrue(this.realRing.getAdditiveInverse(this.thirdValue).equals(-1 * this.thirdValue));
  }

  /**
   * Multiply.
   *
   * @throws Exception the exception
   */
  @Test
  public void multiply() throws Exception {
    assertTrue(this.realRing.multiply(this.firstValue, this.secondValue)
        .equals(this.firstValue * this.secondValue));
    assertTrue(this.realRing.multiply(this.secondValue, this.thirdValue)
        .equals(this.secondValue * this.thirdValue));
  }

  /**
   * Gets multiplicative inverse.
   *
   * @throws Exception the exception
   */
  @Test
  public void getMultiplicativeInverse() throws Exception {
    assertTrue(this.realRing.getMultiplicativeInverse(this.firstValue).equals(1d / this.firstValue));
    assertTrue(this.realRing.getMultiplicativeInverse(this.secondValue).equals(1d / this.secondValue));
    assertTrue(this.realRing.getMultiplicativeInverse(this.thirdValue).equals(1d / this.thirdValue));
    assertTrue(this.realRing.getMultiplicativeInverse(this.thirdValue).equals(1d / this.thirdValue));
  }
  /**
   * Gets multiplicative inverse.
   *
   * @throws Exception the exception
   */
  @Test(expectedExceptions = ArithmeticException.class)
  public void divisionByZero() throws Exception {
    this.realRing.getMultiplicativeInverse(0d);
  }
}
