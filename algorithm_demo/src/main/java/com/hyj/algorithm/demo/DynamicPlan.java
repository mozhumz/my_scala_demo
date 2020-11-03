package com.hyj.algorithm.demo;

import java.util.List;

/**
 * 动态规划
 * <p>
 * 动态规划，无非就是利用历史记录，来避免我们的重复计算。而这些历史记录，我们得需要一些变量来保存，
 * 一般是用一维数组或者二维数组来保存
 * <p>
 * 动态规划的三大步骤：
 * 第一步骤：定义数组元素的含义，上面说了，我们会用一个数组，来保存历史数组，假设用一维数组 dp[] 吧。这个时候有一个非常非常重要的点，就是规定你这个数组元素的含义，例如你的 dp[i] 是代表什么意思？
 * <p>
 * 第二步骤：找出数组元素之间的关系式，我觉得动态规划，还是有一点类似于我们高中学习时的归纳法的，
 * 当我们要计算 dp[n] 时，是可以利用 dp[n-1]，dp[n-2].....dp[1]，来推出 dp[n] 的，也就是可以利用历史数据来推出新的元素值，
 * 所以我们要找出数组元素之间的关系式，例如 dp[n] = dp[n-1] + dp[n-2]，这个就是他们的关系式了。而这一步，也是最难的一步，
 * 后面我会讲几种类型的题来说。
 * <p>
 * 第三步骤：找出初始值。学过数学归纳法的都知道，虽然我们知道了数组元素之间的关系式，例如 dp[n] = dp[n-1] + dp[n-2]，
 * 我们可以通过 dp[n-1] 和 dp[n-2] 来计算 dp[n]，但是，我们得知道初始值啊，例如一直推下去的话，会由 dp[3] = dp[2] + dp[1]。
 * 而 dp[2] 和 dp[1] 是不能再分解的了，所以我们必须要能够直接获得 dp[2] 和 dp[1] 的值，而这，就是所谓的初始值
 */
public class DynamicPlan {
    /**
     * 青蛙跳台阶
     * 一只青蛙一次可以跳上1级台阶，也可以跳上2级。求该青蛙跳上一个n级的台阶总共有多少种跳法
     * 分析：定义dp[n]为青蛙跳上n级台阶的跳法数，
     * 青蛙先跳1阶，则剩余台阶的跳法数为dp[n-1]
     * 青蛙先跳2阶，则剩余台阶的跳法数为dp[n-2]
     * 所以dp[n]=dp[n-1]+dp[n-2]
     * 初始值 dp[0]=0,dp[1]=1,dp[2]=2,dp[3]=dp[2]+dp[1]
     *
     * @return
     */
    public static int frogJumpSteps(int n) {
        if (n <= 1) {
            return n;
        }
        if (n == 2) {
            return 2;
        }
        return frogJumpSteps(n - 1) + frogJumpSteps(n - 2);
    }

    public static int frogJumpSteps2(int n) {
        if (n <= 1) {
            return n;
        }
        if (n == 2) {
            return 2;
        }
        //数组保存跳上i级台阶的跳法数,数组长度为n+1,下标0-n
        int[] dp = new int[n + 1];
        dp[0] = 0;
        dp[1] = 1;
        dp[2] = 2;
        for (int i = 3; i <= n; i++) {
            dp[i] = dp[i - 1] + dp[i - 2];
        }

        return dp[n];
    }

    /**
     * 机器人走网格
     * 一个机器人位于一个 m x n 网格的左上角，机器人每次只能向下或者向右移动一步。机器人试图达到网格的右下角
     * 问总共有多少条不同的路径？
     * <p>
     * 分析 网格可以看做一个二维数组，i j分别表示网格的行标和列标，则i=m-1 j= n-1
     * 定义dp[i][j]为机器人从左上角走到ij位置的路径数，
     * 则机器人在(i-1,j)位置时，走到ij只有1种路径（向下走一步），此走到(i-1,j)位置的路径数为dp[i-1][j]
     * 机器人在(i,j-1)位置时，走到ij只有1种路径（向右走一步），此走到(i,j-1)位置的路径数为dp[i][j-1]
     * 所以dp[i][j]=dp[i-1][j]+dp[i][j-1] 即dp[m][n]=dp[m-1][n]+dp[m][n-1]
     * 初始值 因为机器人从（0,0）走到（0，j）或(i,0)的路径数为1，所以dp[0][j]=dp[i][0]=1
     * dp[1][1]=dp[0][1]+dp[1][0]=2 dp[1][2]=dp[1][1]+dp[0][2]=2+1=3
     * dp[2][1]=dp[1][1]+dp[2][0]=3
     * dp[2][2]=dp[1][2]+dp[2][1]=6
     *
     * @param m
     * @param n
     * @return
     */
    public static int robotWalkGrid(int m, int n) {
        if (m <= 1 && n <= 1) {
            return 0;
        }
        if (m == 1 || n == 1) {
            return 1;
        }
        return robotWalkGrid(m, n - 1) + robotWalkGrid(m - 1, n);
    }

