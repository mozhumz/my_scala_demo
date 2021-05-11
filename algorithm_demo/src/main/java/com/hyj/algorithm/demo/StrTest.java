package com.hyj.algorithm.demo;

import org.junit.Test;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
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
}
