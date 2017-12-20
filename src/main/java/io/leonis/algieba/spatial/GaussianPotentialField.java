package io.leonis.algieba.spatial;

import io.leonis.algieba.calculus.LocalLinearLineIntegral;
import io.leonis.algieba.geometry.*;
import io.leonis.algieba.statistic.distribution.GaussianDistribution;
import java.util.function.UnaryOperator;
import lombok.Value;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.ops.transforms.Transforms;

/**
 * The Class GaussianPotentialField.
 *
 * This class represents a {@link PotentialField} of an object which is modeled after a
 * two-dimensional normal distribution.
 *
 * @author Rimon Oz
 */
@Value
public class GaussianPotentialField implements PotentialField, LocalLinearLineIntegral, Rotation {
  /**
   * A vector pointing to the origin.
   */
  private final INDArray origin;
  /**
   * Height in potential.
   */
  private final double height;
  /**
   * Width of the field.
   */
  private final double width;
  /**
   * Length of the field.
   */
  private final double length;
  /**
   * Orientation of the field in radians.
   */
  private final double angle;

  /**
   * See <a href="http://www.wolframalpha.com/input/?i=integral+of+H+*+e%5E(-1*((A*t%2BB)%5E2%2B(C*t%2BD)%5E2))dt">
   * this equation</a>, where <code>x = A*t + B</code> and <code>y = C*t + D</code>.
   *
   * @param lowerBound The lower bound of the integral.
   * @param upperBound The upper bound of the integral.
   * @return A {@link UnaryOperator} representing the result of the indefinite integral for
   *     which the value can be computed.
   */
  @Override
  public UnaryOperator<Double> computeLineIntegral(
      final INDArray lowerBound,
      final INDArray upperBound
  ) {
    return input -> {
      // compute parametric coefficients
      final double A = upperBound.getDouble(0, 0) - lowerBound.getDouble(0, 0);
      final double B = lowerBound.getDouble(0, 0);
      final double C = upperBound.getDouble(1, 0) - lowerBound.getDouble(1, 0);
      final double D = lowerBound.getDouble(1, 0);

      // compute leading coefficients
      final double coeff = this.height * Math.sqrt(Math.PI)
          / (2 * Math.sqrt(Math.pow(A, 2) + Math.pow(C, 2)));

      // compute exponential expression
      final double eTerm = Math.exp(
          -1 * Math.pow(B * C - A * D, 2) / (Math.pow(A, 2) + Math.pow(C, 2)));

      // compute erf expression
      final double erf = GaussianDistribution.erf(
          Math.pow(A, 2) * input
              + Math.pow(C, 2) * input
              + A * B
              + C * D);

      // compute unscaled line integral
      return coeff * eTerm * erf;
    };
  }

  /**
   * See <a href="http://www.wolframalpha.com/input/?i=H+*+e%5E(-1*(x%5E2%2By%5E2))">this equation</a>.
   *
   * @param positionVector A position vector for which to calculate the potential.
   * @return The potential at the supplied position vector.
   */
  @Override
  public double getPotential(final INDArray positionVector) {
    return Transforms.exp(Transforms.pow(this.toLocalFrame(positionVector), 2).mul(-1d))
        .prodNumber().doubleValue()
        * this.height;
  }

  /**
   * See <a href="http://www.wolframalpha.com/input/?i=derivative+of+H+*+e%5E(-1*(x%5E2%2By%5E2))">
   * this equation</a>.
   *
   * @param positionVector A position vector for which to compute the force vector.
   * @return The force at the supplied position vector due to the potential field.
   */
  @Override
  public INDArray getForce(final INDArray positionVector) {
    return this.toGlobalFrame(this.toLocalFrame(positionVector)
        .mul(this.getPotential(positionVector))
        .mul(2d));
  }


  /**
   * {@inheritDoc}
   */
  @Override
  public INDArray toLocalFrame(final INDArray positionVector) {
    return this.planarCartesian(positionVector.sub(this.getOrigin()), this.getAngle())
        .div(Vectors.columnVector(this.getLength(), this.getWidth()));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public INDArray toGlobalFrame(final INDArray positionVector) {
    return this.planarCartesian(
        positionVector
            .mul(Vectors.columnVector(this.getLength(), this.getWidth())),
        -1 * this.getAngle());
  }
}
