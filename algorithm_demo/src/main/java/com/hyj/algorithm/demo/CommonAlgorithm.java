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
        int[] arr = {3, 2,4};
        int target = 6;
        Map<Integer,List<Integer>> map = new HashMap<>();
        for (int i = 0; i < arr.length; i++) {
            Integer key=target - arr[i];
            List<Integer>list=map.get(key);
            if(list==null){
                list=new ArrayList<>();
            }
            list.add(i);
            map.put(key,list);
        }
        ArrayList<Integer> list = new ArrayList<>();

        for (int i = 0; i < arr.length; i++) {
            if(list.contains(i)){
                continue;
            }
            if (map.get(arr[i]) != null ) {
                if(map.get(arr[i]).size()>1){
                    list.addAll(map.get(arr[i]));
                    continue;
                }
                if(map.get(arr[i]).contains(i))continue;
                list.add(i);
            }
        }
        int[] res = new int[list.size()];
        for (int i = 0; i < list.size(); i++) {
            res[i] = list.get(i);
        }
        System.out.println(list);

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
}
