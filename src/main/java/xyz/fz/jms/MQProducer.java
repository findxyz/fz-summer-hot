package xyz.fz.jms;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.stereotype.Component;

import static xyz.fz.config.ActiveMqConfiguration.SAMPLE_QUEUE;

/**
 * Created by Administrator on 2017/6/15.
 */
@Component
public class MQProducer {

    private final JmsMessagingTemplate jmsMessagingTemplate;

    @Autowired
    public MQProducer(JmsMessagingTemplate jmsMessagingTemplate) {
        this.jmsMessagingTemplate = jmsMessagingTemplate;
    }

    public void sendMessage(String queueName, String message) {
        jmsMessagingTemplate.convertAndSend(queueName, message);
    }

    public void sendSampleQueueMessage(String message) {
        sendMessage(SAMPLE_QUEUE, message);
    }
}
