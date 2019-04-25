package automate;

import automate.state.State;

import java.util.Collection;

/**
 * Finte-Automate.
 *
 * @author flying
 */
public interface FinteAutomate {
    /**
     * If a text can match this NFA.
     * @param text the gavin text.
     * @return true means the text given by {@param text} can match this NFA.
     */
    boolean match(String text);

    /**
     * @return start state of automate
     */
    State start();

    /**
     * all accept states of this automates.
     * @return
     */
    Collection<State> accepts();
}