package com.hyj.algorithm.demo;

/**
 * LCS练习
 */
public class LCSHyj {
    //    标记矩阵 1表示左上 2表示上 3表示左
    public static int[][] B;

    public static int[][] getMatrix(char[] x, char[] y) {
        int i = x.length;
        int j = y.length;
        B = new int[i + 1][j + 1];
        int[][] matrix = new int[i + 1][j + 1];
        for (int i0 = 1; i0 < i + 1; i0++) {
            for (int j0 = 1; j0 < j + 1; j0++) {
                if (x[i0 - 1] == y[j0 - 1]) {
                    matrix[i0][j0] = matrix[i0 - 1][j0 - 1] + 1;
                    B[i0][j0] = 1;
                } else {
                    matrix[i0][j0] = Math.max(matrix[i0 - 1][j0], matrix[i0][j0 - 1]);
                    if (matrix[i0][j0] == matrix[i0 - 1][j0]) {
                        B[i0][j0] = 2;
                    } else {
                        B[i0][j0] = 3;
                    }
                }
            }
        }

        return matrix;
    }

    public static char[] getLcs(int i, int j, char[] x, char[] y) {
        char[] res = new char[i + 1];
        int n = i + 1;
        int m = j + 1;
        while (n != 0 && m != 0 && B[n - 1][m - 1] != 0) {
            if (B[n - 1][m - 1] == 1) {
                res[n - 2] = x[n - 2];
                n--;
                m--;
            }
            if (B[n - 1][m - 1] == 2) {
                n--;
            }
            if (B[n - 1][m - 1] == 3) {
                m--;
            }
        }
        return res;
    }

    public static void print(int[][] array, int n, int m) {
        for (int i = 0; i < n + 1; i++) {
            for (int j = 0; j < m + 1; j++) {
                System.out.printf("%d ", array[i][j]);
            }
            System.out.println();
        }
    }

    /**
     * 给定字符串str1和str2 找出最长公共子序列的长度
     * 如arbc和aebcd LCS为abc 长度为3
     * 分析：定义dp[i][j]为2个字符串的LCS长度，i，j分别为str1 str2长度
     * 则 if str1[i]=str2[j]
     * dp[i][j]=dp[i-1]+dp[j-1]+1
     * else
     * dp[i][j]=max(dp[i-1][j],dp[i][j-1])
     * 初始值 dp[0][j]=dp[i][0]=0 dp[1][1]=(if str1[1]=str2[1] -> 1 else 0)
     * <p>
     * 定义标记函数B[i][j]为str1[i] str2[j]位置的标记符b(b in [1,2,3]) 1表示左上斜箭头↖ 2表示↑ 3表示←
     * 则 if str1[i]=str2[j]
     * B[i][j]=1
     * else
     * if dp[i-1][j]>dp[i][j-1]
     * B[i][j]=2
     * else
     * B[i][j]=3
     * <p>
     * 根据标记矩阵B找出LCS：
     * 从标记矩阵B右下角开始回溯，如果节点为上箭头↑，则继续往上走一格，如果节点为←，则往左移动一格，
     * 如果节点为↖，则筛选出该节点对应的字符串，
     * 且往左上↖移动（往左移动一格再往上移动一格），如果左上箭头对应的格子没有箭头，则停止。
     * 以此类推，找出所有左上斜箭头对应的字符，即为LCS
     * if B[i][j]=1
     * B[i-1][j-1]
     * if B[i][j]=2
     * B[i-1][j]
     * if B[i][j]=3
     * B[i][j-1]
     * if B[i][j]=0
     * return
     *
     * @param str1
     * @param str2
     * @return
     */
    public static int maxLengthOfLCS(String str1, String str2) {
        if (str1 == null) {
            str1 = "";
        }
        if (str2 == null) {
            str2 = "";
        }
        int i = str1.length();
        int j = str2.length();
        //定义dp数组
        int[][] dp = new int[i + 1][j + 1];
        B = new int[i + 1][j + 1];
        //初始值
        for (int p = 0; p <= i; p++) {
            dp[p][0] = 0;
            B[p][0] = 0;
        }
        for (int q = 0; q <= j; q++) {
            dp[0][q] = 0;
            B[0][q] = 0;
        }
        for (int p = 1; p <= i; p++) {
            for (int q = 1; q <= j; q++) {
                if (str1.charAt(p - 1) == str2.charAt(q - 1)) {
                    dp[p][q] = dp[p - 1][q - 1] + 1;
                    //左上
                    B[p][q] = 1;
                } else {
                    dp[p][q] = Integer.max(dp[p - 1][q], dp[p][q - 1]);
                    if (dp[p][q] == dp[p - 1][q]) {
                        //上
                        B[p][q] = 2;
                    } else {
                        //左
                        B[p][q] = 3;
                    }
                }
            }
        }


        return dp[i][j];
    }

