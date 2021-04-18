package com.hyj.spark.offline.dataskew;

import java.io.*;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class GenBigFileTest {
    public static void main(String[] args) throws Exception{
        long ms=System.currentTimeMillis();
        System.out.println(ms);
        ExecutorService pool = Executors.newFixedThreadPool(5);
        for(int i=0;i<5;i++){
            final int j=i;
            pool.submit(() -> {
                try {
                    File file = new File("G:\\tmp\\spark/num_"+j+"");
                    genBigFile(file,j);
                }catch (Exception e){
                    System.out.println("err:"+j);
                    e.printStackTrace();
                }
            });

        }
        pool.shutdown();
        System.out.println("usedTime-seconds:"+(System.currentTimeMillis()-ms)/1000);
    }

    private static void genBigFile(File file,int thread) throws IOException {
        if (!file.exists()) {
            file.createNewFile();
        }
        FileOutputStream fos = new FileOutputStream(file);
        OutputStreamWriter osw = new OutputStreamWriter(fos, "utf-8");
        BufferedWriter out = new BufferedWriter(osw);
        Random r = new Random();

        long i = 0L;
        while(i<15000000L){
            i++;
            for(int j=0;j<10;j++){

                out.write(Integer.toString(r.nextInt()));
                out.write(",");

            }
            out.newLine();
            if(i%100000 ==0){
                out.flush();
                System.out.println("thread:"+thread+","+i);
            }
        }
        out.close();
        System.out.println("数据生成到"+file);
    }
}
