package automate.state;

import automate.transition.Transition;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Abstract state.
 * @author flying
 */
public abstract class AbstractState implements State {
    /**
     * create id for every state.
     */
    private static final AtomicInteger idCreator = new AtomicInteger(0);

    /** members **/
    protected int id;

    /**
     * All state connected with this and each is the next state of this.
     */
    protected HashMap<Transition, State> nexts = new HashMap<>();

    /**
     * All state connected with this and each is the prev state of this.
     */
    protected HashMap<Transition, State> prevs = new HashMap<>();

    /**
     * Constructor
     */
    public AbstractState() {
        this.id = idCreator.getAndIncrement();
    }

    @Override
    public int id() {
        return id;
    }

    @Override
    public void connect(Transition transition, State state) {
        nexts.put(transition, state);
        state.prevs().put(transition, this);
    }

    @Override
    public void disconnect(Transition transition, State state) {
        nexts.remove(transition, state);
        state.prevs().remove(transition, this);
    }

    @Override
    public Collection<State> transfer(short c) {
        return transfer(c, c);
    }

    @Override
    public Collection<State> transfer(short from, short to) {
        HashSet<State> result = new HashSet<>();

        for (Map.Entry<Transition, State> entry : nexts.entrySet()) {
            if (entry.getKey().match(from, to)) {
                result.add(entry.getValue());
            }
        }

        return result;
    }

    @Override
    public Map<Transition, State> nexts() {
        return nexts;
    }

    @Override
    public  Map<Transition, State>  prevs() {
        return prevs;
    }
}