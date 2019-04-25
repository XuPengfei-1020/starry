package kmp;

/**
 * kmp 算法
 */
public class Kmp {
    private static int[] preproc(String s) {
        if (null == s || s.length() == 0) {
            throw new RuntimeException("some null tip");
        }

        int[] rs = new int[s.length()];
        rs[0] = 0;

        for (int i = 1; i < s.length(); i++) {
            char ic = s.charAt(i);
            int j = rs[i - 1];

            while (j > 0 && s.charAt(j) != ic) {
                j = rs[j - 1];
            }

            rs[i] = (j == 0 && s.charAt(j) != ic) ? 0 : (j + 1);
        }

        return rs;
    }

    /**
     * 计算 b 的子串在 a 中出现过的长度， rs[] 下标即为位置。
     * @param a
     * @param b
     * @return
     */
    private static int[] process(String a, String b) {
        if (null == a || a.length() == 0 || null == b || b.length() == 0) {

        }

        int[] failure = preproc(b);
        int[] rs = new int[a.length()];
        int j = 0;

        for (int i = 0; i < a.length(); i++) {
            if (a.charAt(i) == b.charAt(j)) {
                rs[i] = ++j;

                if (j == b.length()) {
                    j = failure[j-1];
                }

                continue;
            }

            j = failure[j];
        }

        return rs;
    }

    public static void main(String[] args) {
        String s = "abcedabcddfabbaccedabcedffabf";

        int[] rs = preproc(s);

        for (int l : rs) {
            System.out.print(l);
        }

        System.out.println();
        String a = "acccdddabacedabacedabacedabacedabacedsadfafabacedabced";
        rs = process(a,"abaced");

        for (int l : rs) {
            System.out.print(l);
        }
    }
}