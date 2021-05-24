package com.hyj.algorithm.demo;

import org.junit.Test;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * 二叉树
 */
public class BinaryTree {
    static class TreeNode {
        int val;
        TreeNode left;
        TreeNode right;

        TreeNode(int x) {
            val = x;
        }
    }

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

    /**
     * 构建二叉树并遍历
     */
    @Test
    public void test1() {
        LinkedList list = new LinkedList<>();
        for (int i = 0; i < 16; i++) {
            list.add(i);
        }
        System.out.println(list);

        int[] arrays = {2, 3, 1, 4, 5};
        int[] arr = {6, 4, 8, 3, 5, 7, 9};
        TreeNode treeNode = null;
        for (int i : arr) {
            treeNode = addRecursive(treeNode, i);
        }
//        frontOrder(treeNode);
        System.out.println("--------------");
//        middleOrder(treeNode);
        levelOrder(treeNode);

    }


    private TreeNode addRecursive(TreeNode current, int value) {
        if (current == null) {
            return new TreeNode(value);
        }

        if (value < current.val) {
            current.left = addRecursive(current.left, value);
        } else if (value > current.val) {
            current.right = addRecursive(current.right, value);
        } else {
            // value already exists
            return current;
        }

        return current;
    }

    /**
     * 前序遍历就是先访问根节点，再访问左节点，最后访问右节点
     *
     * @param root
     */
    public static void frontOrder(TreeNode root) {
        if (root == null) {
            return;
        }
        System.out.println(root.val);
        frontOrder(root.left);
        frontOrder(root.right);

    }

    /**
     * 中序遍历就是先访问左节点，再访问根节点，最后访问右节点
     * @param root
     */
    public static void middleOrder(TreeNode root){
        if (root == null) {
            return;
        }
        middleOrder(root.left);
        System.out.println(root.val);
        middleOrder(root.right);
    }

    /**
     * 先访问左节点，再访问右节点，最后访问根节点
     * @param root
     */
    public static void backOrder(TreeNode root){
        if (root == null) {
            return;
        }
        backOrder(root.left);
        backOrder(root.right);
        System.out.println(root.val);
    }

    /**
     * 层序遍历
     * @param root
     */
    public static void levelOrder(TreeNode root) {
        LinkedList<TreeNode> queue = new LinkedList<>();
        queue.add(root);
        while (!queue.isEmpty()) {
            root = queue.pop();
            System.out.print(root.val + " ");
            if (root.left != null) queue.add(root.left);
            if (root.right != null) queue.add(root.right);
        }
    }

    /**
     * 剑指 Offer 07. 重建二叉树
     * 输入某二叉树的前序遍历和中序遍历的结果，请重建该二叉树。假设输入的前序遍历和中序遍历的结果中都不含重复的数字。
     * <p>
     * 例如，给出
     * 前序遍历 preorder =[3,9,20,15,7]
     * 中序遍历 inorder = [9,3,15,20,7]
     * 返回如下的二叉树：
     *  3
     * / \
     * 9  20
         /  \
        15   7
     * <p>
     * 分析：
     * 前序：根 左 右
     * 中序：左 根 右
     * 每个子树都有若干子树（叶子节点也可以看作子树），每个节点都可以看作子树的根节点，
     * 重建过程是递归创建子树根节点的过程
     *
     * 1 找出根节点：根节点为前序遍历的第一个元素
     * 2 创建根节点：假设根节点在前序和中序的下标分别为root_pre root_in，
     * 则左子树的前序根节点为root_pre+1，右子树的前序根节点为root_pre+1+left_len
     * 3 创建左右子树：利用中序计算左子树的长度left_len=root_in-left，在前序中找出左右子树的根节点
     * 左子树的中序右边界为root_in-1，右子树的中序左边界为root_in+1
     * 4 重复上述过程
     */
    @Test
    public void test2() {
        int[] preorder = {3, 9, 20, 15, 7};
        int[] inorder = {9, 3, 15, 20, 7};
        TreeNode treeNode = buildTree(preorder, inorder);
        System.out.println(treeNode);
    }

    /**
     * @param preorder
     * @param inorder
     * @return
     */
    public TreeNode buildTree(int[] preorder, int[] inorder) {
        //
        Map<Integer, Integer> inMap=new HashMap<>();
        for (int i = 0; i < inorder.length; i++) {
            inMap.put(inorder[i],i);
        }
        return buildTree(preorder,inMap,0,0,inorder.length-1);
    }
    /**
     * 输入一棵二叉树的根节点，判断该树是不是平衡二叉树。如果某二叉树中任意节点的左右子树的深度相差不超过1，
     * 那么它就是一棵平衡二叉树。
     * <p>
     * 示例 1:
     * 给定二叉树 [3,9,20,null,null,15,7]
     * 3
     * / \
     * 9  20
     * /  \
     * 15   7
     * 返回 true 。
     * <p>
     * 示例 2:
     * 给定二叉树 [1,2,2,3,3,null,null,4,4]
     * 1
     * / \
     * 2   2
     * / \
     * 3   3
     * / \
     * 4   4
     * 返回false
     *
     * 分析：
     * 1 当前根节点的高度=max(左子树高度,右子树高度)+1
     * 2 从根节点开始计算左右子树的高度，子树高度=max(左子树高度,右子树高度)+1，如果左右子树高度差大于1则返回-1
     * 如此递归1 2，则可以从底至上，计算出每颗子树的高度了level，如果level!=-1则为平衡二叉树
     */
    @Test
    public void test202105241237() {

    }

    /**
     * 计算当前节点左右子树是否平衡
     * @param root
     * @return
     */
    public boolean isBalanced(TreeNode root) {
//        if (root == null) {
//            return true;
//        }
//        //计算当前根节点左右子树的高度
//        int left = countLevel(root.left, 0);
//        int right = countLevel(root.right, 0);
//        if (Math.abs(left - right) > 1) {
//            return false;
//        }
//        //递归判断当前节点左右子树的根节点是否平衡
//        boolean l=isBalanced(root.left);
//        boolean r=isBalanced(root.right);
//        if(!l||!r){
//            return false;
//        }

        return countLevel(root,0)!=-1;
    }

    /**
     * 计算子树的高度
     * @param node
     * @param count
     * @return
     */
    public int countLevel(TreeNode node, int count) {
        if(node==null){
            return count;
        }
        count++;
        int leftCount=countLevel(node.left,count);
        if(leftCount==-1){
            return -1;
        }
        int rightCount=countLevel(node.right,count);
        if(rightCount==-1){
            return -1;
        }
        if(Math.abs(leftCount-rightCount)>1){
            return -1;
        }

        return Math.max(leftCount,rightCount);
    }

    /**
     * 根据前序和中序重建二叉树
     * 注意left_in和right_in只是递归的出口，并不会决定根节点的位置
     * @param preorder 前序数组
     * @param inMap 中序：元素值-数组下标
     * @param root_pre 前序的根节点下标
     * @param left_in 中序的左边界
     * @param right_in 中序的右边界
     * @return
     */
    public TreeNode buildTree(int[] preorder, Map<Integer, Integer> inMap, int root_pre, int left_in, int right_in) {
        if(left_in>right_in){
            return null;
        }
        TreeNode root=new TreeNode(preorder[root_pre]);
        //根节点的中序下标
        int root_in=inMap.get(preorder[root_pre]);
        //左子树长度
        int left_len=root_in-left_in;
        root.left=buildTree(preorder,inMap,root_pre+1,left_in,root_in-1);
        root.right=buildTree(preorder,inMap,root_pre+1+left_len,root_in+1,right_in);

        return root;
    }
}
