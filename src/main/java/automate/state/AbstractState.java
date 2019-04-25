package automate.state;

import automate.transition.Transition;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Abstract state.
 * @author flying
 */
public abstract class AbstractState implements State {
    /**
     * create id for every state.
     */
    private static final AtomicLong idCreator = new AtomicLong(0);

    /** members **/
    protected long id;

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
    public long id() {
        return id;
    }

    @Override
    public void connect(Transition transition, State state) {
        nexts.put(transition, state);

        if (state instanceof NFAState) {
            ((NFAState) state).prevs.put(transition, this);
        }
    }

    @Override
    public void disconnect(Transition transition, State state) {
        nexts.remove(transition, state);

        if (state instanceof NFAState) {
            ((NFAState) state).prevs.remove(transition, this);
        }
    }

    @Override
    public Collection<State> transfer(short c) {
        HashSet<State> result = new HashSet<>();

        for (Map.Entry<Transition, State> entry : nexts.entrySet()) {
            if (entry.getKey().match(c)) {
                result.add(entry.getValue());
            }
        }

        return result;
    }

    @Override
    public State next(Transition t) {
        return nexts.get(t);
    }

    @Override
    public Map<Transition, State> nexts() {
        return nexts;
    }

    @Override
    public  Map<Transition, State>  prevs() {
        return prevs;
    }

    @Override
    public void combine(State state) {
        Map<Transition, State> prevs = new HashMap<>(state.prevs());

        for (Map.Entry<Transition, State> entry : prevs.entrySet()) {
            State prev = entry.getValue();
            Transition transition = entry.getKey();
            prev.disconnect(transition, state);
            prev.connect(transition, this);
        }

        Map<Transition, State> nexts = state.nexts();

        for (Map.Entry<Transition, State> entry : nexts.entrySet()) {
            State next = entry.getValue();
            Transition transition = entry.getKey();
            connect(transition, next);
        }
    }
}