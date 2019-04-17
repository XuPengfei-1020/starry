package kmp;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * 字典树
 */
public class DictTree {
    public static class Node {
        HashMap<Character, Node> children = new HashMap<>();
        Node parent;
        boolean end;
        int index;
        int failureIndex;
    }

    public static Node createDictTree(String ... ss) {
        int index = 0;
        Node root = new Node();
        root.index = index ++;

        for (String s : ss) {
            Node currentNode = root;

            for (int i = 0; i < s.length(); i++) {
                Node next = currentNode.children.get(s.charAt(i));

                if (next == null) {

                    do {
                        next = new Node();
                        currentNode.children.put(s.charAt(i), next);
                        next.parent = currentNode;
                        next.index = index ++;
                        currentNode = next;
                        i++;
                    } while (i < s.length());

                    next.end = true;
                    continue;
                }

                currentNode = next;
            }
        }

        return root;
    }

    public static void main(String[] args) {
        Node root =  createDictTree("abc", "abced", "hers", "he", "his", "she");
    }
}
