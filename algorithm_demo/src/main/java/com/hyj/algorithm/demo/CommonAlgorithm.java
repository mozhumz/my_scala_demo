package com.hyj.algorithm.demo;

import org.junit.Test;

import java.util.*;

public class CommonAlgorithm {

    /**
     * 给定一个整数数组 nums和一个目标值 target，请你在该数组中找出和为目标值的那两个整数，并返回他们的数组下标。
     * 你可以假设每种输入只会对应一个答案。但是，数组中同一个元素不能使用两遍。
     * <p>
     * 示例:
     * 给定 nums = [2, 7, 11, 15], target = 9
     * 因为 nums[0] + nums[1] = 2 + 7 = 9
     * 所以返回 [0, 1]
     */
    @Test
    public void twoSum2() {
        int[] arr = {3, 3, 1, 1, 1, 5, 5};
        int target = 6;
        //每个元素进行登记
//        twoSum1(arr, target);
//        System.out.println(list);
        twoSum2(arr, target);

    }

    private void twoSum1(int[] arr, int target) {
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
    }

    public int[] twoSum2(int[] nums, int target) {
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
     * 三数之和
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
        int[] arr = {-1, 0, 1, 2, -1, -4, 1, 1, 2, 2};
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
     * 给出两个非空 的链表用来表示两个非负的整数。其中，它们各自的位数是按照逆序的方式存储的，并且它们的每个节点只能存储一位数字。
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
     * 给定一个字符串，请你找出其中不含有重复字符的最长子串的长度。
     * 示例1:
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
     * 解释: 因为无重复字符的最长子串是"wke"，所以其长度为 3。
     * 请注意，你的答案必须是 子串 的长度，"pwke"是一个子序列，不是子串。
     */
    @Test
    public void lengthOfLongestSubstring() {
//        lengthOfLongestSubstring2("abcabcdde");
        String str = "abcaqwert";
        Map<Character, Integer> maxMap = new LinkedHashMap<>();
        maxMap = lengthOfLongestSubstring3(str, maxMap);
        System.out.println(maxMap);
        lengthOfLongestSubstring2(str);
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
            map = new LinkedHashMap<>();
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
        System.out.println(s.substring(s1, s2 + 1));
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


    @Test
    public void testNum() {
        int[] arr = {1, 2, -1, 3, 4};
        System.out.println(findGreatestSumOfSubArray(arr));
        System.out.println(getMaxcontinousSum(arr));
    }

    public int findGreatestSumOfSubArray(int[] array) {
        if (array.length == 0) {
            return 0;
        }
        int max = array[0];
        int current = array[0] < 0 ? 0 : array[0];
        for (int i = 1; i < array.length; i++) {
            current += array[i];
            if (current > max) {
                max = current;
            } else if (current < 0) {
                current = 0;
            }
        }
        return max;
    }

    /**
     * 最大连续子数组
     * 动态规划解析：
     * 状态定义： 设动态规划列表 dp，dp[i] 代表以元素 nums[i]为结尾的连续子数组最大和。
     * 当 dp[i - 1] > 0 时：执行 dp[i] = dp[i-1] + nums[i]
     * 当 dp[i - 1] ≤0 时：执行 dp[i] = nums[i]
     * 初始状态： dp[0] = nums[0]，即以 nums[0]结尾的连续子数组最大和为 nums[0] 。
     * <p>
     * 返回值： 返回 dpdp 列表中的最大值，代表全局最大值
     *
     * @param nums
     * @return
     */
    public int getMaxcontinousSum(int[] nums) {
        int max = nums[0];
        //
        int temp = 0;
        //截止到下标i的最大和子数组
        List tempList = new ArrayList();
        //最大和子数组
        List maxList = new ArrayList();
        for (int i = 0; i < nums.length; i++) {
            if (temp < 0) {
                temp = nums[i];
                tempList.clear();
            } else {
                temp += nums[i];
            }
            tempList.add(nums[i]);
            if (temp > max) {
                max = temp;
                maxList = tempList;
            }
        }
        System.out.println(maxList);

        return max;
    }

    /**
     * 通过排序找出重复任意数字
     *
     * @param nums
     * @return
     */
    public int findRepeatNumber(int[] nums) {
        Arrays.sort(nums);
        int n = nums[0];
        for (int i = 1; i < nums.length; i++) {
            if (n == nums[i]) {
                return n;
            }
            n = nums[i];
        }
        return n;
    }

    /**
     * 通过set集合找出重复数字
     *
     * @param nums
     * @return
     */
    public int findRepeatNumber2(int[] nums) {
        HashSet<Integer> set = new HashSet<>();
        for (int n : nums) {
            if (!set.add(n)) {
                return n;
            }
        }
        return 0;
    }

    /**
     * 找出数组中重复的数字。
     * 在一个长度为 n 的数组 nums 里的所有数字都在 0～n-1 的范围内。数组中某些数字是重复的，但不知道有几个数字重复了，
     * 也不知道每个数字重复了几次。请找出数组中任意一个重复的数字。
     * 示例 1：
     * <p>
     * 输入：
     * [2, 3, 1, 0, 2, 5, 3]
     * 输出：2 或 3
     */
    @Test
    public void testFindRepeatNumber() {
        int[] nums = {2, 3, 1, 0, 2, 5, 3};
        System.out.println(findRepeatNumber(nums));
        System.out.println(findRepeatNumber2(nums));
    }

    /**
     * 20 有效的括号
     * 给定一个只包括 '('，')'，'{'，'}'，'['，']' 的字符串 s ，判断字符串是否有效。
     * 有效字符串需满足：
     * 左括号必须用相同类型的右括号闭合。
     * 左括号必须以正确的顺序闭合。
     * <p>
     * 示例 1：
     * 输入：s = "()"
     * 输出：true
     * <p>
     * 示例2：
     * 输入：s = "()[]{}"
     * 输出：true
     * <p>
     * 示例3：
     * 输入：s = "(]"
     * 输出：false
     * <p>
     * 示例4：
     * 输入：s = "([)]"
     * 输出：false
     * <p>
     * 分析：
     * 对于有效的括号，它的部分子表达式仍然是有效的括号，比如 {()[()]} 是一个有效的括号，
     * ()[{}] 是有效的括号，[()] 也是有效的括号。并且当我们每次删除一个最小的括号对时，
     * 我们会逐渐将括号删除完。比如下面的例子:
     * 1 {()[()]}
     * 2 {[()]}
     * 3 {[]}
     * 4 {}
     * 这个过程其实就是栈的实现过程。因此我们考虑使用栈，遍历字符串s
     * 每次遇到左括号时，将其放入栈，遇到右括号时，从栈顶取出一个左括号，
     * 判断是否与右括号匹配，如果匹配继续遍历s，否则返回false
     * 如果最后栈为空，那么它是有效的括号，反之不是
     */
    @Test
    public void test20210526() {
        System.out.println(isValid("()"));
    }

    public boolean isValid(String s) {
//        Stack<Character> stack = new Stack<>();
        LinkedList<Character> list = new LinkedList<>();
//        list.add('?');
        Map<Character, Character> map = new HashMap<>();
        map.put('(', ')');
        map.put('[', ']');
        map.put('{', '}');
        for (char ch : s.toCharArray()) {
            if (map.containsKey(ch)) {
                list.add(ch);
            } else {
                if (list.isEmpty()) {
                    return false;
                }
                Character left = list.removeLast();
                if (map.getOrDefault(left, '?') != ch) {
                    return false;
                }
            }
        }

//        return list.size()==1;
        return list.isEmpty();
    }


    @Test
    public void test202106011716(){
//        System.out.println(hammingWeight(9));
        System.out.println(myPow(1,2147483647));
    }
    public int hammingWeight(int n) {
        int count=0;
        char[] chars = Integer.toBinaryString(n).toCharArray();
        for(char ch:chars){
            if(ch=='1'){
                count++;
            }
        }
        return count;
    }



    public double myPow(double x, int n) {

        double res=1.;
        if(x==1.){
            return res;
        }
        while (n>=1){
            res*=x;
            n--;
        }
        while (n<0){
            res/=x;
            n++;
        }
        return res;
    }
}
