package com.hyj.algorithm.demo;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import org.junit.Test;

import java.util.*;
import java.util.stream.Collectors;

public class HyjTest {
    public static volatile int exeCount = 0;

    @Test
    public void test1() {
        System.out.println(robotGrid(3, 5));
        System.out.println(DynamicPlan.robotWalkGrid2(3, 5));

    }

    public static int robotGrid(int m, int n) {
        //定义dp[m][n]为m*n的网格的路径数
        if (m == 0 || n == 0) {
            return 0;
        }
        int[][] dp = new int[m][n];
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (i == 0 || j == 0) {
                    dp[i][j] = 1;
                } else {
                    dp[i][j] = dp[i - 1][j] + dp[i][j - 1];
                }
            }
        }
        return dp[m - 1][n - 1];
    }


    /**
     * 判定两个指定的字符串是否异构同质；异构同质的定义为：一个字符串的字符重新排列后，能变成另一个字符串
     * 输入例子1:
     * abc acb
     * <p>
     * 输出例子1:
     * true
     */
    @Test
    public void test2() {
        System.out.println(testStrEq("IAmMemberOfPinganTech", "AmIPingAnTechOMemfber"));
    }

    public static boolean testStrEq(String str1, String str2) {
        if (str1.length() != str2.length()) {
            return false;
        }
        char[] ch1 = str1.toCharArray();

        char[] ch2 = str2.toCharArray();
        Map<Character, Integer> map1 = new HashMap<>();
        for (char ch : ch1) {
            Integer o = map1.get(ch);
            if (o == null) {
                o = 1;
            } else {
                o++;
            }
            map1.put(ch, o);
        }
        for (char ch : ch2) {

            Integer i = map1.get(ch);
            if (i == null || i == 0) {
                return false;
            } else {
                i--;
                map1.put(ch, i);
            }
        }
        return true;


    }

    public static boolean testStrEq2(String str1, String str2) {

        char[] ch1 = str1.toCharArray();
        Arrays.sort(ch1);
        char[] ch2 = str2.toCharArray();
        Arrays.sort(ch2);
        int flag = 0;
        for (int i = 0; i < ch1.length; i++) {
            if (ch1[i] != ch2[i]) {
                flag = 1;
                break;
            }
        }
        if (flag == 0) return true;
        else return false;
    }

    /**
     * LCS
     */
    @Test
    public void test3() {
        char[] X = {'A', 'B', 'C', 'B', 'D', 'A', 'B'};
        char[] Y = {'B', 'D', 'C', 'A', 'B', 'A'};
        String str1 = String.valueOf(X);
        String str2 = String.valueOf(Y);
        System.out.println(maxLenOfLcs(str1, str2));
    }

    public static int maxLenOfLcs(String str1, String str2) {
        int m = str1.length();
        int n = str2.length();
        if (m == 0 || n == 0) {
            return 0;
        }
        //定义dp[m][n]为字符串m和n的LCS长度
        int[][] dp = new int[m + 1][n + 1];
        //定义标记矩阵：0默认 1左上 2上 3左
        int[][] mark = new int[m + 1][n + 1];
        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                if (str1.charAt(i - 1) == str2.charAt(j - 1)) {
                    dp[i][j] = dp[i - 1][j - 1] + 1;
                    mark[i][j] = 1;
                } else {
                    dp[i][j] = Math.max(dp[i - 1][j], dp[i][j - 1]);
                    if (dp[i][j] == dp[i - 1][j]) {
                        mark[i][j] = 2;
                    } else {
                        mark[i][j] = 3;
                    }
                }
            }
        }
        LCSHyj.print(dp, m, n);
        System.out.println("------------------------");
        LCSHyj.print(mark, m, n);
        System.out.println(getLcsStr(mark, str1, m, n));
        System.out.println("------------------------");
        System.out.println(getLcsList(mark, str1, m, n, dp[m][n], dp));
        return dp[m][n];
    }

    public static String getLcsStr(int[][] mark, String str1, int m, int n) {
        StringBuilder sb = new StringBuilder();
        int i = m;
        int j = n;
        while (i >= 1 && j >= 1) {
            if (mark[i][j] == 1) {
                sb.insert(0, str1.charAt(i - 1));
                i--;
                j--;
            } else if (mark[i][j] == 2) {
                i--;
            } else if (mark[i][j] == 3) {
                j--;
            }
        }
        return sb.toString();
    }

    public static List<String> getLcsList(int[][] mark, String str1, int m, int n, int maxLen, int[][] dp) {
        List<String> res = new ArrayList<>();
        for (int i = m; i >= 1; i--) {
            for (int j = n; j >= 1; j--) {
                if (dp[i][j] < maxLen) {
                    break;
                }
                if (dp[i][j] == maxLen && mark[i][j] == 1) {
                    res.add(getLcsStr(mark, str1, i, j));
                }
            }
        }

        return res.stream().sorted().collect(Collectors.toList());
    }

    @Test
    public void test4() {
        int[] arr = {67, 81, 41, 84, 19, 68, 59, 76, 97, 30};
        CommonUtil.printIntArray(arr);
        heapSort(arr, 2);
        CommonUtil.printIntArray(arr);
    }

    /**
     * 堆排序-对parent位置的元素进行下层
     *
     * @param arr
     * @param len
     * @param parent
     * @param sortType 1小顶堆 2大顶堆
     */
    public static void adjustDown(int[] arr, int len, int parent, int sortType) {
        //父节点的元素值
        int parentVal = arr[parent];
        //子节点的左右下标
        int left = 2 * parent + 1;
        int right = left + 1;
        while (left < len) {
            //如果右节点比左节点值小-小顶堆
            if (left + 1 < len) {
                if (sortType == 1 && arr[left] > arr[right]) {
                    left++;
                }
                //如果右节点比左节点值大-大顶堆
                if (sortType == 2 && arr[left] < arr[right]) {
                    left++;
                }

            }
            if (sortType == 1 && parentVal <= arr[left]) {
                break;
            }
            if (sortType == 2 && parentVal >= arr[left]) {
                break;
            }
            //parent位置的值赋值为left parent位置改为left，left位置改为左子节点的位置
            if (parent != left) {
                arr[parent] = arr[left];
                parent = left;
                left = 2 * parent + 1;
            }


        }
        arr[parent] = parentVal;
    }

    public static int[] heapSort(int[] arr, int sortType) {
        //构建堆
        int len = arr.length;
        for (int i = len / 2 - 1; i >= 0; i--) {
            adjustDown(arr, len, i, sortType);
        }
        //堆排序-首位元素交换，且首元素下沉
        for (int i = len - 1; i >= 0; i--) {
            int end = arr[i];
            arr[i] = arr[0];
            arr[0] = end;
            adjustDown(arr, i, 0, sortType);
        }

        return arr;
    }


    /**
     * 在一个 n * m 的二维数组中，每一行都按照从左到右递增的顺序排序，每一列都按照从上到下递增的顺序排序。
     * 请完成一个高效的函数，输入这样的一个二维数组和一个整数，判断数组中是否含有该整数。
     * 示例:
     * 现有矩阵 matrix 如下：
     * [
     * [1,   4,  7, 11, 15],
     * [2,   5,  8, 12, 19],
     * [3,   6,  9, 16, 22],
     * [10, 13, 14, 17, 24],
     * [18, 21, 23, 26, 30]
     * ]
     * 给定 target=5，返回true。
     * 给定target=20，返回false
     * <p>
     * 来源：力扣（LeetCode）
     * 链接：https://leetcode-cn.com/problems/er-wei-shu-zu-zhong-de-cha-zhao-lcof
     */
    @Test
    public void test5() {
        String str = "[[1, 4, 7, 11, 15]," +
                "    [2, 5, 8, 12, 19]," +
                "    [3, 6, 9, 16, 22]," +
                "    [10,13,14,17, 24]," +
                "    [18,21,23,26, 30]] ";
        str = "[  [1, 2, 3, 4, 5]," +
                " [6, 7, 8, 9, 10]," +
                " [11,12,13,14,15]," +
                " [16,17,18,19,20]," +
                " [21,22,23,24,25]]  ";
//        str = "[[-1,3]]";
        str = "[[1,3,5,7,9],[2,4,6,8,10],[11,13,15,17,19],[12,14,16,18,20],[21,22,23,24,25]] ";
        str="[[1],[3],[5]]";
        JSONArray jsonArray = JSON.parseArray(str);
        int len = jsonArray.size();
        int n=1;
        int[][] arr = new int[len][n];
        for (int i = 0; i < jsonArray.size(); i++) {
            JSONArray o = (JSONArray) jsonArray.get(i);
            int[] arr2 = new int[n];
            for (int j = 0; j < n; j++) {
                arr2[j] = (int) o.get(j);
            }
            arr[i] = arr2;
        }
//        System.out.println(Arrays.asList(arr));
        System.out.println(findNumberIn2DArray(arr, 2));
        System.out.println(findNumberIn2DArray2(arr, 2));
    }

    /**
     * @param matrix
     * @param target
     * @return 遍历每行，对每行的数组使用二分查找
     */
    public boolean findNumberIn2DArray(int[][] matrix, int target) {
        int m = matrix.length;
        Integer n = checkMatrix(matrix, target, m);
        if (n == null) return false;
        for (int i = 0; i < m; i++) {
            int[] arr = matrix[i];
            if (target < arr[0] || target > arr[n - 1]) {
                continue;
            }
            if (binarySearch(arr, target, 0, n / 2, n - 1)) {
                return true;
            }

        }

//        int i = 0;
//        int j = 0;
//        while (i < m && j < n) {
//            if (matrix[i][j] == target) {
//                return true;
//            }
//            if (matrix[i][j] > target) {
//                i--;
//                j--;
//                int tmpI=i;
//                if(i<0||j<0){
//                    break;
//                }
//                //往下或右寻找
//                while (i<m){
//                    if (matrix[i][j] == target) {
//                        return true;
//                    }
//                    i++;
//                }
//                while (j<n){
//                    if (matrix[tmpI][j] == target) {
//                        return true;
//                    }
//                    j++;
//                }
//                break;
//            }
//            if(i+1<m &&j+1<n ){
//                i++;
//                j++;
//            }else if(i+1<m){
//                i++;
//            }else if(j+1<n){
//                j++;
//            }
//        }


        return false;
    }

    private Integer checkMatrix(int[][] matrix, int target, int m) {
        if (m == 0) {
            return null;
        }
        int n = matrix[0].length;
        if (n == 0) {
            return null;
        }
        if (target < matrix[0][0] || target > matrix[m - 1][n - 1]) {
            return null;
        }
        return n;
    }

    /**
     * 从右上角开始，定义arr[i][j]为右上角的元素
     * 如果target=arr[i][j]，直接返回true
     * 如果target<arr[i][j]，则往左移动一步，j--(j>=0)
     * 如果target>arr[i][j]，则往下移动一步，i++(i<m)
     * 直到左下角
     * 注意数组判空
     *
     * @param matrix
     * @param target
     * @return
     */
    public boolean findNumberIn2DArray2(int[][] matrix, int target) {
        int m = matrix.length;
        Integer n = checkMatrix(matrix, target, m);
        if (n == null) return false;
        int i = 0;
        int j = n - 1;
        while (i < m && j >=0) {
            if (target == matrix[i][j]) {
                return true;
            }
            if (target < matrix[i][j]) {
                j--;
            } else {
                i++;
            }
        }
        return false;
    }

    public static boolean binarySearch(int[] arr, int target, int l, int mid, int r) {
        exeCount++;
        if (mid < l || mid > r) {
            return false;
        }
        if (arr[mid] == target) {
            return true;
        }
        if (l == mid && mid == r) {
            return arr[mid] == target;
        }
        if (target < arr[mid]) {
            if (mid - 1 < 0) {
                return false;
            }
            return binarySearch(arr, target, l, (mid - 1 + l) / 2, mid - 1);
        } else {
            if (mid + 1 > r) {
                return false;
            }
            return binarySearch(arr, target, mid + 1, (r + mid + 1) / 2, r);
        }
    }

    @Test
    public void test6() {
        int[] arr = {1, 4, 7, 21, 22, 23, 24, 25, 30, 31, 36, 39, 45, 88};
        //2 2 3  3 2 3
        for (int i = 0; i < arr.length; i++) {
            exeCount = 0;
            System.out.println(arr[i] + ":" + binarySearch(arr, arr[i], 0, arr.length / 2, arr.length - 1)
                    + " exeCount:" + exeCount);

        }
        exeCount = 0;
        System.out.println(-1 + ":" + binarySearch(arr, -1, 0, arr.length / 2, arr.length - 1) + " exeCount:" + exeCount);
    }
}
