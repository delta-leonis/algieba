package io.leonis.algieba.spatial;

import org.nd4j.linalg.api.ndarray.INDArray;

/**
 * The Interface PotentialField.
 *
 * @author Rimon Oz
 */
public interface PotentialField {

  /**
   * @param positionVector The position vector at which to compute the potential.
   * @return The potential in the point to which the position vector points.
   */
  INDArray getPotential(final INDArray positionVector);

  /**
   * @param positionVector The position vector at which to compute the force.
   * @return The force vector due to the potential in the neighborhood of the point to which the
   * position vector points.
   */
  INDArray getForce(final INDArray positionVector);

  /**
   * @param origin The origin from which to measure accumulated potential.
   * @param target The target to which to measure accumulated potential.
   * @return The accumulated potential from supplied origin to target (assumes the field to be
   * conservative, which is implied by the existence of the force vector).
   */
  INDArray getAccumulate(final INDArray origin, final INDArray target);

  /**
   * @return The vector pointing to the origin of the potential field.
   */
  INDArray getOrigin();
}
