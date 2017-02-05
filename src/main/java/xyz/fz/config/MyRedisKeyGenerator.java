package xyz.fz.config;

import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * Created by Administrator on 2017/2/5.
 */
@Component
public class MyRedisKeyGenerator implements KeyGenerator {

    @Override
    public Object generate(Object target, Method method, Object... params) {

        StringBuilder key = new StringBuilder("");
        for (Object param : params) {
            key.append(param.toString()).append("_");
        }
        return key.toString();
    }
}
