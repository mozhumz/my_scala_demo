package com.hyj.algorithm.demo.niuke;
import com.hyj.algorithm.demo.TenSort;

import java.util.Scanner;
public class TopKTest {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        // 注意 hasNext 和 hasNextLine 的区别
        while (in.hasNextInt()) { // 注意 while 处理多个 case
            int n = in.nextInt();
            int k = in.nextInt();
            int[]arr=new int[n];
            int[]res=new int[k];
            for(int i=0;i<n;i++){
                arr[i]=in.nextInt();
                if(i<k){
                    res[i]=arr[i];
                }
            }
//            TenSort.heapSort(res,k,2);
            adjustDown(res);
            for(int j=k;j<n;j++){
                if(arr[j]<res[0]){
                    res[0]=arr[j];
                    adjustDown(res,k,0);
                }
            }
            heapSort(res,k);
            StringBuilder sb=new StringBuilder();
            for(int i=0;i<k;i++){
                if(i<k-1){
                    sb.append(res[i]+" ");
                }else{
                    sb.append(res[i]);
                }
            }
            System.out.println(sb);
            sb.substring(1,2);

        }
    }

    //大顶堆
    public static void adjustDown(int[]arr){
        int len=arr.length;
        //调整堆
        for(int i=len/2-1;i>=0;i--){
            adjustDown(arr,len,i);
        }


    }
    //堆排序 首尾元素交换
    private static void heapSort(int[] arr, int len) {
        for(int i = len -1; i>=1; i--){
            int tmp= arr[i];
            arr[i]= arr[0];
            arr[0]=tmp;
            adjustDown(arr,i,0);
        }
    }

    //父元素下沉 父节点值改为child 父节点位置改为child child位置更新
    public static void adjustDown(int[]arr,int len,int parent){
        int left=2*parent+1;
        //记录父节点的值
        int tmp=arr[parent];
        while(left<len){
            //2个子节点比较大小
            if(left+1<len){
                if(arr[left]<arr[left+1]){
                    left++;
                }
            }
            //父节点已经最大
            if(tmp>=arr[left]){
                break;
            }
            if(parent!=left){
                arr[parent]=arr[left];
                parent=left;
                left=parent*2+1;
            }
        }
        arr[parent]=tmp;
    }
}
