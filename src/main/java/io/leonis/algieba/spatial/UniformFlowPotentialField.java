package io.leonis.algieba.spatial;

import io.leonis.algieba.calculus.ParametricLineIntegral;
import java.util.function.UnaryOperator;
import lombok.Value;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;

/**
 * The Class UniformFlowPotentialField.
 *
 * This class represents a uniform flow {@link PotentialField} as proposed in section 2.1 of
 * <a href="https://www.ri.cmu.edu/pub_files/pub3/kim_jin_oh_1992_2/kim_jin_oh_1992_2.pdf">this paper</a>.
 *
 * @author Rimon Oz
 */
@Value
public class UniformFlowPotentialField implements ParametricLineIntegral, PotentialField {
  /**
   * The angle at which the flow inside the {@link PotentialField} flows.
   */
  private final double angle;
  /**
   * The slope of the {@link PotentialField}.
   */
  private final double strength;
  /**
   * The flow multiplier.
   */
  private final INDArray multiplier;
  private final INDArray origin;

  /**
   * Constructs a uniform flow {@link PotentialField}.
   * @param origin   The origin of the {@link PotentialField}.
   * @param angle    The angle at which the uniform flow flows.
   * @param strength The slope of the {@link PotentialField}
   */
  public UniformFlowPotentialField(final INDArray origin, final double angle,
      final double strength) {
    this.origin = origin;
    this.angle = angle;
    this.strength = strength;
    this.multiplier = Nd4j
        .create(new double[]{Math.cos(this.angle), Math.sin(this.angle)}, new int[]{2, 1})
        .mul(-1 * this.strength);
  }

  /**
   * See <a href="http://www.wolframalpha.com/input/?i=-1*(x*Cos%5BA%5D+%2B+y*Sin%5BA%5D)">this equation.</a>
   *
   * @param positionVector The position vector at which to compute the potential.
   */
  @Override
  public double getPotential(final INDArray positionVector) {
    return positionVector
        .mul(this.multiplier)
        .sumNumber().doubleValue();
  }

  /**
   * See <a href="http://www.wolframalpha.com/input/?i=derivative+of+-1*(x*Cos%5BA%5D+%2B+y*Sin%5BA%5D)">this equation.</a>
   *
   * @param positionVector The position vector at which to compute the potential.
   */
  @Override
  public INDArray getForce(final INDArray positionVector) {
    return this.multiplier.mul(-1d);
  }


  /**
   * See <a href="http://www.wolframalpha.com/input/?i=integral+of+-S*((a*t%2Bb)*Cos%5BA%5D+%2B+(c*t%2Bd)*Sin%5BA%5D)+wrt+t">this equation.</a>
   * @param lowerBound The lower bound of the integral.
   * @param upperBound The upper bound of the integral.
   * @return A {@link UnaryOperator} representing the indefinite integral of the parametrized line
   * integral.
   */
  @Override
  public UnaryOperator<Double> computeLineIntegral(final INDArray lowerBound, final INDArray upperBound) {
    return input ->
      upperBound.sub(lowerBound)
          .mul(Math.pow(input, 2) / 2d)
          .add(lowerBound.mul(input))
          .mul(this.multiplier)
          .sumNumber().doubleValue();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public double getLineIntegral(final INDArray lowerBound, final INDArray upperBound) {
    return this.getLineIntegral(lowerBound, upperBound, 0d, 1d);
  }
}
