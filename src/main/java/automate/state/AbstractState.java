package automate.state;

import automate.transition.Transition;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Abstract state.
 * @author flying
 */
public abstract class AbstractState<T extends State> implements State<T> {
    /**
     * create id for every state.
     */
    private static final AtomicInteger idCreator = new AtomicInteger(0);

    /** members **/
    protected int id;

    /**
     * All state connected with this and each is the next state of this.
     */
    protected HashMap<Transition, T> nexts = new HashMap<>();

    /**
     * All state connected with this and each is the prev state of this.
     */
    protected HashMap<Transition, T> prevs = new HashMap<>();

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
    public void connect(Transition transition, T state) {
        nexts.put(transition, state);
        state.prevs().put(transition, this);
    }

    @Override
    public void disconnect(Transition transition, State state) {
        nexts.remove(transition, state);
        state.prevs().remove(transition, this);
    }

    @Override
    public Collection<T> transfer(short c) {
        return transfer(c, c);
    }

    @Override
    public Collection<T> transfer(short from, short to) {
        HashSet<T> result = new HashSet<>();

        for (Map.Entry<Transition, T> entry : nexts.entrySet()) {
            if (entry.getKey().match(from, to)) {
                result.add(entry.getValue());
            }
        }

        return result;
    }

    @Override
    public Map<Transition, T> nexts() {
        return nexts;
    }

    @Override
    public  Map<Transition, T>  prevs() {
        return prevs;
    }
}