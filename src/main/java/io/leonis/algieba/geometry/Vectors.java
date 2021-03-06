package io.leonis.algieba.geometry;

import lombok.experimental.UtilityClass;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;

/**
 * The Class Vectors.
 *
 * This class contains utility functions for instantiating or operating on {@link INDArray}.
 *
 * @author Rimon Oz
 */
@UtilityClass
public class Vectors {

  /**
   * @param values The values to be put in the row vector.
   * @return A row vector containing the supplied values.
   */
  public static INDArray rowVector(double... values) {
    return Nd4j.create(values);
  }

  /**
   * @param values The values to be put in the column vector.
   * @return A column vector containing the supplied values.
   */
  public static INDArray columnVector(double... values) {
    return Nd4j.create(values, new int[]{values.length, 1});
  }

  /**
   * @param input    The input vector as a column vector (2 by 1).
   * @param rotation The rotation (in radians).
   * @return The vector rotated by the specified rotation.
   */
  public static INDArray rotatePlanarCartesian(final INDArray input, final double rotation) {
    return Nd4j.create(
        new double[]{
            StrictMath.cos(rotation), -1d * StrictMath.sin(rotation),
            StrictMath.sin(rotation), StrictMath.cos(rotation)
        },
        new int[]{2, 2}).mmul(input);
  }
}
