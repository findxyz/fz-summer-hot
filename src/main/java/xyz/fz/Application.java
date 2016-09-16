package xyz.fz;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import xyz.fz.interceptor.UserInterceptor;

@SpringBootApplication
@ServletComponentScan
@EnableCaching
public class Application extends WebMvcConfigurerAdapter {

    @Value("${server.context-path}")
    public final String basePath = null;

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        UserInterceptor userInterceptor = new UserInterceptor();
        userInterceptor.setBasePath(basePath);
        registry.addInterceptor(userInterceptor)
                .excludePathPatterns("/pubs/**")
                .excludePathPatterns("/login/**")
                .excludePathPatterns("/index/**")
                .addPathPatterns("/*/**");
    }

}
