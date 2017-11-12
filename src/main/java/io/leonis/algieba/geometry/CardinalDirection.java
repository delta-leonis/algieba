package io.leonis.algieba.geometry;

/**
 * The Enum CardinalDirection.
 *
 * This enumeration enumerates the principal cardinal directions.
 *
 * @author Rimon Oz
 * @author Jeroen de Jong
 */
public enum CardinalDirection {
  /**
   * North cardinal direction.
   */
  NORTH,

  /**
   * North-east cardinal direction.
   */
  NORTH_EAST,

  /**
   * East cardinal direction.
   */
  EAST,

  /**
   * South-east cardinal direction.
   */
  SOUTH_EAST,

  /**
   * South cardinal direction.
   */
  SOUTH,

  /**
   * South-west cardinal direction.
   */
  SOUTH_WEST,

  /**
   * West cardinal direction.
   */
  WEST,

  /**
   * North-west cardinal direction.
   */
  NORTH_WEST;

  /**
   * @param orientation Orientation in radians where north is 0.
   * @return The cardinal direction based on the provided orientation in radians.
   */
  public static CardinalDirection from(double orientation) {
    return values()[(int)((orientation % 2d * Math.PI) / (Math.PI / 4d))];
  }
}
