package io.leonis.algieba.tree;

import com.google.common.collect.Table;
import java.util.function.*;

/**
 * The Interface FiniteStateMachine.
 *
 * This interface describes the functionality of a finite state machine.
 *
 * @param <S> The type of {@link State}
 * @author Rimon Oz
 */
public interface FiniteStateMachine<S extends FiniteStateMachine.State> extends Graph<S> {

  /**
   * Transitions from one state to the next, if the state transition is supported by this finite
   * state machine.
   *
   * @param from The state to transition from.
   * @param to   The state to transition to.
   * @return True if successful, false otherwise.
   */
  default boolean transition(final S from, final S to) {
    return this.getStateTransitionTable().contains(from, to)
        && this.getStateTransitionTable().get(from, to).test(from, to);
  }

  /**
   * @return The state transition table.
   */
  Table<S, S, BiPredicate<S, S>> getStateTransitionTable();

  /**
   * The Interface State.
   *
   * This interface describes the functionality of a state in the state machine.
   */
  interface State {
    String getName();
  }
}
