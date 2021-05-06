package com.hyj.algorithm.demo;

import java.util.Arrays;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CommonUtil {
    public static void printObjArray(Object[]arr){
        System.out.println(Arrays.toString(arr));
    }

    public static void printIntArray(int[]arr){
        System.out.println(Arrays.toString(arr));
    }

    /**
     * 生成随机数组
     * @param l
     * @return
     */
    public static int[] getIntArr(Integer l) {
        if(l==null){
            l = 10;
        }
        int[] arr = new int[l];
        for (int i = 0; i < l; i++) {
            arr[i] = new Random().nextInt(100);
        }
        return arr;
    }

    /**
     * 生成有序数组
     * @param len
     * @return
     */
    public static int[] genSortedArr(int len){
        int[]arr=new int[len];
        for(int i=0;i<len;i++){
            arr[i]=i;
        }
        return arr;
    }

    /**
     * 去除单词中的其他字符 只保留字母和数字
     * @param word
     * @return
     */
    public static String trimWord(String word,int wordType){
        String s ;
        if(wordType==2){
            //中文
            s="([\u4e00-\u9fa5]+)";
        }else {
            //英文
            s="\\w+";
        }
        Pattern pattern = Pattern.compile(s);
        Matcher ma = pattern.matcher(word);
        boolean isWord=ma.find();
        if(isWord){
            return ma.group();
        }
        return null;
    }

}
