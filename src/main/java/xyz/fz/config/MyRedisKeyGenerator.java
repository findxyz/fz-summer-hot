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
        for (int i = 0; i < params.length; i++) {
            Object param = params[i];
            key.append(param.toString());
            if (i != (params.length - 1)) {
                key.append("_");
            }
        }
        return key.toString();
    }
}
