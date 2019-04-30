package automate.DFA;

import automate.FinteAutomate;
import automate.NFA.NFA;
import automate.state.DFAState;
import automate.state.State;
import automate.state.SubSetDFAState;
import automate.transition.*;

import java.util.*;

/**
 * DFA
 * @author flying
 */
public class DFA implements FinteAutomate {
    /**
     * Start and accepts.
     */
    private DFAState start;
    private HashSet<DFAState> accepts = new HashSet<>();

    /**
     * Constructor
     * Convert NFA to DFA, subset-constructor.
     */
    public DFA(NFA nfa) {
        if (nfa == null) {
            throw new IllegalArgumentException("param NFA is null");
        }

        SubSetDFAState start = new SubSetDFAState(epsilonClosure(new ArrayList<State>(){{add(nfa.start());}}));
        this.start = start;

        for (State accept : nfa.accepts()) {
            if (start.containsNFAState(accept.id())) {
                this.accepts.add(start);
                break;
            }
        }

        HashMap<String, SubSetDFAState> DTran = new HashMap<String, SubSetDFAState>() {{put(start.subSetId(), start);}};
        Stack<SubSetDFAState> DTranUnMarked = new Stack<SubSetDFAState>() {{push(start);}};
        ArrayList<short[]> ranges = hashRanges(extractRanges(nfa.start()));

        while (!DTranUnMarked.isEmpty()) {
            SubSetDFAState subSet = DTranUnMarked.pop();

            for (short[] range : ranges) {
                Collection<State> states = epsilonClosure(move(range, subSet.subSet()));

                if (states.isEmpty()) {
                    continue;
                }

                SubSetDFAState nextState = new SubSetDFAState(states);
                SubSetDFAState exist = DTran.get(nextState.subSetId());

                if (exist == null) {
                    DTran.put(nextState.subSetId(), nextState);
                    DTranUnMarked.add(nextState);

                    for (State accept : nfa.accepts()) {
                        if (nextState.containsNFAState(accept.id())) {
                            this.accepts.add(nextState);
                            break;
                        }
                    }

                    exist = nextState;
                }

                subSet.connect(new RangeRuleTransition(new DefaultMatchRange(range[0], range[1])), exist);
            }
        }
    }

    @Override
    public boolean match(String text) {
        DFAState current = this.start;

        for (int i = 0; i < text.length(); i++) {
            current = current.transferTo((short) text.charAt(i));

            if (current == null) {
                return false;
            }
        }

        return accepts.contains(current);
    }

    @Override
    public State start() {
        return start;
    }

    @Override
    public Collection<State> accepts() {
        return (Collection) accepts;
    }

    /**
     * ε-closure(T)
     */
    private Collection<State> epsilonClosure(Collection<State> states) {
        HashSet<State> result = new HashSet() {{addAll(states);}};
        Stack<State> stack = new Stack() {{addAll(states);}};

        while (!stack.isEmpty())  {
            for (Map.Entry<Transition, State> entry : ((Map<Transition, State>) stack.pop().nexts()).entrySet()) {
                State next = entry.getValue();

                // todo, optimized
                if (!result.contains(next) && entry.getKey() instanceof EpsilonTranstion) {
                    result.add(next);
                    stack.add(next);
                }
            }
        }

        return result;
    }

    /**
     * move(States, c)
     */
    private HashSet<State> move(short[] range, Collection<State> states) {
        HashSet<State> nexts = new HashSet<>();

        for (State state : states) {
            nexts.addAll(state.transfer(range[0], range[1]));
        }

        return nexts;
    }

    /**
     * Extract all MatchRanges from NFA.
     * convert them into short arrays, each of them which 2 elements: [min, max], and sort them according min and max.
     * @param nfastate nfastate of a nfa
     * @return all ranges.
     */
    private LinkedList<short[]>  extractRanges(State nfastate) {
        Stack<State> stack = new Stack<State>() {{add(nfastate);}};
        HashSet<State> passed = new HashSet<>();
        LinkedList<short[]> result = new LinkedList<>();

        while (!stack.isEmpty()) {
            State<State> next = stack.pop();

            if (!passed.add(next)) {
                continue;
            }

            for (Map.Entry<Transition, State> entry : next.nexts().entrySet()) {
                for (MatchRange range : entry.getKey().matchRanges() ) {
                    result.add(new short[] {range.from(), range.to()});
                }

                stack.push(entry.getValue());
            }
        }

        return result;
    }

    /**
     * Split ranges into a set of non-intersecting segments.
     * @param ranges gavin ranges.
     * @return the range segments without intersecting.
     * @todo optimized....
     */
    private ArrayList<short[]> hashRanges(LinkedList<short[]> ranges) {
        ranges.sort(Comparator.comparingInt((s) -> s[0]));
        ArrayList<short[]> result = new ArrayList<>();

        while (!ranges.isEmpty()) {
            Iterator<short[]> ite = ranges.iterator();
            short[] firstItem = ite.next();
            short left = firstItem[0];
            short closest = firstItem[1];

            while (ite.hasNext()) {
                short[] next = ite.next();

                if (next[0] == left) {
                    closest = closest > next[1] ? next[1] : closest;
                    continue;
                }

                closest = next[0] > closest ? closest : (short) (next[0] - 1);
                break;
            }

            // add [left - closest] to final result.
            result.add(new short[] {left, closest});
            // shrink other ranges.
            ite = ranges.iterator();

            while (ite.hasNext()) {
                short[] next = ite.next();

                if (next[0] > closest) {
                    break;
                }

                if (next[0] == left && next[1] == closest) {
                    ite.remove();
                    continue;
                }

                next[0] = (short) (closest + 1);
            }
        }

        return result;
    }
}