package tree.multiple;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Stack;

/**
 * 遍历多叉树，自顶向下/自底向上，深度/层次遍历，递归/非递归。
 *
 * @author flying
 */
public class Traversal {
    private MultipleTreeNode node;

    public Traversal(MultipleTreeNode treeNode) {
        this.node = treeNode;
    }

    /**
     * 深度，递归
     * @param bottomUp，是否是从低向上遍历
     */
    public void traversal(boolean bottomUp) {
        this.traversal0(this.node, bottomUp);
    }

    private void traversal0(MultipleTreeNode node, boolean bottomUp) {
        if (node == null) {
            return;
        }

        if (!bottomUp) {
            System.out.print(node.value + ",");
        }

        HashSet<MultipleTreeNode> nodes = node.children;

        if (nodes != null) {
            for (MultipleTreeNode child : nodes) {
                traversal0(child, bottomUp);
            }
        }

        if (bottomUp) {
            System.out.print(node.value + ",");
        }
    }

    /**
     * 深度遍历，非递归
     */
    public void useLastNode() {
        Stack<MultipleTreeNode> stack = new Stack<>();
        Stack<MultipleTreeNode> mark = new Stack<>();
        stack.add(this.node);
        MultipleTreeNode lastParent = null;

        while (!stack.isEmpty()) {
            MultipleTreeNode node = stack.peek();

            if (mark.peek() == node || node.children == null) {
                // 说明该输出自己了
                mark.pop();
                System.out.print(stack.pop().value + ", ");
                return;
            }

            // 下次再遇到它，就得把他输出了。
            mark.push(node);
            LinkedHashSet<MultipleTreeNode> children = node.children;
            lastParent = node;

            for (MultipleTreeNode child : children) {
                if (child != null) {
                    stack.push(child);
                }
            }
        }
    }

    public static void main(String[] args) {
        Traversal traversal = new Traversal(new TreeGenerator(0, 25).genDepthFirstTopDownTree());
        traversal.traversal(false);
    }
}
