package com.hyj.algorithm.demo;

import org.junit.Test;

import java.util.*;

public class CommonAlgorithm {

    /**
     * 给定一个整数数组 nums 和一个目标值 target，请你在该数组中找出和为目标值的那 两个 整数，并返回他们的数组下标。
     * 你可以假设每种输入只会对应一个答案。但是，数组中同一个元素不能使用两遍。
     *  
     * 示例:
     * 给定 nums = [2, 7, 11, 15], target = 9
     * 因为 nums[0] + nums[1] = 2 + 7 = 9
     * 所以返回 [0, 1]
     */
    @Test
    public void twoSum() {
        int[] arr = {3, 3, 1, 1, 5, 5};
        int target = 6;
        //每个元素进行登记
        Map<Integer, List<Integer>> map = new HashMap<>();
        for (int i = 0; i < arr.length; i++) {
            //每个元素的相反数为key value=元素下标
            Integer key = target - arr[i];
            List<Integer> list = map.get(key);
            if (list == null) {
                list = new ArrayList<>();
            }
            list.add(i);
            map.put(key, list);
        }
        ArrayList<Integer> list = new ArrayList<>();

        for (int i = 0; i < arr.length; i++) {
            if (list.contains(i)) {
                continue;
            }
            if (map.get(arr[i]) != null) {
                if (map.get(arr[i]).size() == 1 && map.get(arr[i]).contains(i)) continue;
                //添加相反数的下标
                list.addAll(map.get(arr[i]));
                //添加所有等值元素的下标
                if (arr[i] != arr[map.get(arr[i]).get(0)]) {
                    list.addAll(map.get(arr[map.get(arr[i]).get(0)]));
                }
            }
        }
        int[] res = new int[list.size()];
        for (int i = 0; i < list.size(); i++) {
            res[i] = list.get(i);
        }
//        System.out.println(list);
        twoSum(arr, 6);

    }

    public int[] twoSum(int[] nums, int target) {
        Map<Integer, List<Integer>> map = new HashMap<>();
        List<Integer> res = new ArrayList<>();
        for (int i = 0; i < nums.length; i++) {
            if (res.contains(i)) continue;
            if (map.containsKey(target - nums[i])) {
                res.add(i);
                if (res.contains(map.get(target - nums[i]).get(0))) continue;
                res.addAll(map.get(target - nums[i]));
                continue;
            }
            List<Integer> list = map.get(nums[i]);
            if (list == null) {
                list = new ArrayList<>();
            }
            list.add(i);
            map.put(nums[i], list);
        }
        System.out.println(res);
        return new int[]{-1, -1};
    }


    /**
     * 给你一个包含 n 个整数的数组 nums,判断 nums 中是否存在三个元素 a,b,c ,
     * 使得 a + b + c = 0 ？找出所有满足条件且不重复的三元组
     * 例如, 给定数组 nums = [-1, 0, 1, 2, -1, -4]，
     * * 满足要求的三元组集合为：
     * [
     * [-1, 0, 1],
     * [-1, -1, 2]
     * ]
     * <p>
     * -4 -1 -1 0 1 2
     *
     * @return
     */
    @Test
    public void threeSum() {
        int[] arr = {0, 0, 0};
        List<List<Integer>> res = new ArrayList<>();
        Arrays.sort(arr);
        for (int i = 0; i < arr.length; i++) {
            if (i > 0 && arr[i] == arr[i - 1]) {
                continue;
            }
            int j = i + 1;
            int k = arr.length - 1;
            while (k > j) {

                int sum = arr[i] + arr[j] + arr[k];
                if (sum == 0) {
                    List<Integer> list = new ArrayList<>();
                    list.add(arr[i]);
                    list.add(arr[j]);
                    list.add(arr[k]);
                    res.add(list);
                    j++;
                    k--;
                    while (j < arr.length && arr[j] == arr[j - 1]) {
                        j++;
                    }
                    while (k > j && arr[k] == arr[k + 1]) {
                        k--;
                    }
                } else if (sum > 0) {
                    k--;
                    while (k > j && arr[k] == arr[k + 1]) {
                        k--;
                    }
                } else {
                    j++;
                    while (j < arr.length && arr[j] == arr[j - 1]) {
                        j++;
                    }
                }
            }

        }
        System.out.println(res);
    }

