package io.leonis.algieba;

/**
 * The Interface Temporal.
 *
 * This interface describes the functionality of an object which can be timestamped.
 *
 * @author Rimon Oz
 */
public interface Temporal {

  /**
   * @return The timestamp at which the object representation was constructed.
   */
  long getTimestamp();
}
