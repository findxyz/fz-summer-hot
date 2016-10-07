package xyz.fz;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import xyz.fz.interceptor.AuthInterceptor;
import xyz.fz.service.role.RoleAuthService;

@SpringBootApplication
@ServletComponentScan
@EnableCaching
public class Application extends WebMvcConfigurerAdapter {

    @Value("${server.context-path}")
    public final String basePath = null;

    @Autowired
    private RoleAuthService roleAuthService;

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        AuthInterceptor authInterceptor = new AuthInterceptor(roleAuthService);
        authInterceptor.setBasePath(basePath);
        registry.addInterceptor(authInterceptor)
                .excludePathPatterns("/pubs/**")
                .excludePathPatterns("/login/**")
                .excludePathPatterns("/logout/**")
                .excludePathPatterns("/index/**")
                .excludePathPatterns("/error/**")
                .addPathPatterns("/*/**");
    }

}
