package com.hyj.algorithm.demo;

import org.junit.Test;

import java.util.*;

public class TmpTest {
    private Map<Integer, Integer> indexMap;
    int[] preorder;
    HashMap<Integer, Integer> dic = new HashMap<>();

    public TreeNode buildTree(int[] preorder, int[] inorder) {
        this.preorder = preorder;
        for (int i = 0; i < inorder.length; i++)
            dic.put(inorder[i], i);
        return recur(0, 0, inorder.length - 1);
    }

    TreeNode recur(int root, int left, int right) {
        if (left > right) return null;                          // 递归终止
        TreeNode node = new TreeNode(preorder[root]);          // 建立根节点
        int i = dic.get(preorder[root]);                       // 划分根节点、左子树、右子树
        node.left = recur(root + 1, left, i - 1);              // 开启左子树递归
        node.right = recur(root + i - left + 1, i + 1, right); // 开启右子树递归
        return node;                                           // 回溯返回根节点
    }

    public static String replaceSpace(String s) {
        StringBuilder res = new StringBuilder();
        char space = ' ';
        for (char ch : s.toCharArray()) {
            if (ch == space) {
                res.append("%20");
            } else {
                res.append(ch);

            }
        }
        return res.toString();
    }

    public static void main(String[] args) {
        TmpTest test = new TmpTest();
        int[] preorder = {3, 9, 20, 15, 7};
        int[] inorder = {9, 3, 15, 20, 7};
//        TreeNode node = test.buildTree(preorder, inorder);
//        System.out.println(node.val);
        String s = "We are happy.";
        System.out.println(replaceSpace(s));
    }

    public int[] reversePrint(ListNode head) {
        if (head == null) {
            return new int[0];
        }
        LinkedList<Integer> list = new LinkedList<>();
        ListNode tmp = head;
        while (tmp != null) {
            list.add(tmp.val);
            tmp = tmp.next;
        }
        int size = list.size();
        int[] arr = new int[size];
        for (int i = 0; i < size; i++) {
            arr[i] = list.pop();
        }
        return arr;
    }


    @Test
    public void test1() {
        Deque deque = new ArrayDeque();
        LinkedList list = new LinkedList();
        for (int i = 0; i < 10; i++) {
            list.add(i);
        }
        for (int i = 0; i < 10; i++) {
            System.out.println(list.pop());
        }
    }

    public int minArray(int[] numbers) {
        int i = 0;
        int j = numbers.length - 1;
        while (i < j) {
            int m = (i + j) / 2;
            if (numbers[m] < numbers[j]) {
                j = m;
            } else if (numbers[m] > numbers[j]) {
                i = m + 1;

            } else {
                j--;
            }
        }
        return numbers[i];
    }

    public int search(int[] nums, int target) {
        if (nums.length == 0) {
            return -1;
        }
        int i = 0;
        int j = nums.length - 1;
        while (i < j) {
            int mid = (j + i) / 2;
            if (nums[mid] == target) {
                return mid;
            } else if (nums[mid] < target) {
                i = mid + 1;
            } else {
                j = mid - 1;
            }
        }
        if (nums[i] == target) {
            return i;
        } else if (nums[i] < target) {
            return i + 1;
        } else {
            if (i == 0) {
                return 0;
            }
            return i - 1;
        }
    }

    public int search2(int[] nums, int target) {

        return search(nums, target, 0, nums.length - 1);
    }

    public int search(int[] nums, int target, int left, int right) {
        if (left > right) {
            return -1;
        }
        int mid = (left + right) / 2;
        if (nums[mid] == target) {
            return mid;
        } else if (nums[mid] < target) {
            return search(nums, target, mid + 1, right);
        } else {
            return search(nums, target, left, mid - 1);
        }
    }

    @Test
    public void test202105222029() {

    }

//    public boolean exist(char[][] board, String word) {
//        if (word == null || word.length() == 0 || board == null || board.length == 0) {
//            return false;
//        }
//        int m = board.length;
//        int n = board[0].length;
//        if (n == 0) {
//            return false;
//        }
//        //遍历每行 以每行每个元素为起点，开始寻找=word[0]的字符，如果找到则向右、向左和向下找下一个
//        for (int i = 0; i < m; i++) {
//            for (int j = 0; j < n; j++) {
//                if(board[i][j]==word.charAt(0)){
//
//                }
//            }
//        }
//
//        return false;
//    }

    public boolean exist(char[][] board, String word) {
        char[] words = word.toCharArray();
        for(int i = 0; i < board.length; i++) {
            for(int j = 0; j < board[0].length; j++) {
                if(dfs(board, words, i, j, 0)) return true;
            }
        }
        return false;
    }
    boolean dfs(char[][] board, char[] word, int i, int j, int k) {
        if(i >= board.length || i < 0 || j >= board[0].length || j < 0 || board[i][j] != word[k]) return false;
        if(k == word.length - 1) return true;
        board[i][j] = '\0';
        boolean res = dfs(board, word, i + 1, j, k + 1) || dfs(board, word, i - 1, j, k + 1) ||
                dfs(board, word, i, j + 1, k + 1) || dfs(board, word, i , j - 1, k + 1);
        board[i][j] = word[k];
        return res;
    }


}

class ListNode {
    int val;
    ListNode next;

    ListNode(int x) {
        val = x;
    }
}

class TreeNode {
    int val;
    TreeNode left;
    TreeNode right;

    TreeNode(int x) {
        val = x;
    }
}

