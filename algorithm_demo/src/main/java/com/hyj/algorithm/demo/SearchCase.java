package com.hyj.algorithm.demo;

import org.junit.Test;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 搜索算法
 * <p>
 * 回溯算法解题框架：
 * {for 选择 in 选择列表:
 * # 做选择
 * 将该选择从选择列表移除
 * 路径.add(选择)
 * backtrack(路径, 选择列表)
 * # 撤销选择
 * 路径.remove(选择)
 * 将该选择再加入选择列表}
 */
public class SearchCase {
    /**
     * 深度优先搜索（DFS Depth First Search）
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
        if (m == 0 || word == null || word.length() == 0) {
            return false;
        }
        int n = board[0].length;
        if (n == 0) {
            return false;
        }
        boolean[][] his = new boolean[m][n];
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (exist(board, word, his, i, j, 0, m, n)) {
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
        his[i][j] = true;
        boolean res = exist(board, word, his, i + 1, j, k + 1, m, n) || exist(board, word, his, i, j + 1, k + 1, m, n)
                || exist(board, word, his, i - 1, j, k + 1, m, n) || exist(board, word, his, i, j - 1, k + 1, m, n);

        //ij位置搜索完毕 清除标记
        his[i][j] = false;

        return res;
    }

    /**
     * 22. 括号生成
     * 数字 n代表生成括号的对数，请你设计一个函数，用于能够生成所有可能的并且 有效的 括号组合。
     * 示例 1：
     * 输入：n = 3
     * 输出：["((()))","(()())","(())()","()(())","()()()"]
     * <p>
     * 示例 2：
     * 输入：n = 1
     * 输出：["()"]
     * <p>
     * 提示：
     * 1 <= n <= 8
     * <p>
     * 分析：
     * 如果n组括号合法，则起始位置一定是左括号(，与之相对应的右括号，可能会包含多组括号，
     * 包含的括号数量范围在[0,n-1]，设包含的括号数量为p，未被包含的数量q，则p+q=n-1
     * 即：可能的n组有效括号是：(p组括号的排列组合)q组括号的排列组合, p ->[0,n-1]
     */
    @Test
    public void test202105281236() {
//        System.out.println(generateParenthesis(4));
        System.out.println(generateParenthesis2(3));
    }

    public List<String> generateParenthesis(int n) {
        List<List<String>> totalList = new ArrayList<>(n);
        List<String> resOf0 = new ArrayList<>();
        resOf0.add("");
        totalList.add(resOf0);
        List<String> resOf1 = new ArrayList<>();
        resOf1.add("()");
        totalList.add(resOf1);
        for (int i = 2; i <= n; i++) {
            List<String> resOfI = new ArrayList<>();
            for (int j = 0; j < i; j++) {
                List<String> pList = totalList.get(j);
                List<String> qList = totalList.get(i - 1 - j);
                for (String p : pList) {
                    for (String q : qList) {
                        resOfI.add("(" + p + ")" + q);
                    }
                }
            }
            totalList.add(resOfI);
        }

        return totalList.get(n);
    }

    public List<String> generateParenthesis2(int n) {
        List<String> res = new ArrayList<>();
        generateParenthesisDFS(res, n, n, "");
        return res;
    }

    /**
     * 深度优先搜索查找符合条件的括号组合
     * 合法的括号组合一定是左右括号相互匹配，定义left right为每次剩余的左右括号数量，
     * 每次搜索执行如下操作：
     * 0 搜索的出口：left==right==0
     * 1 剪枝去掉非法组合的条件：left>right，即已经追加的字符串cur中右括号数量>左括号数量
     * 2 先追加左括号，前提left>0，执行left-1并递归
     * 3 追加右括号，前提left<right
     *
     * @param res
     * @param left  剩余的左括号数量
     * @param right 剩余的右括号数量
     * @param cur
     */
    public void generateParenthesisDFS(List<String> res, int left, int right, String cur) {
        if (left == 0 && right == 0) {
            res.add(cur);
            return;
        }
//        if (left > right) {
//            return;
//        }
        if (left > 0) {
            generateParenthesisDFS(res, left - 1, right, cur + "(");
        }

        if (left < right) {
            generateParenthesisDFS(res, left, right - 1, cur + ")");
        }

    }

