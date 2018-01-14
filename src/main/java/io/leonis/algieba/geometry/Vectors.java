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
}
