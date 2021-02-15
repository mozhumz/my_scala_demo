package com.hyj.algorithm.demo;

import java.util.Arrays;
import java.util.Random;

public class CommonUtil {
    public static void printObjArray(Object[]arr){
        System.out.println(Arrays.toString(arr));
    }

    public static void printIntArray(int[]arr){
        System.out.println(Arrays.toString(arr));
    }

    /**
     * 生成随机数组
     * @param l
     * @return
     */
    public static int[] getIntArr(Integer l) {
        if(l==null){
            l = 10;
        }
        int[] arr = new int[l];
        for (int i = 0; i < l; i++) {
            arr[i] = new Random().nextInt(100);
        }
        return arr;
    }

    /**
     * 生成有序数组
     * @param len
     * @return
     */
    public static int[] genSortedArr(int len){
        int[]arr=new int[len];
        for(int i=0;i<len;i++){
            arr[i]=i;
        }
        return arr;
    }
}
