package io.leonis.algieba.calculus;

import org.nd4j.linalg.api.ndarray.INDArray;

/**
 * The Interface LineIntegral.
 *
 * This interface represents the functionality of an object which represents a continuous function
 * for which a line integral between two points can be calculated.
 *
 * @author Rimon Oz
 */
public interface LineIntegral {
  /**
   * @param lowerBound The lower bound of the integral.
   * @param upperBound The upper bound of the integral.
   * @return The value of the line integral between the supplied lower and upper bounds (assumes
   *     the field to be conservative, which is implied by the existence of the force vector).
   */
  double getLineIntegral(final INDArray lowerBound, final INDArray upperBound);
}
