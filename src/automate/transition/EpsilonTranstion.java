package automate.transition;

/**
 * ε-transition， can be passed by any character.
 *
 * @author flying
 */
public class EpsilonTranstion implements Transition<EpsilonTranstion> {
    /**
     * Singleton instance.
     */
    @Override
    public boolean match(short c) {
        return true;
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