    /**
     * 46全排列
     * 给定一个不含重复数字的数组 nums ，返回其 所有可能的全排列 。你可以 按任意顺序 返回答案。
     * 示例 1：
     * 输入：nums = [1,2,3]
     * 输出：[[1,2,3],[1,3,2],[2,1,3],[2,3,1],[3,1,2],[3,2,1]]
     * <p>
     * 示例 2：
     * 输入：nums = [0,1]
     * 输出：[[0,1],[1,0]]
     * <p>
     * 示例 3：
     * 输入：nums = [1]
     * 输出：[[1]]
     * <p>
     * 分析：
     * 我们尝试在纸上写 33 个数字、44 个数字、55 个数字的全排列，相信不难找到这样的方法。以数组 [1, 2, 3] 的全排列为例。
     * 先写以 11 开头的全排列，它们是：[1, 2, 3], [1, 3, 2]，即 1 + [2, 3] 的全排列（注意：递归结构体现在这里）；
     * 再写以 22 开头的全排列，它们是：[2, 1, 3], [2, 3, 1]，即 2 + [1, 3] 的全排列；
     * 最后写以 33 开头的全排列，它们是：[3, 1, 2], [3, 2, 1]，即 3 + [1, 2] 的全排列。
     * 总结搜索的方法：按顺序枚举每一位可能出现的情况，已经选择的数字在 当前 要选择的数字中不能出现。
     * 按照这种策略搜索就能够做到 不重不漏。这样的思路，可以用一个树形结构表示。
     * 看到这里的朋友，建议先尝试自己画出「全排列」问题的树形结构：resources/全排列.png
     * 说明：
     * 每一个结点表示了求解全排列问题的不同的阶段，这些阶段通过变量的「不同的值」体现，这些变量的不同的值，称之为「状态」；
     * 使用深度优先遍历有「回头」的过程，在「回头」以后， 状态变量需要设置成为和先前一样 ，因此在回到上一层结点的过程中，
     * 需要撤销上一次的选择，这个操作称之为「状态重置」；
     * 深度优先遍历，借助系统栈空间，保存所需要的状态变量，在编码中只需要注意遍历到相应的结点的时候，
     * 状态变量的值是正确的，具体的做法是：往下走一层的时候，path 变量在尾部追加，而往回走的时候，需要撤销上一次的选择，
     * 也是在尾部操作，因此 path 变量是一个栈；
     * 深度优先遍历通过「回溯」操作，实现了全局使用一份状态变量的效果。
     * 使用编程的方法得到全排列，就是在这样的一个树形结构中完成 遍历，从树的根结点到叶子结点形成的路径就是其中一个全排列。
     * <p>
     * 设计状态变量
     * 首先这棵树除了根结点和叶子结点以外，每一个结点做的事情其实是一样的，即：在已经选择了一些数的前提下，
     * 在剩下的还没有选择的数中，依次选择一个数，这显然是一个 递归 结构；
     * 递归的终止条件是： 一个排列中的数字已经选够了 ，因此我们需要一个变量来表示当前程序递归到第几层，
     * 我们把这个变量叫做 depth，或者命名为 index ，表示当前要确定的是某个全排列中下标为 index 的那个数是多少；
     * 布尔数组 used，初始化的时候都为 false 表示这些数还没有被选择，当我们选定一个数的时候，
     * 就将这个数组的相应位置设置为 true ，这样在考虑下一个位置的时候，就能够以 O(1)O(1) 的时间复杂度判断这个数是否被选择过，
     * 这是一种「以空间换时间」的思想。
     * 这些变量称为「状态变量」，它们表示了在求解一个问题的时候所处的阶段。需要根据问题的场景设计合适的状态变量。
     */
    @Test
    public void test202105292123() {
        int[] arr = {1, 2, 3};
        System.out.println(permute(arr));
    }

    public List<List<Integer>> permute(int[] nums) {
        int len = nums.length;
        // 使用一个动态数组保存所有可能的全排列
        List<List<Integer>> res = new ArrayList<>();
        if (len == 0) {
            return res;
        }

        boolean[] used = new boolean[len];
        List<Integer> path = new ArrayList<>();

        dfs(nums, len, 0, path, used, res);
        return res;
    }

    private void dfs(int[] nums, int len, int depth,
                     List<Integer> path, boolean[] used,
                     List<List<Integer>> res) {
        if (depth == len) {
            res.add(new ArrayList<>(path));
            return;
        }

        // 在非叶子结点处，产生不同的分支，这一操作的语义是：
        // 在还未选择的数中依次选择一个元素作为下一个位置的元素，这显然得通过一个循环实现。
        for (int i = 0; i < len; i++) {
            if (!used[i]) {
                path.add(nums[i]);
                used[i] = true;

                dfs(nums, len, depth + 1, path, used, res);
                // 注意：下面这两行代码发生 「回溯」，
                // 回溯发生在从 深层结点 回到 浅层结点 的过程，代码在形式上和递归之前是对称的
                used[i] = false;
                path.remove(path.size() - 1);
            }
        }
    }

