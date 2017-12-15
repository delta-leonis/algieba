package io.leonis.algieba.spatial;

import io.leonis.algieba.calculus.LocalLinearLineIntegral;
import io.leonis.algieba.geometry.Rotation;
import java.util.function.UnaryOperator;
import lombok.Value;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;
import org.nd4j.linalg.ops.transforms.Transforms;

/**
 * The Class HydrodynamicPotentialField.
 *
 * This class represents a hydrodynamic {@link PotentialField} as proposed in section 2.1 of
 * <a href="https://www.ri.cmu.edu/pub_files/pub3/kim_jin_oh_1992_2/kim_jin_oh_1992_2.pdf">this paper</a>.
 *
 * @author Rimon Oz
 */
@Value
public class HydrodynamicPotentialField
    implements LocalLinearLineIntegral, Rotation, PotentialField {
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
   * See <a href="http://www.wolframalpha.com/input/?i=ln(x%5E2+%2B+y%5E2)">this equation.</a>
   *
   * @param positionVector The position vector at which to compute the potential.
   */
  @Override
  public double getPotential(final INDArray positionVector) {
    return this.height
        * Math.log(Transforms.pow(this.toLocalFrame(positionVector), 2).sumNumber().doubleValue())
        / (2 * Math.PI);
  }

  /**
   * See <a href="http://www.wolframalpha.com/input/?i=derivate+of+ln(x%5E2+%2B+y%5E2)">this equation.</a>
   *
   * @param positionVector The position vector at which to compute the force.
   */
  @Override
  public INDArray getForce(final INDArray positionVector) {
    final INDArray localPositionVector = this.toLocalFrame(positionVector);

    return this.toGlobalFrame(localPositionVector.mul(2d)
        .div(Transforms.pow(localPositionVector, 2).sumNumber()));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public INDArray toLocalFrame(final INDArray positionVector) {
    return this.planarCartesian(positionVector.sub(this.getOrigin()), this.getAngle())
        .div(Nd4j.create(new double[]{this.getLength(), this.getWidth()}, new int[]{2, 1}));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public INDArray toGlobalFrame(final INDArray positionVector) {
    return this.planarCartesian(
        positionVector
            .mul(Nd4j.create(new double[]{this.getLength(), this.getWidth()}, new int[]{2, 1})),
        -1 * this.getAngle());
  }

  /**
   * See <a href="http://www.wolframalpha.com/input/?i=integral+of+ln((A*t%2BB)%5E2+%2B+(C*t%2BD)%5E2)">
   * this equation.</a>
   *
   * @param lowerBound The starting point of the line along which to calculate the unscaled value
   *                   of the parametrized line integral.
   * @param upperBound The final point of the line along which to calculate the unscaled value
   *                   of the parametrized line integral.
   * @return A {@link UnaryOperator} representing the indefinite integral of the parametrized line
   *     integral.
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

      // compute denominator
      final double denominator = Math.pow(A, 2) + Math.pow(C, 2);

      // compute numerator terms
      final double logarithmicTerm =
          (Math.pow(A, 2) * input + A * B + C * (C * input + D))
              * Math.log(Math.pow(input, 2) * (Math.pow(A, 2) + Math.pow(C, 2))
              + 2 * A * B * input
              + Math.pow(B, 2)
              + 2 * C * D * input
              + Math.pow(D, 2));
      final double inverseTangentTerm =
          2 * (A * D - B * C)
              * Math.atan((B * C - A * D) / (Math.pow(A, 2) * input + A * B + C * (C * input + D)));
      final double linearTerm = -2 * input * (Math.pow(A, 2) + Math.pow(C, 2));

      return (logarithmicTerm + inverseTangentTerm + linearTerm) / denominator;
    };
  }
}
