package com.hyj.algorithm.test;

import java.util.ArrayList;
import java.util.List;

public class CommonTest {
    /**
     * 给定一个整数数组，找出该数组的连续子数组，使得该子数组的和是所有子数组中最大的
     * 分析：遍历数组，
     * @param nums
     * @return
     */
    public static int getMaxSumOfContinuousSubsequence(int[]nums){
        int maxSum=nums[0];
        int tmp=0;
        List<Integer> tmplist=new ArrayList<>();
        for(int i=0;i<nums.length;i++){
            if(tmp<0){
                tmp=nums[i];
                tmplist.clear();
            }else {
                tmp+=nums[i];
            }
            tmplist.add(nums[i]);
            if(maxSum<tmp){
                maxSum=tmp;
                System.out.println(tmplist);
            }
        }
        return maxSum;
    }

    public static void main(String[] args) {
        int[]nums = {-2,1,-3,4,-1,2,1,-5,4};
        System.out.println(getMaxSumOfContinuousSubsequence(nums));
    }
}
