package xyz.fz.service.cache;

import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * Created by fz on 2016/10/5.
 */
@Component("myCKG")
public class MyCacheKeyGenerator implements KeyGenerator {
    @Override
    public Object generate(Object o, Method method, Object... objects) {
        StringBuilder keyBuilder = new StringBuilder();
        keyBuilder.append(method.getName());
        for (Object param : objects) {
            keyBuilder.append("#").append(param.toString());
        }
        return keyBuilder.toString();
    }
}
