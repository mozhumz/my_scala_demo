package com.hyj.algorithm.demo;

import org.junit.Test;

import java.util.Arrays;
import java.util.Random;

/**
 * 十大排序算法
 */
public class TenSort {

    /**
     * 1 冒泡排序
     * 双循环，比较相邻2个元素，如果第1个比第2个大，则交换位置，每一次外循环后，都会找到top-n大的元素，
     * 最后一次循环是比较0和1位置的元素
     */
    @Test
    public void testBubble() {
        int[] arr = getIntsArr();
        CommonUtil.printIntArray(arr);
        CommonUtil.printIntArray(bubbleSort(arr));
    }

    /**
     * 生成随机数组
     * @return
     */
    private int[] getIntsArr() {
        int l = 10;
        int[] arr = new int[l];
        for (int i = 0; i < l; i++) {
            arr[i] = new Random().nextInt(100);
        }
        return arr;
    }

    public int[] bubbleSort(int[] arr) {
        for (int i = 0; i < arr.length - 1; i++) {
            for (int j = 1; j < arr.length - i; j++) {
                if (arr[j - 1] > arr[j]) {
                    int tmp = arr[j];
                    arr[j] = arr[j - 1];
                    arr[j - 1] = tmp;
                }
            }
        }

        return arr;
    }

    /**
     * 首先在未排序序列中找到最小（大）元素，存放到排序序列的起始位置
     * 再从剩余未排序元素中继续寻找最小（大）元素，然后放到已排序序列的末尾。
     * 重复第二步，直到所有元素均排序完毕
     */
    @Test
    public void testChooseSort() {
        int[]arr=getIntsArr();
        CommonUtil.printIntArray(arr);
        CommonUtil.printIntArray(chooseSort(arr));
    }

    public int[] chooseSort(int[] arr) {
        for (int i = 0; i < arr.length; i++) {
            int pos = i;
            for (int j = i; j < arr.length; j++) {
                //找出未排序的序列中的最小（大）值 记录其位置
                if (arr[j] < arr[pos]) {
                    pos=j;
                }
            }

            //如果位置不同则交换
            if(i!=pos){
                int itmp=arr[i];
                arr[i]=arr[pos];
                arr[pos]=itmp;
            }
        }

        return arr;
    }


}