    /**
     * 非递归实现
     *
     * @param m
     * @param n
     * @return
     */
    public static int robotWalkGrid2(int m, int n) {
        if (m <= 1 && n <= 1) {
            return 0;
        }
        if (m == 1 || n == 1) {
            return 1;
        }
        int[][] dp = new int[m][n];
        for (int i = 0; i < m; i++) {
            dp[i][0] = 1;
        }
        for (int j = 0; j < n; j++) {
            dp[0][j] = 1;
        }
        for (int i = 1; i < m; i++) {
            for (int j = 1; j < n; j++) {
                dp[i][j] = dp[i - 1][j] + dp[i][j - 1];
            }
        }

        return dp[m - 1][n - 1];
    }

    /**
     * 最优路径选择
     * 给定一个包含非负整数的 m x n 网格（每个网格有一个数字），请找出一条从左上角到右下角的路径，使得路径上的数字总和为最小。
     * 说明：每次只能向下或者向右移动一步
     * <p>
     * 举例：
     * 输入:
     * arr = [
     * [1,3,1],
     * [1,5,1],
     * [4,2,1]
     * ]
     * 输出: 7
     * 解释: 因为路径 1→3→1→1→1 的总和最小
     * <p>
     * 分析：
     * 定义dp[i][j]为起始位置（0,0）到ij位置的最优路径的和（和最小），i=m-1 j=n-1,arr[i][j]表示ij位置的数字
     * 则ij位置的前一个位置，有2种，dp[i-1][j] dp[i][j-1]
     * (0,0)到ij位置的最小路径和dp[i][j]=min(dp[i-1][j],dp[i][j-1])+arr[i][j]
     * 初始值 dp[0][j]=dp[0][j-1]+arr[0][j] dp[i][0]=dp[i-1][0]+arr[i][0]
     *
     * @param grid
     * @return
     */
    public static  int minSumGrid(int[][] grid){
        int i=grid.length-1;
        int j=grid[0].length-1;
        return minSumGrid(grid,i,j);
    }

    public static int minSumGrid(int[][] grid, int i, int j) {
        if (i <= 0 && j <= 0) {
            return grid[0][0];
        }
        //初始化
        if (i == 0) {
            return minSumGrid(grid, 0, j - 1) + grid[0][j];
        }
        if (j == 0) {
            return minSumGrid(grid, i - 1, 0) + grid[i][0];
        }
        //递推公式 dp[i][j]=min(dp[i-1][j],dp[i][j-1])+arr[i][j]
        return minNum(minSumGrid(grid,i-1,j),minSumGrid(grid,i,j-1))+grid[i][j];
    }

    public static int minNum(int a,int b){
        if(a<b){
            return a;
        }
        return b;
    }

    /**
     * 非递归实现
     * @param grid
     * @return
     */
    public static  int minSumGrid2(int[][] grid){
        int i=grid.length-1;
        int j=grid[0].length-1;
        int[][]dp=new int[i+1][j+1];
        if(i<=0&&j<=0){
            return grid[0][0];
        }

        //初始化 dp[0][j] dp[i][0]
        dp[0][0]=grid[0][0];
        for(int k=1;k<=i;k++){
            dp[k][0]=grid[k][0]+dp[k-1][0];
        }
        for(int l=1;l<=j;l++){
            dp[0][l]=grid[0][l]+dp[0][l-1];
        }
        //递推公式dp[i][j]=min(dp[i-1][j],dp[i][j-1])+arr[i][j]
        for(int p=1;p<=i;p++){
            for (int q=1;q<=j;q++){
                dp[p][q]=minNum(dp[p-1][q],dp[p][q-1])+grid[p][q];
            }
        }

        return dp[i][j];
    }



    public static void main(String[] args) {
//        System.out.println(frogJumpSteps(20));
//        System.out.println(robotWalkGrid(9, 3));
//        System.out.println(robotWalkGrid2(9, 3));
        int[][]arr={{1,3,1},{1,5,1},{4,2,1}};
        int[][]arr2={{1},{2},{3}};
        System.out.println(minSumGrid(arr));
        System.out.println(minSumGrid2(arr));
    }
}
