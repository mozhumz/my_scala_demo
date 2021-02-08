package com.hyj.algorithm.demo;

import org.junit.Test;

import java.util.*;
import java.util.stream.Collectors;

public class CollectionSort {
    /**
     * map之entry排序
     */
    @Test
    public void mapSort() {
        Map<String, Integer> map = new HashMap<>();
        for (int i = 0; i < 10; i++) {
            map.put(i + "", new Random().nextInt(100));
        }
        System.out.println(map);
        System.out.println("----------------");
        mapSort(map);
    }

    public void mapSort(Map<String, Integer> map) {
        //o1-o2表示升序 o2-o1表示倒序
        List<Map.Entry<String, Integer>> list = map.entrySet().stream()
                .sorted((o1, o2) -> -(o1.getValue() - o2.getValue())).collect(Collectors.toList());
        System.out.println(list);
    }
}
