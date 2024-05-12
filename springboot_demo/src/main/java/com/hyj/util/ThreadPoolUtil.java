package com.hyj.util;

import java.util.concurrent.*;

public class ThreadPoolUtil {
    public static void main(String[] args) throws InterruptedException {
        ThreadPoolExecutor pool = new ThreadPoolExecutor(4, Integer.MAX_VALUE,
                60L, TimeUnit.SECONDS,
                new LinkedBlockingDeque<>());
        int count = 10000000;
        CountDownLatch countDownLatch = new CountDownLatch(count);
        for (int i = 0; i < count; i++) {
            try {
                pool.execute(() -> {
                    try {
                        if(Thread.currentThread().isInterrupted()){
                            System.out.println("" + Thread.currentThread().getName() + "-isInterrupted");
                        }
//                        Thread.sleep(5000);
                        System.out.println("" + Thread.currentThread().getName() + "-start...");

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
        pool.shutdownNow();
        System.out.println("ok!");
    }
}
