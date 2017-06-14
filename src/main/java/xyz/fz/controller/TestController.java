package xyz.fz.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import xyz.fz.jms.MQProducer;

/**
 * Created by Administrator on 2017/6/15.
 */
@RestController
@RequestMapping("/test")
public class TestController {

    @Autowired
    private MQProducer mqProducer;

    @RequestMapping("/p/{message}")
    public String produce(@PathVariable String message) {
        mqProducer.sendSampleQueueMessage(message);
        return "message send ok: " + message;
    }

}
