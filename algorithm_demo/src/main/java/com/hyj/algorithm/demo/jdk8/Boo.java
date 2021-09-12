package com.hyj.algorithm.demo.jdk8;

import com.alibaba.fastjson.JSON;
import lombok.Data;

@Data
public class Boo  extends Aoo<Coo,Integer,Object>{
    private Coo param;
    private int param2;

    @Override
    public void start() {
        super.dealMqs(param,this::dealBooMsg);
        super.dealMqs(param,param2,this::dealBooMsg);
    }

    public Object dealBooMsg(Coo obj){
        System.out.println(JSON.toJSONString(obj));
        return obj+":ok";
    }

    public Object dealBooMsg(Coo obj,int param2){
        System.out.println(obj);
        System.out.println(param2);
        return obj+"_"+param2+":ok";
    }
}
