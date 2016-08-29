package xyz.fz.filter;

import org.springframework.beans.factory.annotation.Value;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import java.io.IOException;

/**
 * Created by fz on 2016/8/12.
 */
@WebFilter(filterName = "basePathFilter", urlPatterns = "/*")
public class BasePathFilter implements Filter{

    @Value("${server.context-path}")
    public String basePath;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {}

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        servletRequest.setAttribute("basePath", basePath);
        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {}
}