    /**
     * 给出两个 非空 的链表用来表示两个非负的整数。其中，它们各自的位数是按照 逆序 的方式存储的，并且它们的每个节点只能存储 一位 数字。
     * 如果，我们将这两个数相加起来，则会返回一个新的链表来表示它们的和。
     * 您可以假设除了数字 0 之外，这两个数都不会以 0 开头。
     * 示例：
     * 输入：(2 -> 4 -> 3) + (5 -> 6 -> 4)
     * 输出：7 -> 0 -> 8
     * 原因：342 + 465 = 807
     */
    @Test
    public void addTwoNums() {


        ListNode l1 = new ListNode(9);
//        l1.next = new ListNode(4);
//        l1.next.next = new ListNode(3);

        ListNode l2 = new ListNode(1);
        l2.next = new ListNode(9);
        l2.next.next = new ListNode(9);
        l2.next.next.next = new ListNode(9);
        l2.next.next.next.next = new ListNode(9);
        l2.next.next.next.next.next = new ListNode(9);

        l2.next.next.next.next.next.next = new ListNode(9);
        l2.next.next.next.next.next.next.next = new ListNode(9);
        l2.next.next.next.next.next.next.next.next = new ListNode(9);
        l2.next.next.next.next.next.next.next.next.next = new ListNode(9);


        Map<Integer, Integer> map1 = new LinkedHashMap<>();
        setMap(map1, l1);
        Map<Integer, Integer> map2 = new LinkedHashMap<>();
        setMap(map2, l2);
        ListNode res = setLink(map1, map2);
        System.out.println();
//        ListNode res = setListNode(null, link);
    }

    private ListNode setListNode(ListNode listNode, List<Integer> link) {
        if (!link.isEmpty()) {
            ListNode tmp = null;
            for (int i = 0; i < link.size(); i++) {
                if (listNode == null) {
                    listNode = new ListNode(link.get(i));
                    tmp = listNode;
                } else {
                    tmp.next = new ListNode(link.get(i));
                    tmp = tmp.next;
                }
            }
        }
        return listNode;
    }

    private void setMap(Map<Integer, Integer> map, ListNode l1) {
        boolean flag = l1 != null;
        ListNode node = l1;
        int i = 1;
        while (flag) {
//            list1.add(node.val);
            map.put(i, node.val);
            i++;
            node = node.next;
            flag = node != null;
        }
    }

    private ListNode setLink(Map<Integer, Integer> map1, Map<Integer, Integer> map2) {
        ListNode listNode = null;
        //423 1->3 2->2 3->4 //9 1->9
        if (!map1.isEmpty() && !map2.isEmpty()) {
            int tmpAdd = 0;
            if (map1.size() > map2.size()) {
                listNode = addLink(map1, map2, tmpAdd);
            } else {
                listNode = addLink(map2, map1, tmpAdd);
            }
        }
        return listNode;
    }

    private ListNode addLink(Map<Integer, Integer> map1, Map<Integer, Integer> map2, int tmpAdd) {
        Integer n2;
        int n;
        ListNode tmp = null;
        ListNode listNode = null;
        for (Map.Entry<Integer, Integer> entry : map1.entrySet()) {
            n2 = map2.get(entry.getKey());
            if (n2 == null) {
                n2 = 0;
            }
            //同位相加
            n = entry.getValue() + n2 + tmpAdd;
            //进位数
            tmpAdd = n / 10;
            //新的数放入
//            link.add(n % 10);

            if (listNode == null) {
                listNode = new ListNode(n % 10);
                tmp = listNode;
            } else {
                tmp.next = new ListNode(n % 10);
                tmp = tmp.next;
            }

        }
        if (tmpAdd != 0) {
//            link.add(tmpAdd);
            tmp.next = new ListNode(tmpAdd);
        }
        return listNode;
    }


    class ListNode {
        int val;
        ListNode next;

        ListNode(int x) {
            val = x;
        }
    }


    /**
     * 给定一个字符串，请你找出其中不含有重复字符的 最长子串 的长度。
     * 示例 1:
     * 输入: "abcabcbb"
     * 输出: 3
     * 解释: 因为无重复字符的最长子串是 "abc"，所以其长度为 3。
     * 示例 2:
     * 输入: "bbbbb"
     * 输出: 1
     * 解释: 因为无重复字符的最长子串是 "b"，所以其长度为 1。
     * 示例 3:
     * 输入: "pwwkew"
     * 输出: 3
     * 解释: 因为无重复字符的最长子串是 "wke"，所以其长度为 3。
     *      请注意，你的答案必须是 子串 的长度，"pwke" 是一个子序列，不是子串。
     */
    @Test
    public void lengthOfLongestSubstring() {
        lengthOfLongestSubstring2("abcabcdde");
        String str = "abcaqwert";
        Map<Character, Integer> maxMap = new LinkedHashMap<>();
        maxMap = lengthOfLongestSubstring3(str, maxMap);
        System.out.println(maxMap);
    }

    private Map<Character, Integer> lengthOfLongestSubstring3(String str, Map<Character, Integer> maxMap) {
        Map<Character, Integer> map = new LinkedHashMap<>();
        int maxLen = 0;
        for (int i = 0; i < str.length(); i++) {
            for (int j = i; j < str.length(); j++) {
                if (map.containsKey(str.charAt(j))) {
                    break;
                }
                map.put(str.charAt(j), j);
            }
            if (map.size() > maxLen) {
                maxLen = map.size();
                maxMap = map;
            }
            map = new HashMap<>();
        }
        return maxMap;
    }


