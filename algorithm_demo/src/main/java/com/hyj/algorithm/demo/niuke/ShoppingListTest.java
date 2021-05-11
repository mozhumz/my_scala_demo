package com.hyj.algorithm.demo.niuke;

import java.util.*;

/**
 * 王强今天很开心，公司发给N元的年终奖。王强决定把年终奖用于购物，他把想买的物品分为两类：主件与附件，附件是从属于某个主件的，下表就是一些主件与附件的例子：
 * <p>
 * 主件	附件
 * 电脑	打印机，扫描仪
 * 书柜	图书
 * 书桌	台灯，文具
 * 工作椅	无
 * <p>
 * 如果要买归类为附件的物品，必须先买该附件所属的主件。每个主件可以有 0 个、 1 个或 2 个附件。附件不再有从属于自己的附件。王强想买的东西很多，为了不超出预算，他把每件物品规定了一个重要度，分为 5 等：用整数 1 ~ 5 表示，第 5 等最重要。他还从因特网上查到了每件物品的价格（都是 10 元的整数倍）。他希望在不超过 N 元（可以等于 N 元）的前提下，使每件物品的价格与重要度的乘积的总和最大。
 * 设第 j 件物品的价格为 v[j] ，重要度为 w[j] ，共选中了 k 件物品，编号依次为 j 1 ， j 2 ，……， j k ，则所求的总和为：
 * v[j 1 ]*w[j 1 ]+v[j 2 ]*w[j 2 ]+ … +v[j k ]*w[j k ] 。（其中 * 为乘号）
 * 请你帮助王强设计一个满足要求的购物单
 * 输入描述:
 * 输入的第 1 行，为两个正整数，用一个空格隔开：N m
 * （其中 N （ <32000 ）表示总钱数， m （ <60 ）为希望购买物品的个数。）
 * 从第 2 行到第 m+1 行，第 j 行给出了编号为 j-1 的物品的基本数据，每行有 3 个非负整数 v p q
 * （其中 v 表示该物品的价格（ v<10000 ）， p 表示该物品的重要度（ 1 ~ 5 ）， q 表示该物品是主件还是附件。
 * 如果 q=0 ，表示该物品为主件，如果 q>0 ，表示该物品为附件， q 是所属主件的编号）
 * <p>
 * 输出描述:
 * 输出文件只有一个正整数，为不超过总钱数的物品的价格与重要度乘积的总和的最大值（ <200000 ）。
 * <p>
 * 输入
 * 1000 5
 * 800 2 0
 * 400 5 1
 * 300 5 1
 * 400 3 0
 * 500 2 0
 * <p>
 * 输出
 * 2200
 */
public class ShoppingListTest {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        //总金额
        int N = sc.nextInt();
        //物品数
        int m = sc.nextInt();

        //表示第i行某物品的参数
        int[][] V = new int[m + 1][3];
        int[][] P = new int[m + 1][3];
//        int[][] Q = new int[m + 1][3];
        //物品分组 每组有主从物品 最大件数为3
        for (int i = 1; i <= m; i++) {
            int v = sc.nextInt() / 10;
            int p = sc.nextInt();
            int q = sc.nextInt();
            if (q == 0) {
                V[i][0] = v;
                P[i][0] = p;
//                Q[i][0] = q;
            } else {
                if (V[q][1] == 0) {
                    V[q][1] = v;
                    P[q][1] = p;
//                    Q[q][1] = q;
                } else {
                    V[q][2] = v;
                    P[q][2] = p;
//                    Q[q][2] = q;
                }
            }
        }
//        printDp1(N,m,V,P);

    }

    private static void printDp1(int N, int m, int[][] V, int[][] P) {
        //定义dp[i][j] 在给定j金额和i物品数时的最大权值
        int[][] dp = new int[m + 1][N / 10+1];
        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= N / 10; j += 1) {
                //如果不选一组物品
                dp[i][j] = dp[i - 1][j];
                //如果选择某组 则可能情况为【主，主+从1，主+从2，主+从1+从2】
                if (j >= V[i][0]) {
                    dp[i][j] = Math.max(dp[i][j], dp[i - 1][j - V[i][0]] + V[i][0] * P[i][0]);
                }
                if (V[i][1] != 0 && j >= V[i][0] + V[i][1]) {
                    dp[i][j] = Math.max(dp[i][j], dp[i - 1][j - V[i][0] - V[i][1]] + V[i][0] * P[i][0] + V[i][1] * P[i][1]);
                }
                if (V[i][2] != 0 && j >= V[i][0] + V[i][2]) {
                    dp[i][j] = Math.max(dp[i][j], dp[i - 1][j - V[i][0] - V[i][2]] + V[i][0] * P[i][0] + V[i][2] * P[i][2]);
                }
                if (V[i][2] != 0 && V[i][1] != 0 && j >= V[i][0] + V[i][1] + V[i][2]) {
                    dp[i][j] = Math.max(dp[i][j], dp[i - 1][j - V[i][0] - V[i][1] - V[i][2]]
                            + V[i][0] * P[i][0] + V[i][1] * P[i][1] + V[i][2] * P[i][2]);
                }
            }
        }
        System.out.println(dp[m][N/10]*10);
    }

    private static void printDp2(int N, int m, int[][] V, int[][] P) {

    }
}
