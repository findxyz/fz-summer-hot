package xyz.fz.task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Created by Administrator on 2017/5/15.
 */
@Component
public class WxAccessTokenTask {

    private static final Logger logger = LoggerFactory.getLogger(WxAccessTokenTask.class);

    @Scheduled(fixedRate = 60 * 60 * 1000)
    private void refresh() {
        // 使用缓存的自动过期来保证accessToken的刷新，定时任务方案暂时摒弃
        System.out.println("abc");
    }
}
