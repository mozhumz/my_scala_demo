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
        ListNode res=setLink(map1, map2);
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
        ListNode listNode=null;
        //423 1->3 2->2 3->4 //9 1->9
        if (!map1.isEmpty() && !map2.isEmpty()) {
            int tmpAdd = 0;
            if (map1.size() > map2.size()) {
                listNode=addLink(map1, map2,  tmpAdd);
            } else {
                listNode=addLink(map2, map1,  tmpAdd);
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

}
