package io.leonis.algieba.calculus;

import io.leonis.algieba.spatial.ReferenceFrame;
import java.util.function.UnaryOperator;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.ops.transforms.Transforms;

/**
 * The Interface LocalLinearLineIntegral.
 *
 * This interface represents the functionality of an object which represents a continuous function
 * whose values are computed through a basis transformation and for which a line integral, with a
 * linear path, can be calculated by single variable parametrization.
 *
 * @author Rimon Oz
 */
public interface LocalLinearLineIntegral extends ReferenceFrame, ParametricLineIntegral {

  /**
   * {@inheritDoc}
   */
  @Override
  default double getLineIntegral(final INDArray lowerBound, final INDArray upperBound) {
    // translate to local space
    final INDArray newLowerBound = this.toLocalFrame(lowerBound);
    final INDArray newUpperBound = this.toLocalFrame(upperBound);

    return Math.sqrt(Transforms.pow(newUpperBound.sub(newLowerBound), 2).sumNumber().doubleValue())
        * this.getLineIntegral(newLowerBound, newUpperBound, 0d, 1d);
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
