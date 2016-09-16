package xyz.fz.interceptor;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import xyz.fz.domain.TUser;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by fz on 2016/9/15.
 */
@Component
public class UserInterceptor implements HandlerInterceptor {

    private String basePath;

    public void setBasePath(String basePath) {
        this.basePath = basePath;
    }

    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
        TUser user = (TUser) httpServletRequest.getSession().getAttribute("curUser");
        if (user == null) {
            String returnIndex = "<script>alert('请先登陆...');window.parent.location='" + basePath + "';</script>";
            httpServletRequest.setCharacterEncoding("utf-8");
            httpServletResponse.setContentType("text/html; charset=utf-8");
            httpServletResponse.getWriter().print(returnIndex);
            return false;
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }
}
