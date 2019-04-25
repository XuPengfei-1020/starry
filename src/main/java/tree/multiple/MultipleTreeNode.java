package tree.multiple;

import java.util.LinkedHashSet;

public class MultipleTreeNode {
    public LinkedHashSet<MultipleTreeNode> children;
    public int value;

    public MultipleTreeNode(int value) {
        this.value = value;
    }

    public void addChildren(MultipleTreeNode node) {
        if (children == null)
            children = new LinkedHashSet<>();
        children.add(node);
    }

    public String toString() {
        return String.valueOf(value);
    }
}