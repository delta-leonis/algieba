package io.leonis.algieba.geometry;

import lombok.experimental.UtilityClass;

/**
 * The Class Trigonometry.
 *
 * This class contains utility functions for trigonometric operations which are missing from
 * {@link java.lang.Math}
 *
 * @author Rimon Oz
 */
@UtilityClass
public class Trigonometry {

  /**
   * @param value The value to calculate the inverse hyperbolic sine of.
   * @return The inverse hyperbolic sine of the supplied value.
   */
  public static double asinh(final double value) {
    return Math.log(value + Math.sqrt(value * value + 1d));
  }

  /**
   * @param value The value to calculate the inverse hyperbolic cosine of.
   * @return The inverse hyperbolic cosine of the supplied value.
   */
  public static double acosh(final double value) {
    return Math.log(value + Math.sqrt(value * value - 1d));
  }

  /**
   * @param value The value to calculate the inverse hyperbolic tangent of.
   * @return The inverse hyperbolic tangent of the supplied value.
   */
  public static double atanh(final double value) {
    return 0.5d * Math.log((value + 1d) / (value - 1d));
  }
}
