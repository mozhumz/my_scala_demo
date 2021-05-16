package com.hyj.algorithm.demo;

import org.junit.Test;

import java.util.Arrays;
import java.util.Random;

/**
 * 十大排序算法
 * https://zhuanlan.zhihu.com/p/34894768
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
            //未排序元素依次和已排序元素比较
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
     * 4 希尔排序
     * 先将整个待排序的记录序列分割成为若干子序列分别进行直接插入排序，具体算法描述：
     * 选择增量gap=length/2，缩小增量继续以gap = gap/2的方式，这种增量选择我们可以用一个序列来表示，{n/2,(n/2)/2...1}，称为增量序列
     * 选择一个增量序列t1，t2，…，tk，其中ti>tj，tk=1；
     * 按增量序列个数k，对序列进行k 趟排序；
     * 每趟排序，根据对应的增量ti，将待排序列分割成 len/ti 组长度为m的子序列，分别对各子表进行直接插入排序。
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

    /**
     * 5 归并排序
     * <p>
     * 1.如果给的数组只有一个元素的话，直接返回（也就是递归到最底层的一个情况）
     * <p>
     * 2.把整个数组分为尽可能相等的两个部分（分）,直到只剩一个元素，执行步骤1
     * <p>
     * 3.从最底层开始，依次向上层递归，对于每一层而言，对同属于上一层某部分的有序序列进行合并
     */
    @Test
    public void TestMergeSort() {
        int[] arr = getIntsArr();
        CommonUtil.printIntArray(arr);
        mergeSort(arr, 0, arr.length);
        CommonUtil.printIntArray(arr);

    }

    /**
     * 归并排序-递归拆分序列
     *
     * @param arr
     * @param left
     * @param right
     */
    public static void mergeSort(int[] arr, int left, int right) {
        if (right - left > 1) {
            int mid = (left + right) / 2;
            //拆分左序列
            mergeSort(arr, left, mid);
            //拆分右序列
            mergeSort(arr, mid, right);
            //针对有序序列进行合并排序
            mergeSort(left, mid, right, arr);
        }


    }

    /**
     * 对2个有序序列归并排序
     * 注意：每个序列只包含左边界 [left,mid) [mid,right)
     *
     * @param left  第一个序列起始下标
     * @param mid   第二个序列起始下标
     * @param right
     * @param arr
     */
    public static void mergeSort(int left, int mid, int right, int[] arr) {
        int[] tmp = new int[right - left];
        //指向第一个序列的左边界
        int i = left;
        //指向第二个序列的左边界
        int j = mid;
        //指向tmp下标
        int k = 0;

        while (i < mid && j < right) {
            if (arr[i] <= arr[j]) {
                tmp[k++] = arr[i++];
            } else {
                tmp[k++] = arr[j++];
            }
        }
        //如果某个序列已经取完，还剩下另外一个序列，则将另外一个序列元素追加到tmp数组末尾
        //如果还剩左序列
        while (i < mid) {
            tmp[k++] = arr[i++];
        }
        while (j < right) {
            tmp[k++] = arr[j++];
        }
        //将tmp数组复制到arr对应下标
        for (int m = 0; m < tmp.length; m++) {
            arr[left + m] = tmp[m];
        }
    }

    /**
     * 归并排序-网络方法
     *
     * @param a
     * @param low
     * @param high
     * @return
     */
    public static int[] sort(int[] a, int low, int high) {
        int mid = (low + high) / 2;
        if (low < high) {
            sort(a, low, mid);
            sort(a, mid + 1, high);
            //左右归并
            merge(a, low, mid, high);
        }
        return a;
    }

    public static void merge(int[] a, int low, int mid, int high) {
        int[] temp = new int[high - low + 1];
        int i = low;
        int j = mid + 1;
        int k = 0;
        // 把较小的数先移到新数组中
        while (i <= mid && j <= high) {
            if (a[i] < a[j]) {
                temp[k++] = a[i++];
            } else {
                temp[k++] = a[j++];
            }
        }
        // 把左边剩余的数移入数组
        while (i <= mid) {
            temp[k++] = a[i++];
        }
        // 把右边边剩余的数移入数组
        while (j <= high) {
            temp[k++] = a[j++];
        }
        // 把新数组中的数覆盖nums数组
        for (int x = 0; x < temp.length; x++) {
            a[x + low] = temp[x];
        }
    }

    /**
     * 6 快速排序
     * 从数列中挑出一个元素，称为“基准”（pivot）
     * <p>
     * 分区（partition）: 遍历数列，比基准小的元素放左边，比它大的放右边。
     * 于是此次分区结束后，该基准就处于数列的中间位置，其左边的数全比它小（称为小与子序列），右边的数全比他大（称为大于子序列）。
     * 这样一次排序就造成了整体上的有序化。
     * <p>
     * 子数列排序: 将小于子数列和大于子序列分别继续快速排序。
     * 递归到最底部时，数列的大小是零或一，至此就都排序好了，递归结束。
     */
    @Test
    public void testQuickSort() {
        int[] arr ={9,7,8,1,3,5};
        CommonUtil.printIntArray(arr);
//        swap(0,3,arr);
//        swap(arr,1,2);
//        CommonUtil.printIntArray(arr);
        quickSort(arr, 0, arr.length - 1);
        CommonUtil.printIntArray(arr);
    }

    //
    private void swap(int[] arr, int i, int j) {
        if (i == j) {
            return;
        }
        int temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }

    public void quickSort(int[] arr, int l, int r) {
        if (l < r) {
            int p = partition(arr, l, r);
            //[l,p-1]<arr[p] [p+1,r]>arr[p]
            quickSort(arr, l, p - 1);
            quickSort(arr, p + 1, r);
        }
    }

    public int partition(int[] arr, int l, int r) {
        //指定左边界为分割点
        int v = arr[l];
        //j记录>=v的元素下标起始位置
        int j = l + 1;
        for (int i = j; i <= r; i++) {
            //将小于v的元素交换到前面，如数组：10 9 8 7 11 12 6 5 交换后为10 9 8 7 6 5 11 12 arr[j]=11
            if (arr[i] < v) {
                swap(arr, i, j++);
//                CommonUtil.printIntArray(arr);
//                swap(i, j++, arr);
            }
        }
        //j-1表示小于v的元素下标：arr[j-1]=5,arr[l]=10, 10 9 8 7 6 5 11 12 -> 5 9 8 7 6 10 11 12
        swap(arr, j - 1, l);

        return j - 1;
    }

    public void swap(int i, int j, int arr[]) {
        if (i == j) {
            return;
        }
        arr[i] = arr[i] + arr[j];
        arr[j] = arr[i] - arr[j];
        arr[i] = arr[i] - arr[j];
    }

    /**
     * 7 堆排序
     * 二叉堆的特点，以大顶堆为例：
     * 1 父节点有2个子节点（最后一层父节点可能只有1个 子节点）
     * 2 父节点元素值parent>=子节点元素值child
     * 堆排序流程：
     * 堆使用数组存储，初始是无序的，默认第一个元素为根节点，接着是左右子节点left right，
     * 1 从最后一层的父节点p开始进行下沉调整：如果p<=子节点c（c为子节点中的较大值），
     * 则p和c交换,c变为下一层的child（arr[p]=cVal p=c c=2*p+1） 当p>=c时，arr[p]=pVal
     * 2 排序，将首尾元素交换，然后对根元素下沉
     */
    @Test
    public void testHeapSort() {
        int[] arr = {67, 81, 41, 84, 19, 68, 59, 76, 97, 30};
        CommonUtil.printIntArray(arr);
        heapSort(arr,arr.length,2);
        CommonUtil.printIntArray(arr);
    }

    /**
     * 堆排序
     * @param arr
     * @param length
     * @param sortType 1 倒序 2 升序
     * @return
     */
    public static int[] heapSort(int[] arr, int length,int sortType) {
        //构建二叉堆
        for (int i = (length - 2) / 2; i >= 0; i--) {
            arr = downAdjust(arr, i, length,sortType);
        }
        //进行堆排序
        for (int i = length - 1; i >= 1; i--) {
            //把堆顶的元素与最后一个元素交换
            int temp = arr[i];
            arr[i] = arr[0];
            arr[0] = temp;
            //下沉调整
            arr = downAdjust(arr, 0, i,sortType);
        }
        return arr;
    }

    /**
     *  下沉操作，执行删除操作相当于把最后
     *  * 一个元素赋给根元素之后，然后对根元素执行下沉操作
     * @param arr
     * @param parent 要下沉元素的下标
     * @param length 数组长度
     * @param sortType 1小顶堆 2大顶堆
     */
    public static int[] downAdjust(int[] arr, int parent, int length,int sortType) {
        //临时保存要下沉的元素
        int temp = arr[parent];
        //定位左孩子节点位置
        int child = 2 * parent + 1;
        //开始下沉
        while (child < length) {
            //如果右孩子节点比左孩子小，则定位到右孩子
            if (child + 1 < length ) {
                if(sortType==1&&arr[child] > arr[child + 1]){
                    child++;
                }else if(sortType==2&&arr[child] <arr[child + 1]){
                    child++;
                }
            }
            //如果父节点比孩子节点小或等于，则下沉结束
            if (temp <= arr[child] &&sortType==1){
                break;
            }
            if (temp >= arr[child] &&sortType==2){
                break;
            }
            //父节点和子节点元素交换
            if(parent!=child){
                arr[parent] = arr[child];
                parent = child;
                child = 2 * parent + 1;
            }
        }
        arr[parent] = temp;
        return arr;
    }





    /**
     * 8 计数排序
     * 找到数组中的最大值和最小值 max min
     * 生成max-min+1长度的数组，下标0表示min，数组最后一个下标表示max，
     * 元素的值val和新数组下标index的关系为 val - min = index，
     * 再次遍历原数组，按照此关系将元素存入新数组，如果新数组index有值(>0)，则所在index自增1，否则为1
     * 遍历新数组，其中下标index+1就是元素的值，下标所在的值为元素出现的次数
     */
    @Test
    public void testCountingSort() {
        int[] arr = getIntsArr();
        CommonUtil.printIntArray(arr);
        countingSort(arr);
        CommonUtil.printIntArray(arr);
    }

    public int[] countingSort(int[] arr) {
        int max = arr[0];
        int min = arr[0];
        //找到最值
        for (int i : arr) {
            if (i > max) {
                max = i;
            }
            if (i < min) {
                min = i;
            }
        }
        //生成max-min长度的新数组
        int[] newArr = new int[max - min + 1];

        for (int i : arr) {
            int index = i - min;
            int val = newArr[index];
            if (val == 0) {
                newArr[index] = 1;
            } else {
                newArr[index] += 1;
            }
        }

        int k = 0;
        for (int j = 0; j < newArr.length; j++) {
            int count = newArr[j];
            if (count == 0) {
                continue;
            }
            for (int i = 1; i <= count; i++) {
                arr[k++] = j + min;
            }
        }

        return arr;
    }

    @Test
    public void testBucketSort(){

    }




}
