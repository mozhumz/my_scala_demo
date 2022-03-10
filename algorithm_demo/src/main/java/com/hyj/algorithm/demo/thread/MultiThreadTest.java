package com.hyj.algorithm.demo.thread;

import java.util.concurrent.*;

public class MultiThreadTest {
    public static void main(String[] args) {
        ExecutorService pool = Executors.newFixedThreadPool(10);
        LinkedBlockingQueue<Runnable> workQueue = new LinkedBlockingQueue<>();
        ThreadPoolExecutor pool2 = new ThreadPoolExecutor(4, 8,
                5L, TimeUnit.MINUTES,
                workQueue);


        int []a = {10};
        for (int i = 0; i < 20; i++) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    if(a[0]>0){
                        a[0]--;
//                        System.out.println(a[0]);
                    }
                }
            }).start();
        }
        System.out.println(a[0]);
    }
}
