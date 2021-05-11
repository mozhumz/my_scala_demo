package com.hyj.algorithm.demo;

import org.junit.Test;

import java.util.*;
import java.util.stream.Collectors;

public class HyjTest {
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
        heapSort(arr,2);
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
            if(left+1<len){
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
        for (int i = len-1; i >=0; i--) {
            int end=arr[i];
            arr[i]=arr[0];
            arr[0]=end;
            adjustDown(arr,i,0,sortType);
        }

        return arr;
    }

}
