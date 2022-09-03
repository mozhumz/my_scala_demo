package com.hyj.algorithm.demo.tree;

import org.junit.Test;

import java.util.*;

public class TreeTest {
    class TreeNode {
        int val;
        TreeNode left;
        TreeNode right;

        TreeNode() {
        }

        TreeNode(int val) {
            this.val = val;
        }

        TreeNode(int val, TreeNode left, TreeNode right) {
            this.val = val;
            this.left = left;
            this.right = right;
        }
    }

    /**
     * 给定一个二叉树 根节点 root ，树的每个节点的值要么是 0，要么是 1。请剪除该二叉树中所有节点的值为 0 的子树。
     * <p>
     * 节点 node 的子树为 node 本身，以及所有 node 的后代。
     * <p>
     * 输入: [1,null,0,0,1]
     * 输出: [1,null,0,null,1]
     * 解释:
     * 只有红色节点满足条件“所有不包含 1 的子树”。
     * <p>
     * 输入: [1,0,1,0,0,0,1]
     * 输出: [1,null,1,null,1]
     * <p>
     * 输入: [1,1,0,1,1,0,1,0]
     * 输出: [1,1,0,1,1,null,1]
     * <p>
     * 提示:
     * <p>
     * 二叉树的节点个数的范围是 [1,200]
     * 二叉树节点的值只会是 0 或 1
     */
    @Test
    public void testPruneTree() {

    }

    public TreeNode pruneTree(TreeNode root) {
        if (root == null) {
            return null;
        }
        if (root.left != null) {
            root.left = pruneTree(root.left);
        }
        if (root.right != null) {
            root.right = pruneTree(root.right);
        }
        if (root.val == 0 && root.left == null && root.right == null) {
            return null;
        }

        return root;
    }

    @Test
    public void testBuildTree() {
        Integer[] arr = {3, 9, 20, null, null, 15, 7};
        TreeNode treeNode = getTreeNode(arr);
        printLevelTreeNode(treeNode);
    }

    private  TreeNode getTreeNode(Integer[] arr) {
        TreeNode treeNode = new TreeNode(arr[0]);
        build(arr, treeNode, 1);
        return treeNode;
    }

    public void printLevelTreeNode(TreeNode root) {
        List<Integer> res = new ArrayList<>();
        LinkedList<TreeNode> list = new LinkedList();
        list.add(root);
        while (!list.isEmpty()) {
            TreeNode node = list.poll();
            res.add(node.val);
            if (node.left != null) {
                list.add(node.left);
            }
            if (node.right != null) {
                list.add(node.right);
            }
        }
        System.out.println(res);

    }

    @Test
    public void build() {
        Integer[] a = {1, 2, 3, 4, 5, 6, 7};
        int i = 1;
        TreeNode root = new TreeNode(a[0]);  // 根节点
        TreeNode current = null;
        Integer value = null;

        //层序创建二叉树
        LinkedList<TreeNode> queue = new LinkedList<TreeNode>();
        queue.offer(root);
        while (i < a.length) {
            current = queue.poll();//从链表中移除并获取第一个节点
            value = a[i++];
            if (value != null) {
                TreeNode left = new TreeNode(value);
                current.left = (left);//创建当前节点的左孩子
                queue.offer(left); // 在链表尾部 左孩子入队
            }
            value = a[i++];
            if (value != null) {
                TreeNode right = new TreeNode(value);
                current.right = (right);//创建当前节点的右孩子
                queue.offer(right);// 在链表尾部 右孩子入队
            }

        }

        List<List<Integer>> lists = levelOrder2(root);
        System.out.println(lists);

    }

    public  int build(Integer[] arr, TreeNode root, int i) {
        if (root == null) {
            return i;
        }
        if (i >= arr.length) {
            return i;
        }
        if (arr[i] != null) {
            root.left = new TreeNode(arr[i]);
        }
        i++;
        if (i >= arr.length) {
            return i;
        }
        if (arr[i] != null) {
            root.right = new TreeNode(arr[i]);
        }
        i++;
        i = build(arr, root.left, i);
        i = build(arr, root.right, i);


        return i;
    }


    @Test
    public void testLevelOrder() {
        Integer[] arr = {1, 2, 3, 4, 5, 6, 7};
        TreeNode treeNode = getTreeNode(arr);
        List<List<Integer>> lists = levelOrder(treeNode);
        System.out.println(lists);
    }

