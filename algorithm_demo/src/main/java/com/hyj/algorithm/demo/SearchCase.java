package com.hyj.algorithm.demo;

import org.junit.Test;

/**
 * 搜索算法
 */
public class SearchCase {
    /**
     * 深度优先搜索（DFS Deep First Search）
     * 给定一个m x n 二维字符网格board 和一个字符串单词word 。如果word 存在于网格中，返回 true ；否则，返回 false 。
     * <p>
     * 单词必须按照字母顺序，通过相邻的单元格内的字母构成，其中“相邻”单元格是那些水平相邻或垂直相邻的单元格。
     * 同一个单元格内的字母不允许被重复使用。
     * <p>
     * 例如，在下面的 3×4 的矩阵中包含单词 "ABCCED"（单词中的字母已标出）
     * 示例 1：
     * <p>
     * 输入：board = [["A","B","C","E"],
     * ["S","F","C","S"],
     * ["A","D","E","E"]],
     * word = "ABCCED"
     * 输出：true
     * <p>
     * 示例 2：
     * 输入：board = [["a","b"],["c","d"]], word = "abcd"
     * 输出：false
     * <p>
     * 分析：
     * 遍历矩阵的每个位置b[i][j]，以该位置作为起点进行搜索，每次可以移动的方向为上下左右，
     * 如果起始位置b[i][j]和word中的对应位置word[k]的字符相同，则往上下左右移动起始位置，
     * 继续查找和word[k+1]相同的字符，
     * 递归上述过程，直到b[i][j]经过的位置和word中的字符全部匹配，则返回true，如果不匹配则返回false
     * 提前结束搜索的条件：i j下标越界，b[i][j]!=word[k],同一位置搜索多次（如先下后上），如果k为word最后一个字符则返回true
     * 为了避免同一位置搜索多次，可新建一个和b矩阵大小相同的矩阵，每次搜索时，经过的位置标记为true
     */
    @Test
    public void test202105231438() {

    }

    public boolean exist(char[][] board, String word) {
        int m = board.length;
        if (m == 0||word==null||word.length()==0) {
            return false;
        }
        int n = board[0].length;
        if (n == 0) {
            return false;
        }
        boolean[][]his=new boolean[m][n];
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if(exist(board,word,his,i,j,0,m,n)){
                    return true;
                }
            }
        }
        return false;
    }

    public boolean exist(char[][] board, String word, boolean[][] his, int i, int j, int k, int m, int n) {
        if (i < 0 || i > m - 1 || j < 0 || j > n - 1) {
            return false;
        }
        if (board[i][j] != word.charAt(k) || his[i][j]) {
            return false;
        }
        //已经搜索到word最后一个字符 ;
        if (k == word.length() - 1) {
            return true;
        }
        //开始在ij位置为起点进行递归搜索 标记ij位置
        his[i][j]=true;
        boolean res=exist(board,word,his,i+1,j,k+1,m,n)||exist(board,word,his,i,j+1,k+1,m,n)
                ||exist(board,word,his,i-1,j,k+1,m,n)||exist(board,word,his,i,j-1,k+1,m,n);

        //ij位置搜索完毕 清除标记
        his[i][j]=false;

        return res;
    }


}
