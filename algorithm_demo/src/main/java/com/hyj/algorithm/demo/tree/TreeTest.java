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
        TreeNode treeNode = new TreeNode(arr[0]);
        build(arr, treeNode, 1);
        printLevelTreeNode(treeNode);
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
        Integer[] a = {1,2,3,4,5,6,7};
        int i=1;
        TreeNode root = new TreeNode(a[0]);  // 根节点
        TreeNode current = null;
        Integer value = null;

        //层序创建二叉树
        LinkedList<TreeNode> queue = new LinkedList<TreeNode>();
        queue.offer(root);
        while(i<a.length) {
            current = queue.poll();//从链表中移除并获取第一个节点
            value = a[i++];
            if(value!=null) {
                TreeNode left =new TreeNode(value);
                current.left=(left);//创建当前节点的左孩子
                queue.offer(left); // 在链表尾部 左孩子入队
            }
            value=a[i++];
            if(value!=null) {
                TreeNode right =new TreeNode(value);
                current.right=(right);//创建当前节点的右孩子
                queue.offer(right);// 在链表尾部 右孩子入队
            }

        }

        List<List<Integer>> lists = levelOrder2(root);
        System.out.println(lists);

    }

    public int build(Integer[] arr, TreeNode root, int i) {
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
        TreeNode treeNode = new TreeNode(arr[0]);
        build(arr, treeNode, 1);
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
}
