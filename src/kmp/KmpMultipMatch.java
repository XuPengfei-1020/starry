package kmp;

import com.sun.xml.internal.ws.api.pipe.ServerTubeAssemblerContext;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

public class KmpMultipMatch {
    private static void search(String a, String  ... s) {
        DictTree.Node root = DictTree.createDictTree(s);

    }

    // 生产 nodes 的失效信息
    private static void preproc(DictTree.Node root) {
        // dosome check

        HashMap<Character, DictTree.Node> children = root.children;

        Stack<Map.Entry<Character, DictTree.Node>> stack = new Stack<>();

        for (Map.Entry<Character, DictTree.Node> entry : children.entrySet()) {
            stack.push(entry);
        }

        while (stack.empty()) {
            Map.Entry<Character, DictTree.Node> next = stack.pop();
            // process current node
            System.out.println("c:" + next.getKey());
            children = next.getValue().children;

            if (children != null) {
                for (Map.Entry<Character, DictTree.Node> entry : children.entrySet()) {
                    stack.push(entry);
                }
            }
        }
    }

    /**
     * 非递归方式，遍历树。
     */
    private static void print(DictTree.Node root) {
        // dosome check
        HashMap<Character, DictTree.Node> children = root.children;

        Stack<Map.Entry<Character, DictTree.Node>> stack = new Stack<>();

        while (stack.empty()) {
            Map.Entry<Character, DictTree.Node> next = stack.pop();
            // process current node
            System.out.println("c:" + next.getKey());
            children = next.getValue().children;

            if (children != null) {
                for (Map.Entry<Character, DictTree.Node> entry : children.entrySet()) {
                    stack.push(entry);
                }
            }
        }
    }
}