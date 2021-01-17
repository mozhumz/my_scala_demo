package com.hyj.algorithm.demo.thread;

public class MultiThreadTest {
    public static void main(String[] args) {
        int []a = {10};
        for (int i = 0; i < 20; i++) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    if(a[0]>0){
                        a[0]=a[0]-1;
//                        System.out.println(a[0]);
                    }
                }
            }).start();
        }
        System.out.println(a[0]);
    }
}
