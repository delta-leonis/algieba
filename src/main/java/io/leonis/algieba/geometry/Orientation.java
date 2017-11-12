package io.leonis.algieba.geometry;

/**
 * The Interface Orientation.
 *
 * This interface describes the functionality of an object which has orientation (with respect to a
 * specific reference angle).
 *
 * @author Rimon Oz
 */
public interface Orientation {

  /**
   * @return The orientation of the object.
   */
  double getOrientation();

  /**
   * @return The {@link CardinalDirection} based on {@link #getOrientation()}.
   */
  default CardinalDirection getCardinalDirection() {
    return CardinalDirection.from(this.getOrientation());
  }
}
