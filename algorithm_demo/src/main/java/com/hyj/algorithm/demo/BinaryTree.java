package com.hyj.algorithm.demo;

import org.junit.Test;

/**
 * 二叉树
 */
public class BinaryTree {
    /**
     * 给定一个二叉树，判断其是否是一个有效的二叉搜索树。
     * <p>
     * 假设一个二叉搜索树具有如下特征：
     * 节点的左子树只包含小于当前节点的数。
     * 节点的右子树只包含大于当前节点的数。
     * 所有左子树和右子树自身必须也是二叉搜索树
     * <p>
     * 输入:
     * 2
     * / \
     * 1   3
     * 输出: true
     * <p>
     * 输入:
     * 5
     * / \
     * 1   4
     * / \
     * 3   6
     * 输出: false
     */
    @Test
    public void validateBinarySearchTree() {

    }

    class TreeNode {
        int val;
        TreeNode left;
        TreeNode right;

        TreeNode(int x) {
            val = x;
        }
    }

    /**
     * 分析：根据二叉搜索树的性质，所有左子树节点的值都小于根节点的值，根节点的值大于所有右节点的值
     * 则根节点的值在一个开区间内（min,max），则可以构造递归函数helper(root,min,max)，该函数满足条件：
     * root.val>min.val and root.val<max.val
     * 因为所有左子节点小于根节点，所以下次递归的左函数为helper(root.left,min,root)
     * 因为所有右子节点大于根节点，所以下次递归的右函数为helper(root.right,root,max)
     *
     * @param node
     * @return
     */
    public boolean validateBinarySearchTree(TreeNode node) {
        return validateBinarySearchTree(node, null, null);
    }

    public boolean validateBinarySearchTree(TreeNode node, TreeNode min, TreeNode max) {
        if (node == null) {
            return true;
        }
        if (min != null && node.val <= min.val) {
            return false;
        }
        if (max != null && node.val >= max.val) {
            return false;
        }
        return validateBinarySearchTree(node.left, min, node)
                && validateBinarySearchTree(node.right, node, max);
    }

}
