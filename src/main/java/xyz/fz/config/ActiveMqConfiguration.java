package xyz.fz.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.jms.core.JmsTemplate;


/**
 * Created by Administrator on 2017/6/15.
 */
@Configuration
public class ActiveMqConfiguration {

    public static final String SAMPLE_QUEUE = "sample.queue";

    @Bean
    @Autowired
    public JmsMessagingTemplate jmsMessagingTemplate(JmsTemplate jmsTemplate) {
        return new JmsMessagingTemplate(jmsTemplate);
    }
}
