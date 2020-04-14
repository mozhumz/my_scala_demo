package com.hyj.flume.source;

import org.apache.flume.Context;
import org.apache.flume.Event;
import org.apache.flume.EventDeliveryException;
import org.apache.flume.PollableSource;
import org.apache.flume.conf.Configurable;
import org.apache.flume.event.SimpleEvent;
import org.apache.flume.source.AbstractSource;

public class MySource extends AbstractSource implements Configurable, PollableSource {
    private String prefix;
    private String sufix;


    @Override
    public void configure(Context context) {
        prefix=context.getString("prefix");
        sufix=context.getString("suffix","suffix");
    }

    @Override
    public Status process() throws EventDeliveryException {
        Status status = null;

        try {
            // This try clause includes whatever Channel/Event operations you want to do

            // Receive new data
            for (int i = 0; i < 5; i++) {
                Event event=new SimpleEvent();
                event.setBody((prefix+"-"+i+"-"+sufix).getBytes());
                getChannelProcessor().processEvent(event);
            }

            // Store the Event into this Source's associated Channel(s)

            status = Status.READY;
        } catch (Exception t) {
            t.printStackTrace();
            // Log exception, handle individual exceptions as needed

            status = Status.BACKOFF;

            // re-throw all Errors
        }

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return status;
    }

    @Override
    public long getBackOffSleepIncrement() {
        return 0;
    }

    @Override
    public long getMaxBackOffSleepInterval() {
        return 0;
    }

    @Override
    public void start() {
        // Initialize the connection to the external client
    }

}
