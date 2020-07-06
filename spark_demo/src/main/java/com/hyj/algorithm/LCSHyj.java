package com.hyj.algorithm;

/**
 * LCS练习
 */
public class LCSHyj {
    public static int[][] getMatrix(char[] x, char[] y) {
        int i = x.length;
        int j = y.length;
        int[][]matrix=new int[i+1][j+1];
        for (int j0 = 1; j0 < j + 1; j0++) {
            for (int i0 = 1; i0 < i + 1; i0++) {
                if(x[i0-1]==y[j0-1]){
                    matrix[i0][j0]=matrix[i0-1][j0-1]+1;
                }else {
                    matrix[i0][j0]=Math.max(matrix[i0-1][j0],matrix[i0][j0-1]);
                }
            }
        }

        return matrix;
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
        int[][]matrix=getMatrix(X,Y);
        print(matrix,n,m);
    }
}
