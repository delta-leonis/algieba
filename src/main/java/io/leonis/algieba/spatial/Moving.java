package io.leonis.algieba.spatial;

import org.nd4j.linalg.api.ndarray.INDArray;

/**
 * The Interface Moving.
 *
 * This class represents an object (in an n-dimensional space) which has a velocity vector.
 *
 * @author Rimon Oz
 */
public interface Moving {

  /**
   * @return The velocity vector of the moving object.
   */
  INDArray getVelocity();
}
