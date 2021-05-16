package com.hyj.algorithm.demo;

import org.junit.Test;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StrTest {
    @Test
    public void testWorkReg() {
        String str = "ssSS2   ,”hello";

        String s = "\\w+";
        Pattern pattern = Pattern.compile(s);
        Matcher ma = pattern.matcher(str);
        while (ma.find()) {
            System.out.println(ma.group());
        }
        String str2=" he llo haha";
        System.out.println(str2.substring(str2.lastIndexOf(" ")+1));
        String str3="ABCDABCDABCDABCDABCDABCDABCDABCDABCDABCD";
        int count=0;
        for(char ch:str3.toCharArray()){
            if(ch=='A'){
                count++;
            }
        }
        System.out.println(Arrays.asList("192.168.2.1".split("[.]")));
        String s0511="abcd12345ed125ss123058789";
        String reg="\\d+";
        Pattern pattern1= Pattern.compile(reg);
        Matcher matcher = pattern1.matcher(s0511);
        while (ma.find()){
            System.out.println(matcher.group());
        }
//        Character.isDigit()


    }

    @Test
    public void test2(){
        String str="abc";
        System.out.println(check("bca",str));
    }

    //判断是否是兄弟单词
    public static boolean check(String x,String word){
        if(x.equals(word)){
            return false;
        }
        Map<Character,Integer> wMap=new HashMap();
        setMapByStr(wMap,word);

        for(char ch:x.toCharArray()){
            int count= wMap.getOrDefault(ch,0);
            if(count==0){
                return false;
            }
            wMap.put(ch,--count);

        }

        return true;
    }

    public static void setMapByStr(Map<Character,Integer> xMap, String x){
        for(char ch:x.toCharArray()){
            int c=xMap.getOrDefault(ch,0);
            xMap.put(ch,++c);
        }
    }
}
