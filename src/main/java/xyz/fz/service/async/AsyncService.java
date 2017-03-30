package xyz.fz.service.async;

import java.util.concurrent.Future;

/**
 * Created by Administrator on 2017/3/30.
 */
public interface AsyncService {
    Future<String> longTimeWork() throws InterruptedException;
}
