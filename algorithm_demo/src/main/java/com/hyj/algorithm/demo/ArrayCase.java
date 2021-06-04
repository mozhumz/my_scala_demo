package com.hyj.algorithm.demo;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * 数组相关算法
 */
public class ArrayCase {

    /**
     * 35 搜索插入位置 （二分查找）
     * 给定一个排序数组和一个目标值，在数组中找到目标值，并返回其索引。如果目标值不存在于数组中，返回它将会被按顺序插入的位置。
     * 示例 1:
     * 输入: [1,3,5,6], 5
     * 输出: 2
     * <p>
     * 示例 2:
     * 输入: [1,3,5,6], 2
     * 输出: 1
     * <p>
     * 示例 3:
     * 输入: [1,3,5,6], 7
     * 输出: 4
     * <p>
     * 示例 4:
     * 输入: [1,3,5,6], 0
     * 输出: 0
     */
    @Test
    public void testBinarySearch() {
        int len = 10;
        int[] arr = {1, 3};
//        for (int i = 0; i < len; i++) {
//            arr[i] = i;
//        }
//        int[]arr={9, 11, 21, 23, 27, 50, 54, 56, 86, 93,94,95,96,97,98,99,100};
        CommonUtil.printIntArray(arr);
        System.out.println(binarySearch(arr, 2));

    }


    /**
     * 二分查找
     *
     * @param arr
     * @param target
     * @return
     */
    public int binarySearch(int[] arr, int target) {

        return binarySearch(arr, 0, arr.length - 1, target);
    }

    /**
     * 二分查找
     *
     * @param arr
     * @param begin
     * @param end
     * @param target
     * @return
     */
    public int binarySearch(int[] arr, int begin, int end, int target) {
        if (begin >= end) {
            //防止下标越界
            if (end < 0) {
                return 0;
            }
            if (target == arr[end]) {
                return end;
            } else if (target > arr[end]) {
                return end + 1;
            } else {
                if (end == 0) {
                    return 0;
                }
                //针对情况：[1,3] 2
                if (target > arr[end - 1]) {
                    return end;
                }
                return end - 1;
            }
        }
        int mid = (end + begin) / 2;
        if (target == arr[mid]) {
            return mid;
        } else if (target > arr[mid]) {
            return binarySearch(arr, mid + 1, end, target);
        } else {
            return binarySearch(arr, 0, mid - 1, target);
        }

    }

    /**
     * ❞
     * 编号：27. 移除元素
     * 给你一个数组 nums 和一个值 val，你需要 原地 移除所有数值等于 val 的元素，并返回移除后数组的新长度。
     * <p>
     * 不要使用额外的数组空间，你必须仅使用 O(1) 额外空间并「原地」修改输入数组。
     * <p>
     * 元素的顺序可以改变。你不需要考虑数组中超出新长度后面的元素。
     * <p>
     * 示例 1:
     * 给定 nums = [3,2,2,3], val = 3,
     * 函数应该返回新的长度 2, 并且 nums 中的前两个元素均为 2。
     * 你不需要考虑数组中超出新长度后面的元素。
     * <p>
     * 示例 2:
     * 给定 nums = [0,1,2,2,3,0,4,2], val = 2,
     * 函数应该返回新的长度 5, 并且 nums 中的前五个元素为 0, 1, 3, 0, 4。
     * <p>
     * 「你不需要考虑数组中超出新长度后面的元素。」
     */
    @Test
    public void testRemove() {
        int[] arr = {1, 2, 3, 4, 4, 5, 4, 6};
        CommonUtil.printIntArray(arr);
//        System.out.println(removeTarget(arr,4));
        System.out.println(removeTargetBy2pointers(arr, 4));
        CommonUtil.printIntArray(arr);
    }

    /**
     * 删除元素-双循环，第一层遍历元素，第二层元素依次向前移动1位
     *
     * @param arr
     * @param target
     * @return
     */
    public int removeTarget(int[] arr, int target) {
        int len = arr.length;
        for (int i = 0; i < arr.length; i++) {
            //找到要移除的元素值
            if (arr[i] == target) {
                for (int j = i + 1; j < arr.length; j++) {
                    arr[j - 1] = arr[j];
                }
                //数组长度-1
                len--;
                //新数组下标向前移动1位
                i--;
            }
        }
        return len;
    }

    /**
     * 删除元素-双指针 快指针fast遍历元素，慢指针slow规定 [0,slow)范围不包含target
     * 如果fast值!=target 则slow位置的值=fast值 即nums[slow]=nums[fast]
     * 赋值完成之后，接着要做的是将慢指针slow向后移动一个位置，来保证区间[0,slow)中的元素都是值不等于target的元素
     *
     * @param arr
     * @param target
     * @return
     */
    public int removeTargetBy2pointers(int[] arr, int target) {
        int slow = 0;
        for (int fast = 0; fast < arr.length; fast++) {
            if (arr[fast] != target) {
                arr[slow++] = arr[fast];
            }
        }

        return slow;
    }

