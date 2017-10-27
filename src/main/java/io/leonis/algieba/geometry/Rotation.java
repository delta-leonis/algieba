package io.leonis.algieba.geometry;

import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;

/**
 * The Class Rotation.
 *
 * @author Rimon Oz
 */
public interface Rotation {

  /**
   * @param input    The input vector as a column vector (2 by 1).
   * @param rotation The rotation (in radians).
   * @return The vector rotated by the specified rotation.
   */
  default INDArray planarCartesian(final INDArray input, final double rotation) {
    return Nd4j.create(
        new double[]{
            StrictMath.cos(rotation), -1d * StrictMath.sin(rotation),
            StrictMath.sin(rotation), StrictMath.cos(rotation)
        },
        new int[]{2, 2}).mmul(input);
  }
}
