package com.hyj.algorithm;

/**
 * LongestCommonSubsequence 最长公共子序列
 * 子序列：去掉字符串的任意位置的任意个字符后的序列  如abcd，子序列有 acd cd d bd 等
 * LCS:给定字符串序列 X Y，两者子序列中长度最长的序列
 */
public class LCS {
    public static int[][] mem;
    public static int[][] s;
    public static int[] result; // 记录子串下标

    public static int LCS(char[] X, char[] Y, int n, int m) {
        for (int i = 0; i <= n; i++) {
            mem[i][0] = 0;
            s[i][0] = 0;
        }
        for (int i = 0; i <= m; i++) {
            mem[0][i] = 0;
            s[0][i] = 0;
        }
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= m; j++) {
                if (X[i - 1] == Y[j - 1]) {
                    mem[i][j] = mem[i - 1][j - 1] + 1;
                    s[i][j] = 1;
                } else {
                    mem[i][j] = Math.max(mem[i][j - 1], mem[i - 1][j]);
                    if (mem[i][j] == mem[i - 1][j]) {
                        s[i][j] = 2;
                    } else s[i][j] = 3;
                }
            }
        }
        return mem[n][m];
    }

    // 追踪解
    public static void trace_solution(int n, int m) {
        int i = n;
        int j = m;
        int p = 0;
        while (true) {
            if (i == 0 || j == 0) break;
            if (s[i][j] == 1) {
                result[p] = i;
                p++;
                i--;
                j--;
            } else if (s[i][j] == 2) {
                i--;
            } else { //s[i][j] == 3
                j--;
            }
        }

    }

    public static void print(int[][] array, int n, int m) {
        for (int i = 0; i < n + 1; i++) {
            for (int j = 0; j < m + 1; j++) {
                System.out.printf("%d ", array[i][j]);
            }
            System.out.println();
        }
    }

    public static void main(String[] args) {
        char[] X = {'A', 'B', 'C', 'B', 'D', 'A', 'B'};
        char[] Y = {'B', 'D', 'C', 'A', 'B', 'A'};
        int n = X.length;
        int m = Y.length;
        // 这里重点理解，相当于多加了第一行 第一列。
        mem = new int[n + 1][m + 1];
        // 1 表示 左上箭头  2 表示 上  3 表示 左
        s = new int[n + 1][m + 1];
        result = new int[Math.min(n, m)];
        int longest = LCS(X, Y, n, m);
        System.out.println("备忘录表为：");
        print(mem, n, m);
        System.out.println("标记函数表为：");
        print(s, n, m);
        System.out.printf("longest : %d \n", longest);

        trace_solution(n, m);
        // 输出注意  result 记录的是字符在序列中的下标
        for (int k = longest - 1; k >= 0; k--) {
            // 还需要再减一 才能跟 X Y序列对齐。
            int index = result[k] - 1;
            System.out.printf("%c ", X[index]);
        }
        System.out.println();

    }
}
