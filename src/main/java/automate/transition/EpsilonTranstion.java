package automate.transition;

import java.util.Collection;
import java.util.Collections;

/**
 * ε-transition， can be passed by any character.
 *
 * @author flying
 */
public class EpsilonTranstion implements Transition {
    /**
     * Singleton instance.
     */
    @Override
    public boolean match(short c) {
        return false;
    }

    @Override
    public boolean match(short from, short to) {
        return false;
    }

    @Override
    public Collection<MatchRange> matchRanges() {
        return Collections.EMPTY_SET;
    }

    @Override
    public EpsilonTranstion clone() {
        return new EpsilonTranstion();
    }

    @Override
    public String toString() {
        return "ε";
    }
}