    public int lengthOfLongestSubstring2(String s) {
        int n = s.length(), ans = 0;
        //最长字符串-起始下标
        int s1 = 0;
        //最长字符串-结尾下标
        int s2 = 0;
        Map<Character, Integer> map = new HashMap<>();
        int start = 0;
        for (int end = 0; end < n; end++) {
            char alpha = s.charAt(end);
            if (map.containsKey(alpha)) {
                start = Math.max(map.get(alpha) + 1, start);
            }
            if (end - start + 1 > ans) {
                ans = end - start + 1;
                s1 = start;
                s2 = end;
            }
//            ans = Math.max(ans, end - start + 1);
            map.put(s.charAt(end), end);
        }
        return ans;
    }

    /**
     * 给出一个 32 位的有符号整数，你需要将这个整数中每位上的数字进行反转。
     * <p>
     * 示例 1:
     * <p>
     * 输入: 123
     * 输出: 321
     *  示例 2:
     * <p>
     * 输入: -123
     * 输出: -321
     * 示例 3:
     * <p>
     * 输入: 120
     * 输出: 21
     * 注意:
     * 假设我们的环境只能存储得下 32 位的有符号整数，则其数值范围为 [−231,  231 − 1]。
     * 请根据这个假设，如果反转后整数溢出那么就返回 0。
     */
    @Test
    public void reverse() {
        int x = -123;
        int res = 0;
        while (x != 0) {
            int n = x % 10;
            if (res > Integer.MAX_VALUE / 10 || res < Integer.MIN_VALUE / 10) {
                res = 0;
            }
            res = res * 10 + n;
            x /= 10;
        }
        System.out.println(res);
    }


    /**
     * 将一个按照升序排列的有序数组，转换为一棵高度平衡二叉搜索树。
     * 本题中，一个高度平衡二叉树是指一个二叉树每个节点 的左右两个子树的高度差的绝对值不超过 1。
     * 示例:
     * 给定有序数组: [-10,-3,0,5,9],
     * 一个可能的答案是：[0,-3,9,-10,null,5]，它可以表示下面这个高度平衡二叉搜索树：
     * 0
     * / \
     * -3   9
     * /   /
     * -10  5
     * public class TreeNode {
     * *     int val;
     * *     TreeNode left;
     * *     TreeNode right;
     * *     TreeNode(int x) { val = x; }
     * * }
     * TreeNode sortedArrayToBST(int[] nums)
     */
    @Test
    public void sortedArrayToBST() {
        int[] nums = {-10, -3, -2, -1, 1, 5, 9};
        TreeNode treeNode = sortedArrayToBST(nums, 0, nums.length - 1);
        printTreeNode(treeNode);
    }

    public class TreeNode {
        int val;
        TreeNode left;
        TreeNode right;

        TreeNode(int x) {
            val = x;
        }
    }

    public TreeNode sortedArrayToBST(int[] nums, int left, int right) {
        if (left > right) {
            return null;
        }

        // 总是选择中间位置左边的数字作为根节点
        int mid = (left + right) / 2;

        TreeNode root = new TreeNode(nums[mid]);
        root.left = sortedArrayToBST(nums, left, mid - 1);
        root.right = sortedArrayToBST(nums, mid + 1, right);
        return root;
    }

    public void printTreeNode(TreeNode treeNode) {
        if (treeNode == null) {
            return;
        }
        System.out.println(treeNode.val);
        printTreeNode(treeNode.left);
        printTreeNode(treeNode.right);
    }

    /**
     * 判断一个整数是否是回文数。回文数是指正序（从左向右）和倒序（从右向左）读都是一样的整数。
     * <p>
     * 示例 1:
     * <p>
     * 输入: 121
     * 输出: true
     * 示例 2:
     * <p>
     * 输入: -121
     * 输出: false
     * 解释: 从左向右读, 为 -121 。 从右向左读, 为 121- 。因此它不是一个回文数。
     */
    @Test
    public void isPalindrome() {
        System.out.println(isPalindrome(1231321));
        System.out.println(isPalindrome2(1231321));
    }

    public boolean isPalindrome(int num) {
        if (num < 0) {
            return false;
        }

        List<Integer> list = getArrByNum(num);
        int size = list.size();
        if (size == 1) {
            return true;
        }
        int times = size / 2;
        for (int i = 0; i < times; i++) {
            if (!list.get(i).equals(list.get(size - 1 - i))) {
                return false;
            }
        }


        return true;
    }

    private List<Integer> getArrByNum(int num) {
        List<Integer> list = new ArrayList<>();
        int a = num;
        while (true) {
            list.add(a % 10);
            a /= 10;
            if (a == 0) {
                break;
            }
        }
        return list;
    }

    public boolean isPalindrome2(int num) {
        if (num < 0) {
            return false;
        }
        if (num < 10) {
            return true;
        }
        int r = 0;
        while (num > r) {
            r = r * 10 + num % 10;
            num /= 10;
        }

        return num == r || num == r / 10;
    }


}
