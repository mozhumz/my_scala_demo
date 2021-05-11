package com.hyj.algorithm.demo;

import java.util.Scanner;
import java.util.*;
public class NiuKeTest {

    public static void main(String[] args){
        Scanner sc = new Scanner(System.in);
        //总余额
        int m = sc.nextInt();
        //物品数
        int n = sc.nextInt();
        //单价
        int[] v = new int[n+1];
        //权重
        int[] p = new int[n+1];
        //主从 0主
        int[] q = new int[n+1];
        int groups = 0;
        for(int i = 1; i<=n; i++){
            v[i] = sc.nextInt();
            p[i] = sc.nextInt();
            q[i] = sc.nextInt();
            if(q[i] == 0) {
                groups++;
            }
        }

        //分组
        int[][] _v = new int[groups +1][4];
        int[][] _p = new int[groups +1][4];
        processData(q, v, p, _v, _p);

        int gc = _v.length;
        int[][] r = new int[gc][m+1];
        for(int i = 1; i< gc; i++){
            for(int j = 1; j<= m; j++){

                //本组不取时：变换为新的子问题
                int max = r[i-1][j];
                //本组选取时：每组分别取1 2 3 个物品时的权值
                for (int t = 1; t < 4; t++) {
                    int tempv = _v[i][t];
                    int tempp = _p[i][t];
                    if(tempv != 0 && tempv <= j) {
                        max = Math.max(max, r[i - 1][j - tempv] + tempp);
                    }
                }
                r[i][j] = max;
            }
        }
        System.out.println(r[gc -1][m]);
    }

    private static void processData(int[] q, int[] v, int[] p, int[][] _v, int[][] _p) {
        Map<Integer, List<Integer>> groups = new HashMap<>();
        for (int i = 1; i < q.length; i++) {
            //主
            if(q[i] == 0 ) {
                if(!groups.containsKey(i)) {
                    List<Integer> temp = new ArrayList<Integer>();
                    temp.add(i);
                    groups.put(i, temp);
                }

            }else {
                //附属
                if (groups.containsKey(q[i])) {
                    List<Integer> list = groups.get(q[i]);
                    list.add(i);
                }else {
                    List<Integer> temp = new ArrayList<Integer>();
                    temp.add(q[i]);
                    temp.add(i);
                    groups.put(q[i], temp);
                }
            }
        }
        int index = 1;
        for(List<Integer> list : groups.values()) {
            int size = list.size();
            //每组的最大权值
            if(size == 1) {
                _v[index][1] = v[list.get(0)];
                _p[index][1] = p[list.get(0)] * v[list.get(0)];
            }else if (size == 2) {
                _v[index][1] = v[list.get(0)];
                _p[index][1] = p[list.get(0)] * v[list.get(0)];

                _v[index][2] = v[list.get(0)] + v[list.get(1)];
                _p[index][2] = p[list.get(0)] * v[list.get(0)] + p[list.get(1)] * v[list.get(1)];
            }else {
                _v[index][1] = v[list.get(0)];
                _p[index][1] = p[list.get(0)]* v[list.get(0)];

                _v[index][2] = v[list.get(0)] + v[list.get(1)];
                _p[index][2] = p[list.get(0)] * v[list.get(0)] + p[list.get(1)] * v[list.get(1)];

                _v[index][3] = v[list.get(0)] + v[list.get(1)] + v[list.get(2)];
                _p[index][3] = p[list.get(0)] * v[list.get(0)]  + p[list.get(1)]* v[list.get(1)] + p[list.get(2)]* v[list.get(2)];
            }
            index++;
        }
    }

}
