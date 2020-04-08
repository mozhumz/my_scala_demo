package com.hyj.flume.interceptor;

import org.apache.commons.collections.CollectionUtils;
import org.apache.flume.Context;
import org.apache.flume.Event;
import org.apache.flume.interceptor.Interceptor;

import java.util.List;

public class MyInterceptor implements Interceptor {


    @Override
    public void initialize() {

    }

    @Override
    public Event intercept(Event event) {
        String body=new String(event.getBody());
        if(body.contains("hello")){
            event.getHeaders().put("hyj","hyj");
        }else {
            event.getHeaders().put("hyj","gcd");
        }
        return event;
    }

    @Override
    public List<Event> intercept(List<Event> list) {
        if(CollectionUtils.isNotEmpty(list)){
            list.forEach(this::intercept);
        }
        return list;
    }

    @Override
    public void close() {

    }

    public static class BuilderHyj implements Interceptor.Builder{

        @Override
        public Interceptor build() {
            return null;
        }

        @Override
        public void configure(Context context) {

        }
    }
}
