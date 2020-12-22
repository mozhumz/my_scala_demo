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
     *
     * @return
     */
    private int[] getIntsArr() {
        int l = 40;
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
     * 2 选择排序
     * 首先在未排序序列中找到最小（大）元素，存放到排序序列的起始位置
     * 再从剩余未排序元素中继续寻找最小（大）元素，然后放到已排序序列的末尾。
     * 重复第二步，直到所有元素均排序完毕
     */
    @Test
    public void testChooseSort() {
        int[] arr = getIntsArr();
        CommonUtil.printIntArray(arr);
        CommonUtil.printIntArray(chooseSort(arr));
    }

    public int[] chooseSort(int[] arr) {
        for (int i = 0; i < arr.length; i++) {
            int pos = i;
            for (int j = i; j < arr.length; j++) {
                //找出未排序的序列中的最小（大）值 记录其位置
                if (arr[j] < arr[pos]) {
                    pos = j;
                }
            }

            //如果位置不同则交换
            if (i != pos) {
                int itmp = arr[i];
                arr[i] = arr[pos];
                arr[pos] = itmp;
            }
        }

        return arr;
    }

    /**
     * 3 插入排序
     * 首先把待排序列第一个元素看作有序，其余元素依次和已排序列比较，
     * 将元素插入到已排序列合适的位置（依次和相邻元素比较，较小（大）则交换位置）
     */
    @Test
    public void testInsertSort() {
        int[] arr = getIntsArr();
        CommonUtil.printIntArray(arr);
        CommonUtil.printIntArray(insertSort(arr));
    }

    public int[] insertSort(int[] arr) {
        //i=1 表示未排序列的第一个元素
        for (int i = 1; i < arr.length; i++) {
            for (int j = i; j - 1 >= 0; j--) {
                if (arr[j] < arr[j - 1]) {
                    int tmp = arr[j];
                    arr[j] = arr[j - 1];
                    arr[j - 1] = tmp;
                }

            }
        }

        return arr;
    }

    /**
     * 希尔排序
     * 先将整个待排序的记录序列分割成为若干子序列分别进行直接插入排序，具体算法描述：
     * 选择增量gap=length/2，缩小增量继续以gap = gap/2的方式，这种增量选择我们可以用一个序列来表示，{n/2,(n/2)/2...1}，称为增量序列
     * 选择一个增量序列t1，t2，…，tk，其中ti>tj，tk=1；
     * 按增量序列个数k，对序列进行k 趟排序；
     * 每趟排序，根据对应的增量ti，将待排序列分割成若干长度为m 的子序列，分别对各子表进行直接插入排序。
     * 仅增量因子为1 时，整个序列作为一个表来处理，表长度即为整个序列的长度
     */
    @Test
    public void testShellSort() {
        int[] arr = getIntsArr();
        CommonUtil.printIntArray(arr);
        CommonUtil.printIntArray(shellSort(arr));
    }

    public int[] shellSort(int[] arr) {
        //增量因子
        int fact = 2;
        int gap = arr.length / fact;
        while (gap != 0) {
            for (int i = gap; i < arr.length; i++) {
                for (int j = i; j >= gap; j -= gap) {
                    if (arr[j] < arr[j - gap]) {
                        int tmp = arr[j];
                        arr[j] = arr[j - gap];
                        arr[j - gap] = tmp;
                    }
                }
            }
            gap /= fact;
        }

        return arr;
    }

}