    public List<List<Integer>> levelOrder2(TreeNode root) {
        List<List<Integer>> res = new ArrayList<>();

        Queue<TreeNode> queue = new ArrayDeque<>();
        if (root != null) {
            queue.add(root);
        }
        while (!queue.isEmpty()) {
            int n = queue.size();
            List<Integer> level = new ArrayList<>();
            for (int i = 0; i < n; i++) {
                TreeNode node = queue.poll();
                level.add(node.val);
                if (node.left != null) {
                    queue.add(node.left);
                }
                if (node.right != null) {
                    queue.add(node.right);
                }
            }
            res.add(level);
        }

        return res;
    }


    public List<List<Integer>> levelOrder(TreeNode root) {
        List<List<Integer>> list = new ArrayList<>();
        if (root == null) {
            return list;
        }
        LinkedList<TreeNode> lk = new LinkedList<>();
        lk.add(root);
        Map<TreeNode, Integer> levelMap = new HashMap<>();
        levelMap.put(root, 1);
        List<Integer> intList = new ArrayList<>();
        List<TreeNode> tmpList = new ArrayList<>();
        while (!lk.isEmpty()) {
            TreeNode node = lk.poll();
            int level = levelMap.getOrDefault(node, 0);
            if (tmpList.isEmpty() || levelMap.getOrDefault(tmpList.get(0), 0) == level) {
                intList.add(node.val);
                tmpList.add(node);
            } else {
                list.add(intList);
                intList = new ArrayList<>();
                intList.add(node.val);
                tmpList = new ArrayList<>();
                tmpList.add(node);
            }
            if (node.left != null) {
                lk.add(node.left);
                levelMap.put(node.left, level + 1);
            }
            if (node.right != null) {
                lk.add(node.right);
                levelMap.put(node.right, level + 1);
            }
        }
        list.add(intList);

        return list;
    }

    /**
     * LCP 34. 二叉树染色
     * <p>
     * 小扣有一个根结点为 root 的二叉树模型，初始所有结点均为白色，可以用蓝色染料给模型结点染色，
     * 模型的每个结点有一个 val 价值。小扣出于美观考虑，
     * 希望最后二叉树上每个蓝色相连部分的结点个数不能超过 k 个，求所有染成蓝色的结点价值总和最大是多少？
     * <p>
     * 示例 1：
     * 输入：root = [5,2,3,4], k = 2
     * 输出：12
     * 解释：结点 5、3、4 染成蓝色，获得最大的价值 5+3+4=12
     * <p>
     * 示例 2：
     * 输入：root = [4,1,3,9,null,null,2], k = 2
     * 输出：16
     * 解释：结点 4、3、9 染成蓝色，获得最大的价值 4+3+9=16
     * <p>
     * 题解：使用树形DP自底向上计算价值总和
     * 定义dp[i]（i<=k），表示以root为根节点时，染色i个节点时的最大价值总和，则ans=max(dp[i])
     * root为根节点进行染色时，分为2种情况：
     * 1）root不染色，即dp[0]，则答案为左右子树最大值之和，ans=dp[0]=left_max(dp[i])+right_max(dp[i])
     * 2）root染色，则左子树染色j个，右子树最多染色i-1-j个，答案为左子树最多染色i个的最大值+右子树最多染色k-1-i个的最大值+根节点的值
     * ans=root.val+left_dp[j]+right_dp[i-1-j]
     */
    @Test
    public void test() {
        Integer[] arr = {4,1,3,9,null,null,2};
        TreeNode treeNode = getTreeNode(arr);
        System.out.println(maxValue(treeNode,2));
    }

    public int maxValue(TreeNode root, int k) {
        int[]dp=dp(root,k);
        System.out.println("maxValue-dp:"+Arrays.toString(dp));
        int ans=Integer.MIN_VALUE;
        for (int i = 0; i <= k; i++) {
            ans=Math.max(dp[i],ans);
        }
        return ans;
    }

    public int[] dp(TreeNode root, int k) {
        int[] dp = new int[k + 1];
        if (root == null) {
            return dp;
        }
        // 获取左、右子树染色状态的dp表
        int[] l = dp(root.left, k);
        int[] r = dp(root.right, k);
        // root不染色
        int ml = Integer.MIN_VALUE;
        int mr = Integer.MIN_VALUE;
        for (int i = 0; i <= k; i++) {
            ml = Math.max(ml, l[i]);
            mr = Math.max(mr, r[i]);
        }
        dp[0] = ml + mr;
        // root染色
        for (int i = 0; i <= k; i++) {
            for (int j = 0; j < i; j++) {
                // dp[i]表示最多染色i个时的最大价值总和
                // 还需要染色 i - 1 个点，左子树 j 个，右子树 i-1-j 个
                dp[i] = Math.max(dp[i],  root.val + l[j] + r[i - 1 - j]);
            }
        }
        return dp;
    }


}
