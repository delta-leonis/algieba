package io.leonis.algieba.spatial;

import io.leonis.algieba.calculus.LineIntegral;
import org.nd4j.linalg.api.ndarray.INDArray;

/**
 * The Interface PotentialField.
 *
 * This interface describes the functionality of a differentiable conservative potential field over
 * {@link INDArray}, ie. a space which has a potential defined in every point, along with its
 * derivatives, and a line integral.
 *
 * @author Rimon Oz
 */
public interface PotentialField extends LineIntegral {

  /**
   * @param positionVector The position vector at which to compute the potential.
   * @return The potential in the point to which the position vector points.
   */
  double getPotential(final INDArray positionVector);

  /**
   * @param positionVector The position vector at which to compute the force.
   * @return The force vector due to the potential in the neighborhood of the point to which the
   * position vector points.
   */
  INDArray getForce(final INDArray positionVector);

  /**
   * @return The vector pointing to the origin of the potential field.
   */
  INDArray getOrigin();

  /**
   * Represents an object which can supply a {@link PotentialField}.
   *
   * @param <P> The type of {@link PotentialField} enclosed by the supplier.
   */
  interface Supplier<P extends PotentialField> {
    /**
     * @return The enclosed {@link PotentialField}
     */
    P getPotentialField();
  }
}
