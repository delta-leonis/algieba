package io.leonis.algieba.spatial;

import org.nd4j.linalg.api.ndarray.INDArray;

/**
 * The Interface ReferenceFrame.
 *
 * This interface represents the functionality of an object which has a reference frame, i.e.
 * an object for which basis transformations exist between a local and a global reference frame
 * such that these transformations are inverses of each other.
 *
 * @author Rimon Oz
 */
public interface ReferenceFrame {
  /**
   * @param positionVector The position vector which to change basis for.
   * @return The supplied (global) position vector expressed in terms of the basis formed by the
   * potential field.
   */
  INDArray toLocalFrame(final INDArray positionVector);

  /**
   * @param positionVector The position vector which to change basis for.
   * @return The supplied (local) position vector expressed in terms of the standard basis.
   */
  INDArray toGlobalFrame(final INDArray positionVector);
}
