package tree.multiple;

import java.util.Random;

/**
 * 生成树，默认每一个树的 children 最多只有 7 个。
 */
public class TreeGenerator {
    /**
     * 范围
     */
    private int value;
    private int maxValue;

    /**
     * Constructor
     */
    public TreeGenerator(int value, int maxValue) {
        this.value = value;
        this.maxValue = maxValue;
    }

    public MultipleTreeNode genDepthFirstTopDownTree() {
        return this.genDepthFirstTopDownTree0(this.value, this.maxValue);
    }

    /**
     * 递归生成多叉树。
     * @param initValue 初始节点的值，区间左闭合
     * @param maxValue 最大节点的值，区间右闭合
     * @return 生成的树
     */
    private MultipleTreeNode genDepthFirstTopDownTree0(int initValue, int maxValue) {
        if (initValue > maxValue) {
            return null;
        }

        MultipleTreeNode root = new MultipleTreeNode(initValue++);
        int range = maxValue - initValue + 1;

        if (range != 0) {
            int childrenNum = new Random().nextInt(7) + 1;
            // initValue - maxValue 分成 childrenNum 段。 其中前 remain 个跨度为 stepLen + 1，后几个跨度为 stepLen。
            int stepLen = range / childrenNum;
            int remain = range % childrenNum;

            for (int start = initValue; start <= maxValue;) {
                int stop = start + stepLen + (remain -- > 0 ? 0 : -1);
                root.addChildren(genDepthFirstTopDownTree0(start, stop));
                start = stop + 1;
            }
        }

        return root;
    }
}
