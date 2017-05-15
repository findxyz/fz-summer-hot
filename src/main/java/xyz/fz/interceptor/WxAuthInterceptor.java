package xyz.fz.interceptor;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Created by fz on 2016/9/15.
 */
public class WxAuthInterceptor implements HandlerInterceptor {

    private String basePath;

    public void setBasePath(String basePath) {
        this.basePath = basePath;
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
        HttpSession session = httpServletRequest.getSession();
        if (session.getAttribute("openid") == null) {
            String returnWxAppIndex = "<script>window.parent.location='" + basePath + "wxApp/index';</script>";
            httpServletRequest.setCharacterEncoding("utf-8");
            httpServletResponse.setContentType("text/html; charset=utf-8");
            httpServletResponse.getWriter().print(returnWxAppIndex);
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
