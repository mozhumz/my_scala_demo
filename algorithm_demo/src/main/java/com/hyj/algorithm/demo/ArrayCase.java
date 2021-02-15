package com.hyj.algorithm.demo;

import org.junit.Test;


/**
 * 数组相关算法
 */
public class ArrayCase {

    /**
     * 二分查找
     * 给定一个排序数组和一个目标值，在数组中找到目标值，并返回其索引。如果目标值不存在于数组中，返回它将会被按顺序插入的位置。
     * 示例 1:
     * 输入: [1,3,5,6], 5
     * 输出: 2
     *
     * 示例 2:
     * 输入: [1,3,5,6], 2
     * 输出: 1
     *
     * 示例 3:
     * 输入: [1,3,5,6], 7
     * 输出: 4
     *
     * 示例 4:
     * 输入: [1,3,5,6], 0
     * 输出: 0
     */
    @Test
    public void testBinarySearch(){
        int len=10;
        int[] arr=new int[len];
        for(int i=0;i<len;i++){
            arr[i]=i;
        }
//        int[]arr={9, 11, 21, 23, 27, 50, 54, 56, 86, 93,94,95,96,97,98,99,100};
        CommonUtil.printIntArray(arr);
        System.out.println(binarySearch(arr,10));

    }


    /**
     * 二分查找
     * @param arr
     * @param target
     * @return
     */
    public int binarySearch(int[]arr,int target){

        return binarySearch(arr,0,arr.length-1,target);
    }

    /**
     * 二分查找
     * @param arr
     * @param begin
     * @param end
     * @param target
     * @return
     */
    public int binarySearch(int[] arr,int begin,int end,int target){
        if(begin>=end){
            if(end<0){
                return 0;
            }
            if(target==arr[end]){
                return end;
            }else if(target>arr[end]){
                return end+1;
            }else {
                return end-1;
            }
        }
        int mid=(end+begin)/2;
        if(target==arr[mid]){
            return mid;
        }else if(target>arr[mid]){
            return binarySearch(arr,mid+1,end,target);
        }else {
            return binarySearch(arr,0,mid-1,target);
        }

    }

    /**
     * ❞
     * 编号：27. 移除元素
     * 给你一个数组 nums 和一个值 val，你需要 原地 移除所有数值等于 val 的元素，并返回移除后数组的新长度。
     *
     * 不要使用额外的数组空间，你必须仅使用 O(1) 额外空间并「原地」修改输入数组。
     *
     * 元素的顺序可以改变。你不需要考虑数组中超出新长度后面的元素。
     *
     * 示例 1:
     * 给定 nums = [3,2,2,3], val = 3,
     * 函数应该返回新的长度 2, 并且 nums 中的前两个元素均为 2。
     * 你不需要考虑数组中超出新长度后面的元素。
     *
     * 示例 2:
     * 给定 nums = [0,1,2,2,3,0,4,2], val = 2,
     * 函数应该返回新的长度 5, 并且 nums 中的前五个元素为 0, 1, 3, 0, 4。
     *
     * 「你不需要考虑数组中超出新长度后面的元素。」
     */
    @Test
    public void testRemove(){
        int[]arr={1,2,3,4,4,5,4,6};
        CommonUtil.printIntArray(arr);
//        System.out.println(removeTarget(arr,4));
        System.out.println(removeTargetBy2pointers(arr,4));
        CommonUtil.printIntArray(arr);
    }

    /**
     * 删除元素-双循环，第一层遍历元素，第二层元素依次向前移动1位
     *
     * @param arr
     * @param target
     * @return
     */
    public int removeTarget(int[]arr,int target){
        int len=arr.length;
        for(int i=0;i<arr.length;i++){
            //找到要移除的元素值
            if(arr[i]==target){
                for(int j=i+1;j<arr.length;j++){
                    arr[j-1]=arr[j];
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
     * @param arr
     * @param target
     * @return
     */
    public int removeTargetBy2pointers(int[]arr,int target){
        int slow=0;
        for(int fast=0;fast<arr.length;fast++){
            if(arr[fast]!=target){
                arr[slow++]=arr[fast];
            }
        }

        return slow;
    }

}
