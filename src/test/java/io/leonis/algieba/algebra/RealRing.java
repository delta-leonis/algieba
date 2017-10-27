package io.leonis.algieba.algebra;

/**
 * The Class RealRing.
 *
 * @author Rimon Oz
 */
public class RealRing implements Ring<Double> {

  @Override
  public Double add(final Double firstElement, final Double secondElement) {
    return firstElement + secondElement;
  }

  @Override
  public Double getAdditiveInverse(final Double fieldElement) {
    return (-1d) * fieldElement;
  }

  @Override
  public Double multiply(final Double firstElement, final Double secondElement) {
    return firstElement * secondElement;
  }

  @Override
  public Double getMultiplicativeInverse(final Double fieldElement) throws ArithmeticException {
    if (fieldElement == 0) {
      throw new ArithmeticException("Division by zero!");
    }
    return 1d / fieldElement;
  }
}