    /**
     * 题目209.长度最小的子数组
     * 给定一个含有 n 个正整数的数组和一个正整数 s ，找出该数组中满足其和 ≥ s 的长度最小的 连续 子数组，并返回其长度。
     * 如果不存在符合条件的子数组，返回 0。
     * <p>
     * 示例：
     * 输入：s = 7, nums = [2,3,1,2,4,3]
     * 输出：2
     * 解释：子数组 [4,3] 是该条件下的长度最小的子数组
     */
    @Test
    public void testMinlenOfContinousArr() {
        int[] arr = {2, 3, 1, 2, 4, 3};
        System.out.println(minlenOfContinousArr(arr, 7));
    }

    public int minlenOfContinousArr(int[] arr, int s) {
        //最小子数组长度
        int minlen = 0;
        //最小子数组和
//        int sum = 0;
        //临时子数组的和
        int tmpsum = 0;
        //临时子数组的长度
        int tmplen = 0;
        int j = 0;
        for (int i = 0; i < arr.length; i++) {
            tmpsum += arr[i];
            while (tmpsum >= s) {
                tmplen = i - j + 1;
                if (minlen == 0 || tmplen < minlen) {
                    minlen = tmplen;
//                    sum=tmpsum;
                }
                tmpsum -= arr[j++];
            }
        }
//        System.out.println("sum:"+sum);
        return minlen;
    }

    /**
     * 题目59.螺旋矩阵II
     * 给定一个正整数 n，生成一个包含 1 到 n2 所有元素，且元素按顺时针顺序螺旋排列的正方形矩阵。
     * <p>
     * 示例:
     * <p>
     * 输入: 3 输出:
     * [
     * [ 1, 2, 3 ],
     * [ 8, 9, 4 ],
     * [ 7, 6, 5 ]
     * ]
     * <p>
     * 分析：每次，分4个方向填充二维数组，每个方向最后一个数的值设置为m，停止条件为m=n^2
     * 1 2 3 ... n
     * k=4n-4...  k+n-2 n+1
     * 3n        n+2
     * 3n-1      2n-2
     * 3n-2	   2n 2n-1
     */
    @Test
    public void testScrewArr() {
        int[][] arr = screwArr(5);
        for (int i = 0; i < arr.length; i++) {
            CommonUtil.printIntArray(arr[i]);
        }
    }

    /**
     * @param n
     * @return
     */
    public int[][] screwArr(int n) {
        int[][] arr = new int[n][n];
        if (n == 1) {
            arr[0][0] = 1;
            return arr;
        }
        //x表示行坐标 y表示列坐标
        int x, y;
        //每次循环的起始坐标
        int x0 = 0, y0 = 0;

        //计数
        int count = 1;
        //控制每边的长度
        int offset = 1;
        for (int loop = 0; loop < n / 2; loop++) {
            //loop表示轮数
            //更新起始位置
            x = x0;
            y = y0;
            //右
            for (; y < y0 + n - offset; y++) {
                arr[x][y] = count++;
            }
            //下
            for (; x < x0 + n - offset; x++) {
                arr[x][y] = count++;
            }
            //左
            for (; y > y0; y--) {
                arr[x][y] = count++;
            }
            //上
            for (; x > x0; x--) {
                arr[x][y] = count++;
            }
            //每一圈后更新起始位置
            x0++;
            y0++;
            //每一圈后 每边长度比上次少2
            offset += 2;

        }
        //n为奇数时 最中间的坐标
        int mid = n / 2;
        if (n % 2 == 1) {
            arr[mid][mid] = n * n;
        }


        return arr;
    }

    /**
     * 剑指 Offer 21. 调整数组顺序使奇数位于偶数前面
     * 输入一个整数数组，实现一个函数来调整该数组中数字的顺序，使得所有奇数位于数组的前半部分，所有偶数位于数组的后半部分。
     * 示例：
     * 输入：nums =[1,2,3,4]
     * 输出：[1,3,2,4]
     * 注：[3,1,2,4] 也是正确的答案之一。
     *
     * 提示：
     * 0 <= nums.length <= 50000
     * 1 <= nums[i] <= 10000
     *
     */
    @Test
    public void test20210604() {
        int[] arr = {1, 2, 3, 4, 5};
        System.out.println(Arrays.toString(exchange2(arr)));
    }

    /**
     * 快慢指针
     * 快指针fast寻找奇数
     * 慢指针low寻找偶数
     * fast>low 进行交换元素
     * @param nums
     * @return
     */
    public int[] exchange(int[] nums) {
        int low = 0;
//        int fast=0;
        for (int fast = 0; fast < nums.length; fast++) {
            while (low < nums.length && nums[low] % 2 != 0) {
                low++;
            }
            if (low < fast && nums[fast] % 2 == 1) {
                int tmp = nums[fast];
                nums[fast] = nums[low];
                nums[low] = tmp;
            }
        }

        return nums;
    }

    /**
     * 左右指针
     * left寻找偶数 right寻找奇数
     * left<right 交换元素位置
     * @param nums
     * @return
     */
    public int[] exchange2(int[] nums) {
        int l = 0;
        int r = nums.length - 1;
        while (l < r) {
            //找偶数
            if (nums[l] % 2 != 0) {
                l++;
                continue;
            }
            //找奇数
            if (nums[r] % 2 == 0) {
                r--;
                continue;
            }
            int tmp = nums[l];
            nums[l] = nums[r];
            nums[r] = tmp;
            l++;
            r--;

        }

        return nums;
    }
}
