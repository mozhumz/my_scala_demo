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

    public static char[] getLcs(int i,int j,char[]x,char[]y) {
        char[] res = new char[i + 1];
        int n=i+1;
        int m=j+1;
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


    public static void main(String[] args) {
        char[] X = {'A', 'B', 'C', 'B', 'D', 'A', 'B'};
        char[] Y = {'B', 'D', 'C', 'A', 'B', 'A'};
        int n = X.length;
        int m = Y.length;
        int[][] matrix = getMatrix(X, Y);
        print(matrix, n, m);
        print(B,n,m);
        getLcs(n,m,X,Y);
    }
}