    /**
     * 47 全排列2
     * 给定一个可包含重复数字的序列 nums ，按任意顺序 返回所有不重复的全排列。
     * <p>
     * 示例 1：
     * 输入：nums = [1,1,2]
     * 输出：
     * [[1,1,2],
     * [1,2,1],
     * [2,1,1]]
     * <p>
     * 示例 2：
     * 输入：nums = [1,2,3]
     * 输出：[[1,2,3],[1,3,2],[2,1,3],[2,3,1],[3,1,2],[3,2,1]]
     * <p>
     * 分析：
     * 在全排列的基础上，同一层级去重即可
     * 同一层级去重-法1：每层新建一个map，对待选的元素进行判断
     * 同一层级去重-法2：对数组排序，当前元素和上一个元素比较，上一个元素如果没有被选择：
     * i>0&&nums[i]==nums[i-1]&&!visited[i-1]
     */
    @Test
    public void test202106022034() {
        int[] arr = {1, 2, 2};
        System.out.println(permuteUnique(arr));
    }

    public List<List<Integer>> permuteUnique(int[] nums) {
        List<List<Integer>> res = new ArrayList<>();
        permuteWithRepeat(nums, res, new LinkedList<>(), new boolean[nums.length]);
        return res;
    }

    public void permuteWithRepeat(int[] nums, List<List<Integer>> res, LinkedList<Integer> path,
                                  boolean[] visited) {
        if (path.size() == nums.length) {
            res.add(new ArrayList<>(path));
            return;
        }
//        Map<Integer, Integer> map=new HashMap<>();
        for (int i = 0; i < nums.length; i++) {
            //排除已经选择的元素和同一层级的重复元素
            if (visited[i] || (i > 0 && nums[i] == nums[i - 1] && !visited[i - 1])/*map.get(nums[i])!=null*/) {
                continue;
            }
            //同层级的元素列表存储
//            map.put(nums[i],0);
            //做出选择
            visited[i] = true;
            path.add(nums[i]);
            //递归进入下一层
            permuteWithRepeat(nums, res, path, visited);
            //取消选择
            visited[i] = false;
            path.removeLast();

        }

    }

    /**
     * 剑指 Offer 13. 机器人的运动范围
     * 地上有一个m行n列的方格，从坐标 [0,0] 到坐标 [m-1,n-1] 。一个机器人从坐标 [0, 0] 的格子开始移动，
     * 它每次可以向左、右、上、下移动一格（不能移动到方格外），也不能进入行坐标和列坐标的数位之和大于k的格子。
     * 例如，当k为18时，机器人能够进入方格 [35, 37] ，因为3+5+3+7=18。但它不能进入方格 [35, 38]，因为3+5+3+8=19。
     * 请问该机器人能够到达多少个格子？
     * <p>
     * 示例 1：
     * 输入：m = 2, n = 3, k = 1
     * 输出：3
     * <p>
     * 示例 2：
     * 输入：m = 3, n = 1, k = 0
     * 输出：1
     * <p>
     * 提示：
     * 1 <= n,m <= 100
     * 0 <= k<= 20
     */
    @Test
    public void test202106011132() {
//        System.out.println(calDigitSum(15));
        System.out.println(movingCount(16, 8, 4));
    }

