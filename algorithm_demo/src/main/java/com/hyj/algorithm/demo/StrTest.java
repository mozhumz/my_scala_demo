package com.hyj.algorithm.demo;

import org.junit.Test;

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
    }
}
