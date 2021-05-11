package com.hyj.algorithm.demo.thread;

import java.util.Scanner;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Thread4Test {
    public static char[] arr;
    public static CountDownLatch countDownLatch;
    public static void main(String[] args) throws InterruptedException {
        Scanner in = new Scanner(System.in);
        // 注意 hasNext 和 hasNextLine 的区别
        while (in.hasNextInt()) { // 注意 while 处理多个 case
            int a = in.nextInt();
            ExecutorService pool= Executors.newFixedThreadPool(4);
            pool.submit(new ThreadFun1(a));
            pool.submit(new ThreadFun2());
            pool.submit(new ThreadFun3());
            pool.submit(new ThreadFun4());
            countDownLatch.await();
            pool.shutdown();


            System.out.println(new String(arr) );
        }
    }

    public static int setArr(int index){
        int type=index%4;
        char ch='A';
        switch(type){
            case 0:
                ch='A';
                break;
            case 1:
                ch='B';
                break;
            case 2:
                ch='C';
                break;
            case 3:
                ch='D';
                break;
        }
        while(index<arr.length){
            arr[index]=ch;
            index+=4;
        }
        countDownLatch.countDown();
        return index;
    }

    public static class ThreadFun1 implements Runnable{
        ThreadFun1(int num){
            arr=new char[num*4];
            countDownLatch=new CountDownLatch(4);
        }
        public void run(){
            setArr(0);
        }
    }

    public static class ThreadFun2 implements Runnable{
        public void run(){
            setArr(1);

        }
    }

    public static class ThreadFun3 implements Runnable{
        public void run(){
            setArr(2);

        }
    }

    public static class ThreadFun4 implements Runnable{
        public void run(){
            setArr(3);

        }
    }

}
