package io.leonis.algieba.control;

import java.util.function.Predicate;

/**
 * The Interface AnomalyDetector.
 *
 * This interface describes the functionality of an anomaly detector, ie. a function which can
 * determine whether a specific input is considered to be an anomaly or not.
 *
 * @author Rimon Oz
 */
public interface AnomalyDetector<I> extends Predicate<I> {
}
