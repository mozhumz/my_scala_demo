package com.hyj.algorithm.demo.tree;

import org.junit.Test;

public class TreeTest {
      class TreeNode {
        int val;
        TreeNode left;
        TreeNode right;
        TreeNode() {}
        TreeNode(int val) { this.val = val; }
        TreeNode(int val, TreeNode left, TreeNode right) {
            this.val = val;
            this.left = left;
             this.right = right;
         }
     }

    /**
     * 给定一个二叉树 根节点 root ，树的每个节点的值要么是 0，要么是 1。请剪除该二叉树中所有节点的值为 0 的子树。
     *
     * 节点 node 的子树为 node 本身，以及所有 node 的后代。
     *
     * 输入: [1,null,0,0,1]
     * 输出: [1,null,0,null,1]
     * 解释:
     * 只有红色节点满足条件“所有不包含 1 的子树”。
     *
     * 输入: [1,0,1,0,0,0,1]
     * 输出: [1,null,1,null,1]
     *
     * 输入: [1,1,0,1,1,0,1,0]
     * 输出: [1,1,0,1,1,null,1]
     *
     * 提示:
     *
     * 二叉树的节点个数的范围是 [1,200]
     * 二叉树节点的值只会是 0 或 1
     *
     *
     */
    @Test
    public void testPruneTree(){

     }

    public TreeNode pruneTree(TreeNode root) {
          if(root==null){
              return null;
          }
          if(root.left!=null){
              root.left=pruneTree(root.left);    
          }
          if(root.right!=null){
              root.right=pruneTree(root.right);
          }
        if(root.val==0 && root.left==null&&root.right==null){
              return null;
          }
          
        return root;
    }
}
