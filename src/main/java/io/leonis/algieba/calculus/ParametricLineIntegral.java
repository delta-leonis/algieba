package io.leonis.algieba.calculus;

import java.util.function.UnaryOperator;
import org.nd4j.linalg.api.ndarray.INDArray;

/**
 * /**
 * The Interface ParametricLineIntegral.
 *
 * This interface represents the functionality of an object which represents a continuous function
 * for which a line integral, with a linear path, can be calculated by single variable parametrization.
 *
 * @author Rimon Oz
 */
public interface ParametricLineIntegral extends LineIntegral {
  /**
   * @param lowerBound          The lower bound of the line integral.
   * @param upperBound          The upper bound of the line integral.
   * @param parameterLowerBound The lower bound of the parametrized integral.
   * @param parameterUpperBound The upper bound of the parametrized integral.
   * @return The value of the integral along the line between the supplied lower and upper bounds
   * by parametrization through a single variable.
   */
  default double getLineIntegral(final INDArray lowerBound, final INDArray upperBound,
      final double parameterLowerBound, final double parameterUpperBound) {
    // compute indefinite integral
    final UnaryOperator<Double> integral = this.computeLineIntegral(lowerBound, upperBound);

    // compute line integral
    return integral.apply(parameterUpperBound) - integral.apply(parameterLowerBound);
  }

  /**
   * Computes the indefinite integral along the line between the supplied lower and upper bounds
   * parametrized by a single variable.
   *
   * @param lowerBound The lower bound of the integral.
   * @param upperBound The upper bound of the integral.
   * @return A {@link UnaryOperator} representing the result of an indefinite integral for which the
   * value can be computed.
   */
  UnaryOperator<Double> computeLineIntegral(final INDArray lowerBound, final INDArray upperBound);
}
