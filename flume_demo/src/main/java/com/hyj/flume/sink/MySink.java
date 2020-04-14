package com.hyj.flume.sink;

import org.apache.flume.*;
import org.apache.flume.conf.Configurable;
import org.apache.flume.sink.AbstractSink;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MySink extends AbstractSink implements Configurable {
    private Logger logger= LoggerFactory.getLogger(MySink.class);

    @Override
    public void configure(Context context) {

    }


    @Override
    public Status process() throws EventDeliveryException {

        Status status = null;
        Channel ch = getChannel();
        Transaction txn = ch.getTransaction();
        txn.begin();
        try {
            // This try clause includes whatever Channel operations you want to do
            Event event = ch.take();
            if(event!=null){

                logger.info(new String(event.getBody()));
            }
            // Send the Event to the external repository.
            // storeSomeData(e);

            txn.commit();
            status = Status.READY;
        } catch (Exception t) {
            txn.rollback();
            logger.error("err:"+t);
            // Log exception, handle individual exceptions as needed

            status = Status.BACKOFF;

            // re-throw all Errors
        }finally {
                txn.close();
        }
        return status;

    }
}
