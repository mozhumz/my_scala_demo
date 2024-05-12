package com.hyj.algorithm.demo.jdk8;

import org.junit.Test;

import java.util.function.Function;

public class FunctionTest {
    public static void main(String[] args) {
        Function<String,String>func=new FunctionTest()::test1;
        test0(func,"lisi");

    }

    public static void test0(Function<String,String>function,String param){
        System.out.println(function.apply(param));
    }

    public  String test1(String str){
        return str+"haha";
    }

    @Test
    public void test20210912(){
        Boo boo = new Boo();
        Coo coo = new Coo();
        coo.setName("lisi");
        boo.setParam(coo);
        boo.start();
        new Thread().interrupt();
    }
}
