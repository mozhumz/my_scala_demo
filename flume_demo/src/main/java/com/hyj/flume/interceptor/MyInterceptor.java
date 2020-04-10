package com.hyj.flume.interceptor;

import org.apache.commons.collections.CollectionUtils;
import org.apache.flume.Context;
import org.apache.flume.Event;
import org.apache.flume.interceptor.Interceptor;

import java.util.ArrayList;
import java.util.List;

public class MyInterceptor implements Interceptor {

    private List<Event> eventList;

    @Override
    public void initialize() {
        eventList = new ArrayList<>();
    }

    @Override
    public Event intercept(Event event) {
        String body = new String(event.getBody());
        if (body.contains("hello")) {
            event.getHeaders().put("hyj", "mozhu");
        } else {
            event.getHeaders().put("hyj", "gcd");
        }
        return event;
    }

    @Override
    public List<Event> intercept(List<Event> list) {
//        eventList.clear();
        if (CollectionUtils.isNotEmpty(list)) {
            list.forEach(event -> {
                intercept(event);
//                eventList.add(event);
            });
        }
        return list;
    }

    @Override
    public void close() {

    }

    public static class BuilderHyj implements Interceptor.Builder {

        @Override
        public Interceptor build() {
            return new MyInterceptor();
        }

        @Override
        public void configure(Context context) {

        }
    }
}