    public int movingCount(int m, int n, int k) {
        int count = 1;
        int[][] arr = new int[m][n];
        arr[0][0] = 1;
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (calDigitSum(i) + calDigitSum(j) > k) {
                    continue;
                }

                if (i > 0 && arr[i - 1][j] == 1) {

                } else if (j > 0 && arr[i][j - 1] == 1) {

                } else {
                    continue;
                }
                arr[i][j] = 1;
                count++;
            }
        }
        return count;
    }

    public int calDigitSum(int num) {
        int res = 0;
        while (num != 0) {
            res += num % 10;
            num /= 10;
        }
        return res;
    }


    /**
     * 51. N 皇后
     * n皇后问题 研究的是如何将 n个皇后放置在 n×n 的棋盘上，并且使皇后彼此之间不能相互攻击。
     * <p>
     * 给你一个整数 n ，返回所有不同的n皇后问题 的解决方案。
     * 每一种解法包含一个不同的n 皇后问题 的棋子放置方案，该方案中 'Q' 和 '.' 分别代表了皇后和空位。
     * <p>
     * 分析：
     * n个皇后一定是在不同的行，即棋盘的每行都有且只有一个皇后Q
     * 遍历每行的每个格子arr[row][j]，使得该格子满足当前棋盘中的所有皇后不相互攻击
     * （即该格子正上方，左上方和右上方没有Q，由于下一行还没有放置皇后，故不用考虑下方），如果不满足则跳过，
     * 如果满足则令arr[row][j]='Q'，继续在下一行合适的位置放置皇后
     * 直到最后一行row==n
     */
    @Test
    public void test202106021217() {

        System.out.println(solveNQueens(20));
    }

    public List<List<String>> solveNQueens(int n) {
        List<List<String>> res = new ArrayList<>();
        char[][] arr = new char[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                arr[i][j] = '.';
            }
        }
        solveNQueens(arr, res, n, 0);
        return res;
    }

    public void solveNQueens(char[][] arr, List<List<String>> res, int n, int row) {
        if (row == n) {
            res.add(getOneResByArr(arr, n));
            return;
        }
        for (int j = 0; j < n; j++) {
            if (!isValidByQueenRule(arr, row, j, n)) {
                continue;
            }
            arr[row][j] = 'Q';
            solveNQueens(arr, res, n, row + 1);
            arr[row][j] = '.';
        }
    }

    public List<String> getOneResByArr(char[][] arr, int n) {
        List<String> res = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            StringBuilder sb = new StringBuilder();
            for (int j = 0; j < n; j++) {
                sb.append(arr[i][j]);
            }
            res.add(sb.toString());
        }
        return res;
    }

    public boolean isValidByQueenRule(char[][] arr, int row, int col, int n) {
        //↑ col
        for (int i = 0; i <= row; i++) {
            if (arr[i][col] == 'Q') {
                return false;
            }
        }
        //↖
        for (int i = row - 1, j = col - 1; i >= 0 && j >= 0; i--, j--) {
            if (arr[i][j] == 'Q') {
                return false;
            }
        }

        //↗
        for (int i = row - 1, j = col + 1; i >= 0 && j < n; i--, j++) {
            if (arr[i][j] == 'Q') {
                return false;
            }
        }

        return true;
    }

    /**
     * 77. 组合
     * 给定两个整数 n 和 k，返回 1 ... n 中所有可能的 k 个数的组合。
     * <p>
     * 示例:
     * 输入:n = 4, k = 2
     * 输出:
     * [
     * [2,4],
     * [3,4],
     * [2,3],
     * [1,2],
     * [1,3],
     * [1,4],
     * ]
     * <p>
     * 分析：
     * 使用树形结构的递归，每一层level可选择的元素个数为k,k-1,k-2,...,1,
     * 可选择的数据范围为[begin,end],begin从0开始，end=arr.len
     * 递归过程：
     * 1 获取本层的元素范围，遍历该范围，使用path选取范围中的一个元素path.add(arr[index])
     * 2 缩小范围(index+1)，进入下一层
     * 3 停止条件path.size==k
     * 4 取消选取 path.remove(arr[index])
     */
    @Test
    public void test202106051647() {
        System.out.println(combine(1, 1));
    }

    public List<List<Integer>> combine(int n, int k) {
        List<List<Integer>> res = new ArrayList<>();
        if (k > n) {
            return res;
        }
        int[] arr = new int[n];
        for (int i = 0; i < n; i++) {
            arr[i] = i + 1;
        }
        if (k == n) {
            System.out.println(Arrays.stream(arr).boxed().collect(Collectors.toList()));
            res.add(Arrays.stream(arr).boxed().collect(Collectors.toList()));
            return res;
        }
        combine(k, new LinkedList<>(), res, arr, 0, n - 1);
        System.out.println(res.size());

        return res;
    }

    public void combine(int k, LinkedList<Integer> path, List<List<Integer>> res,
                        int[] arr, int begin, int end) {
        if (k == path.size()) {
            res.add(new ArrayList<>(path));
            return;
        }
        for (int i = begin; i <= end; i++) {
            path.add(arr[i]);
            //下一层
            combine(k, path, res, arr, i + 1, end);
            path.removeLast();
        }

    }

    /**
     * 组合2 给定一个有重复元素的数组和整数k，返回 数组中所有可能的 k 个数的组合。
     * 示例: 输入:{1,2,2,4}, k = 2 输出: [ [1,2], [1,4], [2,2], [2,4]]
     * <p>
     * 分析：
     * 在组合算法的基础上，每层去重：使用boolean[] visited记录每层的元素是否被选取
     */
    @Test
    public void test202106060902() {
        int[] arr = {1, 2, 2, 3, 3, 4};
        System.out.println(combineWithRepeat(arr, 2));
    }

    public List<List<Integer>> combineWithRepeat(int[] arr, int k) {
        List<List<Integer>> res = new ArrayList<>();
        if (k > arr.length) {
            return res;
        }
        if (k == arr.length) {
            res.add(Arrays.stream(arr).boxed().collect(Collectors.toList()));
            return res;
        }
        Arrays.sort(arr);
        combineWithRepeat(k, new LinkedList<>(), res, arr, 0, arr.length - 1, new boolean[arr.length]);
        System.out.println(res.size());

        return res;
    }

    public void combineWithRepeat(int k, LinkedList<Integer> path, List<List<Integer>> res,
                                  int[] arr, int begin, int end, boolean[] visited) {
        if (k == path.size()) {
            res.add(new ArrayList<>(path));
            return;
        }
        for (int i = begin; i <= end; i++) {
            if (visited[i] || (i > 0 && !visited[i - 1] && arr[i] == arr[i - 1])) {
                continue;
            }
            path.add(arr[i]);
            visited[i] = true;
            //下一层
            combineWithRepeat(k, path, res, arr, i + 1, end, visited);
            path.removeLast();
            visited[i] = false;
        }

    }

    /**
     * 39. 组合总和
     * 给定一个无重复元素的数组candidates和一个目标数target，找出candidates中所有可以使数字和为target的组合。
     * candidates中的数字可以无限制重复被选取。
     * <p>
     * 说明：
     * 所有数字（包括target）都是正整数。
     * 解集不能包含重复的组合。
     * 示例1：
     * 输入：candidates = [2,3,6,7], target = 7,
     * 所求解集为：
     * [
     * [7],
     * [2,2,3]
     * ]
     * <p>
     * 示例2：
     * 输入：candidates = [2,3,5], target = 8,
     * 所求解集为：
     * [
     * [2,2,2,2],
     * [2,3,3],
     * [3,5]
     * ]
     * <p>
     * 提示：
     * 1 <= candidates.length <= 30
     * 1 <= candidates[i] <= 200
     * candidate 中的每个元素都是独一无二的。
     * 1 <= target <= 500
     * <p>
     * 分析：
     * 由于数组每个数字可以重复使用，故组合成target的方式可以表示为
     * x0*arr[0] + x1*arr[1] + x2*arr[2] + ... + xi*arr[i] = target ,其中xi是>=0的整数,表示数组中每个数字出现的次数
     * 根据上述公式，可设计算法流程：
     * 1 做出选择：每一层为arr[i]，选取的次数xi，xi->[0,target/arr[i]], 执行xi次：path.add(arr[i])和target-=arr[i]
     * 2 i+1,进入下一层
     * 3 停止条件 target==0 || target<0 || index > end
     * 4 取消选择：执行xi次：path.remove(arr[i])和target+=arr[i]
     */
    @Test
    public void test202106061203() {
        int[] arr = {2, 3, 5};
        combinationSum(arr, 8);
    }

    public List<List<Integer>> combinationSum(int[] candidates, int target) {
        List<List<Integer>> res = new ArrayList<>();
        combinationSum(res, candidates, target, 0, candidates.length - 1, new LinkedList<>());
//        System.out.println(res);
        return res;
    }

    public void combinationSum(List<List<Integer>> res, int[] arr, int target, int index, int end,
                               LinkedList<Integer> path) {
        if (target == 0) {
            res.add(new ArrayList<>(path));
            return;
        } else if (target < 0) {
            return;
        }
        if (index > end) {
            return;
        }
        for (int i = 0; i <= target / arr[index]; i++) {
            //选择该元素i次
            target = addOrRemove(true, arr[index], i, path, target);
            //进入下一层
            combinationSum(res, arr, target, index + 1, end, path);

            //删除该元素i次
            target = addOrRemove(false, arr[index], i, path, target);
        }
    }

    public int addOrRemove(boolean isAdd, int num, int times, LinkedList<Integer> path, int target) {
        for (int i = 0; i < times; i++) {
            if (isAdd) {
                path.add(num);
                target -= num;
            } else {
                path.removeLast();
                target += num;
            }
        }
        return target;
    }

    /**
     * 40 组合总和2
     * 给定一个数组candidates和一个目标数target，找出candidates中所有可以使数字和为target的组合。
     * candidates中的每个数字在每个组合中只能使用一次。
     * 说明：
     * 所有数字（包括目标数）都是正整数。
     * 解集不能包含重复的组合。
     * <p>
     * 示例1:
     * 输入: candidates =[10,1,2,7,6,1,5], target =8,
     * 所求解集为:
     * [
     * [1, 7],
     * [1, 2, 5],
     * [2, 6],
     * [1, 1, 6]
     * ]
     * 示例2:
     * 输入: candidates =[2,5,2,1,2], target =5,
     * 所求解集为:
     * [
     * [1,2,2],
     * [5]
     * ]
     * <p>
     * 分析：
     * 和上题不同，由于每个数字只能使用一次，公式
     * x0*arr[0] + x1*arr[1] + x2*arr[2] + ... + xi*arr[i] = target ,其中xi是>=0的整数,表示数组中每个数字出现的次数
     * 每个元素可选择的次数xi->[0,1]，即转换为遍历每层的可选择列表
     * 1 做出选择：每层的可选择列表为[index,end] path.add(arr[index]) target -= arr[index] 同一层级需要去重
     * 2 index+1,进入下一层
     * 3 停止条件 target==0 || target<0 || index > end
     * 4 取消选择：path.remove(arr[index]) target += arr[index]
     */
    @Test
    public void test202106091409() {
        int[] arr = {2, 5, 2, 1, 2};
        combinationSum2(arr, 5);
    }

    public List<List<Integer>> combinationSum2(int[] candidates, int target) {
        List<List<Integer>> res = new ArrayList<>();
        Arrays.sort(candidates);
        combinationSum2(res, new LinkedList<>(), candidates, 0, candidates.length - 1, target);
//        System.out.println(res);
        return res;
    }

    public static void combinationSum2(List<List<Integer>> res, LinkedList<Integer> path,
                                       int[] arr, int index, int end, int target) {
        if (target < 0) {
            return;
        }
        if (target == 0) {
            res.add(new ArrayList<>(path));
        }
        for (int i = index; i <= end; i++) {
//            if (visited[i] || (i > 0 && !visited[i - 1] && arr[i] == arr[i - 1])) {
//                continue;
//            }
            //数组的值是递增的 跳出循环
            if (arr[i] > target) {
                break;
            }
            if ((i > index && arr[i] == arr[i - 1])) {
                continue;
            }
            path.add(arr[i]);
            target -= arr[i];
            combinationSum2(res, path, arr, i + 1, end, target);
            path.removeLast();
            target += arr[i];

        }

    }

    /**
     * 216. 组合总和3
     * 找出所有相加之和为n 的k个数的组合。组合中只允许含有 1 -9 的正整数，并且每种组合中不存在重复的数字。
     * 说明：
     * 所有数字都是正整数。
     * 解集不能包含重复的组合。
     * <p>
     * 示例 1:
     * 输入: k = 3, n = 7
     * 输出: [[1,2,4]]
     * 示例 2:
     * 输入: k = 3, n = 9
     * 输出: [[1,2,6], [1,3,5], [2,3,4]]
     * <p>
     * 分析：
     * 构造不重复的数组arr
     * 1 做出选择：每层的可选择列表为[index,end] path.add(arr[i]) n-=arr[i]
     * 2 index+1,进入下一层
     * 3 停止条件：n==0
     * 4 取消选择：path.remove(arr[i]) n+=arr[i]
     */
    @Test
    public void test202106091622() {
        combinationSum3(3,9);
    }

    public List<List<Integer>> combinationSum3(int k, int n) {
        List<List<Integer>> res = new ArrayList<>();
        int[]arr=new int[9];
        for(int i=0;i<9;i++){
            arr[i]=i+1;
        }
        combinationSum3(res,new LinkedList<>(),arr,n,0,arr.length-1,k);
//        System.out.println(res);
        return res;
    }

    public void combinationSum3(List<List<Integer>> res, LinkedList<Integer> path, int[] arr, int n,
                                int index, int end,int k) {
        if (n == 0) {
            if (path.size()==k)
            res.add(new ArrayList<>(path));
            return;
        }
        for (int i = index; i <= end; i++) {
            if (arr[i] > n) {
                break;
            }
            path.add(arr[i]);
            n -= arr[i];
            combinationSum3(res, path, arr, n, i + 1, end,k);
            path.removeLast();
            n += arr[i];
        }
    }


}
