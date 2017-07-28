package xyz.fz.service.impl.async;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.fz.service.async.AsyncService;

import java.util.concurrent.Future;

/**
 * Created by Administrator on 2017/3/30.
 */
@Service
@Transactional
public class AsyncServiceImpl implements AsyncService {

    private static Logger logger = LoggerFactory.getLogger(AsyncServiceImpl.class);

    @Override
    @Async
    public Future<String> longTimeWork() throws InterruptedException {
        Thread.sleep(2000L);
        logger.warn("longTimeWork done");
        return new AsyncResult<>("done");
    }
}
