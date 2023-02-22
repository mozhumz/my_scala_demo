package com.hyj.util;

import java.util.concurrent.*;

public class ThreadPoolUtil {
    public static void main(String[] args) throws InterruptedException {
        ThreadPoolExecutor pool = new ThreadPoolExecutor(0, Integer.MAX_VALUE,
                60L, TimeUnit.SECONDS,
                new SynchronousQueue<Runnable>());
        Thread.sleep(5000);
        int count = 100000;
        CountDownLatch countDownLatch = new CountDownLatch(count);
        for (int i = 0; i < count; i++) {
            try {
                pool.execute(() -> {
                    try {
//                        System.out.println("" + Thread.currentThread().getName() + "-start...");
                        Thread.sleep(6000);
                    } catch (Exception e) {
                        System.out.println("thread-Exception:");
                        e.printStackTrace();
                    } finally {
                        countDownLatch.countDown();
//                        System.out.println("" + Thread.currentThread().getName() + "-end");
                    }
                });
            } catch (Exception e) {
                System.out.println("execute-Exception:");
                e.printStackTrace();
            }
        }
        countDownLatch.await();
        pool.shutdown();
        System.out.println("ok!");
    }
}