    /**
     * 根据标记矩阵B获取LCS
     * * if B[i][j]=1
     * *  B[i-1][j-1]
     * * if B[i][j]=2
     * *  B[i-1][j]
     * * if B[i][j]=3
     * *  B[i][j-1]
     * if B[i][j]=0
     * return
     *
     * @param str1
     * @param str2
     * @return
     */
    public static String getLCSByB(String str1, String str2) {
        StringBuilder res = new StringBuilder("");
        //字符串长度 i j
        int i = B.length - 1;
        int j = B[0].length - 1;

        //从右下角开始遍历
        boolean flag = true;

        while (flag) {
            switch (B[i][j]) {
                case 1:
                    res.insert(0, str1.charAt(i - 1));
                    i--;
                    j--;
                    break;
                case 2:
                    i--;
                    break;
                case 3:
                    j--;
                    break;
                default:
                    flag = false;
                    break;
            }
        }
//        for (int p = i; p >= 0; p--) {
//            for (int q = j; q >= 0; q--) {
//
//            }
//        }

        return res.toString();
    }

    /**
     * 最长公共连续字符串lccs
     * 分析：
     * str1.len=m str2.len=n
     * lccs的长度范围: [0,m]，使用双循环，定义最大字符串lccs为max
     * 假设长度为m，则从str1下标0开始和str2的所有字符进行比较，找出str1下标=0开始的最大lccs和max比较
     * 假设长度为m-1，则从str1下标1开始和str2的所有字符进行比较，找出str1下标=1开始的最大lccs和max比较
     * 假设长度为m-2，则从str1下标2开始和str2的所有字符进行比较，找出str1下标=2开始的最大lccs和max比较
     *
     */
    public static void lccs() {
        String str1 = "abcdesbcdf";
        String str2 = "aebcdfe";
//        byte[] char1 = str1.getBytes();
//        byte[] char2 = str2.getBytes();
        char[] chars1 = str1.toCharArray();
        char[] chars2 = str2.toCharArray();
        int len1 = chars1.length;
        int len2 = chars2.length;
        String maxchar = "";
        StringBuffer temp = new StringBuffer(" ");
        int maxl = 0;
        for (int i = 0; i < len1; i++) {
            for (int j = 0; j < len2; j++) {
                int pos1 = i;
                int pos2 = j;
                int l = 0;
                //temp = new StringBuffer("");
                temp.delete(0, temp.length());

                while (chars1[pos1] == chars2[pos2]) {
                    l++;
                    temp.append(chars1[pos1]);
                    if (++pos1 > len1 - 1) break;
                    if (++pos2 > len2 - 1) break;
                }
                //System.out.println(""+(++loops)+":"+temp);
                if (l > maxl) {
                    maxl = l;
                    maxchar = temp.toString();

                }
            }
        }
        System.out.println("" + maxl + " " + maxchar);
    }


    public static void main(String[] args) {
        char[] X = {'A', 'B', 'C', 'B', 'D', 'A', 'B'};
        char[] Y = {'B', 'D', 'C', 'A', 'B', 'A'};
        String str1 = String.valueOf(X);
        String str2 = String.valueOf(Y);
        System.out.println(str1);
        System.out.println(str2);
        int n = X.length;
        int m = Y.length;
        int[][] matrix = getMatrix(X, Y);
        print(matrix, n, m);
//        print(B, n, m);
//        String res = String.valueOf(getLcs(n, m, X, Y));
//        System.out.println(res);
//
        System.out.println(maxLengthOfLCS(str1, str2));
//        print(B, n, m);
//        System.out.println(getLCSByB(str1,str2));
        lccs();

    }


}
