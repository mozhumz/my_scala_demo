package com.hyj.algorithm.demo.jdk8;

import java.util.function.BiFunction;
import java.util.function.Function;

public abstract class Aoo<T,U, R> {


    public StringBuilder create(String param){
        if(param==null){
            param="";
        }
        return new StringBuilder(param);
    }

    public  abstract void start();


    public  void dealMqs(T t, Function<T, R> function){
        System.out.println(function.apply(t) );
    }

    public void dealMqs(T t, U u, BiFunction<T,U,R>biFunction){
        System.out.println(biFunction.apply(t,u));
    }
}
