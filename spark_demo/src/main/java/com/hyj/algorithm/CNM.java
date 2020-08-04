package com.hyj.algorithm;


import java.util.*;

public class CNM {

    public static void main(String[] args) {
        int[]arr={-1,0,1,2,-1,-4};
        System.out.println(getC(arr));
    }

    private static List<List<Integer>> getC(int[] arr) {
        Set<String> set=new HashSet<>();
        List<List<Integer>>res=new ArrayList<>();
        for(int i=0;i<arr.length;i++){
            for(int j=i+1;j<arr.length;j++){
                for(int m=j+1;m<arr.length;m++){
                    if(arr[i]+arr[j]+arr[m]==0){
                        List<Integer>nums=new ArrayList<>();
                        nums.add(arr[i]);
                        nums.add(arr[j]);
                        nums.add(arr[m]);
                        nums.sort(null);
                        set.add(nums.get(0)+","+nums.get(1)+","+nums.get(2));
                    }
                }
            }
        }
        for(String str:set){
            String[]nums=str.split(",");
            List<Integer>list=new ArrayList<>();
            for(String n:nums){
                if(n!=null){
                    list.add(Integer.parseInt(n));
                }
            }
            res.add(list);
        }
        return res;
    }
}
