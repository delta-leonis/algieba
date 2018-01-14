package io.leonis.algieba.spatial;

import io.leonis.algieba.geometry.Vectors;
import java.util.function.UnaryOperator;
import lombok.Value;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;
import org.nd4j.linalg.ops.transforms.Transforms;

/**
 * The Class BoundaryPotentialField.
 *
 * This class represents a {@link PotentialField} which bounds a rectangle such that the potential
 * approximates zero for any point on the field and approximates infinity for any
 * point outside the field.
 *
 * @author Rimon Oz
 */
@Value
public class BoundaryPotentialField implements PotentialField {
  private final INDArray origin = Nd4j.zeros(2, 1);
  /**
   * The width of the field in mm.
   */
  private final double width;
  /**
   * The length of the field in mm.
   */
  private final double length;
  /**
   * The displacement of the potential field from the edge.
   */
  private final double fieldDisplacement;

  /**
   * {@inheritDoc}
   */
  @Override
  public double getPotential(final INDArray positionVector) {
    return this.getPotential(positionVector.getDouble(0, 0))
        + this.getPotential(this.getWidth() - positionVector.getDouble(0, 0))
        + this.getPotential(positionVector.getDouble(1, 0))
        + this.getPotential(this.getLength() - positionVector.getDouble(1, 0));
  }

  /**
   * See <a href="http://www.wolframalpha.com/input/?i=1%2F(1+%2B+e%5E(D+-+d))">this equation.</a>
   *
   * @param distanceToBoundary The distance to the boundary in mm.
   * @return The potential due to a single boundary at the specified distance from that boundary.
   */
  public double getPotential(final double distanceToBoundary) {
    return 1 / (1 + Math.exp(this.getFieldDisplacement() - distanceToBoundary));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public INDArray getForce(final INDArray positionVector) {
    return Vectors.columnVector(
        -1 * this.getForceMagnitude(positionVector.getDouble(0, 0)),
        0)
        .add(Vectors.columnVector(
            this.getForceMagnitude(this.getWidth() - positionVector.getDouble(0, 0)),
            0))
        .add(Vectors.columnVector(
            0,
            -1 * this.getForceMagnitude(positionVector.getDouble(1, 0))))
        .add(Vectors.columnVector(
            0,
            this.getForceMagnitude(this.getLength() - positionVector.getDouble(1, 0))));
  }

  /**
   * See <a href="http://www.wolframalpha.com/input/?i=derivative+of+1%2F(1+%2B+e%5E(D+-+d))">this
   * equation</a>.
   *
   * @param distanceToBoundary The distance from the boundary for which the compute the magnitude of
   *                           the force vector.
   * @return The magnitude of the force vector due to a single boundary.
   */
  private double getForceMagnitude(final double distanceToBoundary) {
    return Math.exp(distanceToBoundary + this.getFieldDisplacement())
        / Math.pow(Math.exp(distanceToBoundary) + Math.exp(this.getFieldDisplacement()), 2);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public double getLineIntegral(final INDArray lowerBound, final INDArray upperBound) {
    return Math.sqrt(Transforms.pow(upperBound.sub(lowerBound), 2).sumNumber().doubleValue())
        * (this.computeNorthernLineIntegralSegment(lowerBound, upperBound).apply(1d)
        + this.computeSouthernLineIntegralSegment(lowerBound, upperBound).apply(1d)
        + this.computeEasternLineIntegralSegment(lowerBound, upperBound).apply(1d)
        + this.computeWesternLineIntegralSegment(lowerBound, upperBound).apply(1d)
        - this.computeNorthernLineIntegralSegment(lowerBound, upperBound).apply(0d)
        - this.computeSouthernLineIntegralSegment(lowerBound, upperBound).apply(0d)
        - this.computeEasternLineIntegralSegment(lowerBound, upperBound).apply(0d)
        - this.computeWesternLineIntegralSegment(lowerBound, upperBound).apply(0d));
  }

  /**
   * @param origin The starting point of the line integral.
   * @param target The target point of the line integral.
   * @return The total potential between the supplied origin and target due to the northern boundary
   * segment.
   */
  private UnaryOperator<Double> computeNorthernLineIntegralSegment(
      final INDArray origin,
      final INDArray target
  ) {
    return this.computeLineIntegral(origin.getDouble(0, 0), target.getDouble(0, 0));
  }

  /**
   * @param origin The starting point of the line integral.
   * @param target The target point of the line integral.
   * @return The total potential between the supplied origin and target due to the southern boundary
   * segment.
   */
  private UnaryOperator<Double> computeSouthernLineIntegralSegment(
      final INDArray origin,
      final INDArray target
  ) {
    return this.computeLineIntegral(this.getWidth() - origin.getDouble(0, 0),
        this.getWidth() - target.getDouble(0, 0));
  }

  /**
   * @param origin The starting point of the line integral.
   * @param target The target point of the line integral.
   * @return The total potential between the supplied origin and target due to the eastern boundary
   * segment.
   */
  private UnaryOperator<Double> computeEasternLineIntegralSegment(
      final INDArray origin,
      final INDArray target
  ) {
    return this.computeLineIntegral(origin.getDouble(1, 0), target.getDouble(1, 0));
  }

  /**
   * @param origin The starting point of the line integral.
   * @param target The target point of the line integral.
   * @return The total potential between the supplied origin and target due to the western boundary
   * segment.
   */
  private UnaryOperator<Double> computeWesternLineIntegralSegment(
      final INDArray origin,
      final INDArray target
  ) {
    return this.computeLineIntegral(this.getLength() - origin.getDouble(1, 0),
        this.getLength() - target.getDouble(1, 0));
  }

  /**
   * See <a href="http://www.wolframalpha.com/input/?i=integral+of+1%2F(1+%2B+e%5E(D+-+(A*t+%2B+B)))dt">
   * this equation</a>.
   *
   * @param origin The starting distance from the boundary.
   * @param target The target distance from the boundary.
   * @return The total potential between the supplied origin and target distances from a boundary.
   */
  private UnaryOperator<Double> computeLineIntegral(final double origin, final double target) {
    if (origin == target) {
      return input -> 0d;
    }
    return input -> Math.log(
        Math.exp((target - origin) * input + origin) + Math.exp(this.getFieldDisplacement()))
        / (target - origin);
  }
}
