package automate.ui;

import automate.state.State;
import automate.transition.Transition;
import guru.nidi.graphviz.attribute.*;
import guru.nidi.graphviz.engine.Format;
import guru.nidi.graphviz.engine.Graphviz;
import guru.nidi.graphviz.model.Graph;
import guru.nidi.graphviz.model.Node;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static guru.nidi.graphviz.attribute.Label.Justification.LEFT;
import static guru.nidi.graphviz.model.Factory.*;

public class DrawAutoMate {
    public static void draw(State start) throws IOException {
        Node node = convertToNode(start, new HashMap<>());
        Graph g = graph("nfa").directed().with(node);
        Graphviz.fromGraph(g).render(Format.PNG).toFile(new File("example/nfa.png"));
    }

    private static Node convertToNode(State start, HashMap<State, Node> passed) {
        if (passed.containsKey(start)) {
            return passed.get(start);
        }

        Map<Transition, State> nexts =  start.nexts();
        Node node = node(start.id() + "");
        passed.put(start, node);

        for (Map.Entry<Transition, State> entry : nexts.entrySet()) {
            node = node.link(to(convertToNode(entry.getValue(), passed)).with(Label.of(entry.getKey().toString())));
        }

        return node;
    }

    public static void main(String[] args) throws IOException {
        Node
            main = node("main").with(Label.html("<b>main</b><br/>start"), Color.rgb("1020d0").font()),
            init = node(Label.markdown("**_init_**")),
            execute = node("execute"),
            compare = node("compare").with(Shape.RECTANGLE, Style.FILLED, Color.hsv(.7, .3, 1.0)),
            mkString = node("mkString").with(Label.lines(LEFT, "make", "a", "multi-line")),
            printf = node("printf");

        Graph g = graph("example2").directed().with(
            main.link(
                to(node("parse").link(execute)).with(LinkAttr.weight(8)),
                to(init).with(Style.DOTTED),
                node("cleanup"),
                to(printf).with(Label.of("100 times"))));

        Graphviz.fromGraph(g).width(900).render(Format.PNG).toFile(new File("example/ex.png"));
    }
}