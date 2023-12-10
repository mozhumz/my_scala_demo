package com.hyj.algorithm.demo.leetcode;

import org.junit.Test;

public class ZigZag {
    @Test
    public void test() {
        System.out.println(convert("PAYPALISHIRING",4));
    }

    public String convert(String s, int numRows) {
        if (s.length() == 1 || numRows == 1) {
            return s;
        }
        StringBuilder res = new StringBuilder();
        StringBuilder[]ss=new StringBuilder[numRows];
        for (int i = 0; i < ss.length; i++) {
            ss[i]=new StringBuilder();
        }
        int rowIndex=0;
        boolean downFlag = false;
        for (int i = 0; i < s.length(); i++) {
            ss[rowIndex].append(s.charAt(i));
            if (i % (numRows - 1) == 0) {
                downFlag = !downFlag;
            }
            if (downFlag) {
                rowIndex++;
            } else {
                rowIndex--;
            }
        }
        for (StringBuilder stringBuilder : ss) {
            res.append(stringBuilder);
        }
        return res.toString();
    }
}
