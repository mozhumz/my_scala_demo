package com.hyj.util;

import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

@Component
public class ThreadPoolManager {

    private ThreadPoolTaskExecutor taskExecutor;

    public ThreadPoolManager() {
        taskExecutor = new ThreadPoolTaskExecutor();
        taskExecutor.initialize();
        taskExecutor.shutdown();
    }

    public void executeTask(String taskId) {
        taskExecutor.submit(() -> {
            // 异步任务的逻辑
            // 可能是一个长时间运行的任务

            // 判断任务是否被取消
            if (Thread.currentThread().isInterrupted()) {
                System.out.println("Task " + taskId + " is canceled.");
                return;
            }

            // 执行任务逻辑
            System.out.println("Executing task " + taskId);
            try {
                Thread.sleep(5000);  // 模拟耗时操作
            } catch (InterruptedException e) {
                System.out.println("Task " + taskId + " is interrupted.");
                return;
            }
            System.out.println("Task " + taskId + " completed.");
        });
    }

    public void cancelTask(String taskId) {
        // 模拟取消任务
    }
}