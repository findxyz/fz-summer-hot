package xyz.fz.config;

import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.google.code.kaptcha.util.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

/**
 * Created by Administrator on 2016/10/24 0024.
 */
@Configuration
public class KaptchaConfiguration {

    @Bean("googleKaptcha")
    public DefaultKaptcha googleKaptcha() {

        Properties properties = new Properties();
        properties.setProperty("kaptcha.image.width", "150");
        properties.setProperty("kaptcha.image.height", "60");
        properties.setProperty("kaptcha.noise.color", "yellow");
        properties.setProperty("kaptcha.textproducer.char.string", "1234567890");
        properties.setProperty("kaptcha.textproducer.char.length", "4");
        properties.setProperty("kaptcha.textproducer.font.color", "blue");
        Config config = new Config(properties);
        DefaultKaptcha kaptcha = new DefaultKaptcha();
        kaptcha.setConfig(config);
        return kaptcha;
    }
}
