package com.hyj.algorithm.demo.tree;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

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
        Integer[] arr = {1, null, 0, 0, 1};
        TreeNode treeNode = build(arr,  new TreeNode(arr[0]));
        System.out.println(treeNode);
    }
    private int i=1;

    public TreeNode build(Integer[] arr,  TreeNode root) {
        if(root==null){
            return null;
        }
        if (i >= arr.length) {
            return root;
        }
        if(arr[i]!=null){
            root.left=new TreeNode(arr[i]);
        }
        i++;
        if (i >= arr.length) {
            return root;
        }
        if(arr[i]!=null){
            root.right=new TreeNode(arr[i]);
        }
        i++;
        build(arr,root.left);
        build(arr,root.right);


        return root;
    }

    public void printTree(TreeNode root,List<Integer> list){
        if(root!=null){
            list.add(root.val);
            if(root.left!=null){
                list.add(root.left.val);
            }
            if(root.right!=null){
                list.add(root.right.val);
            }
            if(root.left!=null)
            printTree(root.left.left,list);
            printTree(root.right,list);
        }

    }
}
