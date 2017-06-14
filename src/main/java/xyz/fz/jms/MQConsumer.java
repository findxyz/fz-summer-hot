package xyz.fz.jms;

import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import static xyz.fz.config.ActiveMqConfiguration.SAMPLE_QUEUE;

/**
 * Created by Administrator on 2017/6/15.
 */
@Component
public class MQConsumer {

    @JmsListener(destination = SAMPLE_QUEUE)
    public void receiveSampleQueueMessage(String message) {
        System.out.println(message);
    }
}
