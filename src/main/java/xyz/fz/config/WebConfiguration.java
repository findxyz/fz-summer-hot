package xyz.fz.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import xyz.fz.interceptor.AuthInterceptor;
import xyz.fz.service.role.RoleAuthService;

/**
 * Created by Administrator on 2017/3/30.
 */
@Configuration
public class WebConfiguration extends WebMvcConfigurerAdapter {

    @Value("${server.context-path}")
    public final String basePath = null;

    private final RoleAuthService roleAuthService;

    @Autowired
    public WebConfiguration(RoleAuthService roleAuthService) {
        this.roleAuthService = roleAuthService;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        AuthInterceptor authInterceptor = new AuthInterceptor(roleAuthService);
        authInterceptor.setBasePath(basePath);
        registry.addInterceptor(authInterceptor)
                .excludePathPatterns("/test/**")
                .excludePathPatterns("/pubs/**")
                .excludePathPatterns("/login/**")
                .excludePathPatterns("/logout/**")
                .excludePathPatterns("/index/**")
                .excludePathPatterns("/error/**")
                .addPathPatterns("/*/**");
    }

}
