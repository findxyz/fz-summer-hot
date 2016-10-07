package xyz.fz.interceptor;

import org.apache.commons.lang.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import xyz.fz.domain.user.TUser;
import xyz.fz.service.role.RoleAuthService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.Set;

/**
 * Created by fz on 2016/9/15.
 */
public class AuthInterceptor implements HandlerInterceptor {

    private String basePath;

    public void setBasePath(String basePath) {
        this.basePath = basePath;
    }

    private RoleAuthService roleAuthService;

    public AuthInterceptor() {}

    public AuthInterceptor(RoleAuthService roleAuthService) {
        this.roleAuthService = roleAuthService;
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
        TUser user = (TUser) httpServletRequest.getSession().getAttribute("curUser");
        String requestURI = httpServletRequest.getRequestURI();
        int qIndex = requestURI.indexOf("?");
        if (qIndex != -1) {
            requestURI = requestURI.substring(0, qIndex);
        }
        if (user == null) {
            String returnIndex = "<script>alert('请先登陆...');window.parent.location='" + basePath + "';</script>";
            httpServletRequest.setCharacterEncoding("utf-8");
            httpServletResponse.setContentType("text/html; charset=utf-8");
            httpServletResponse.getWriter().print(returnIndex);
            return false;
        } else {
            if (user.getRoleId() == null) {
                String message = "当前用户尚未分配角色";
                httpServletRequest.setCharacterEncoding("utf-8");
                httpServletResponse.setContentType("text/html; charset=utf-8");
                httpServletResponse.getWriter().print(message);
                return false;
            }
            // 超级管理员
            if (user.getRoleId() == 0) {
                return true;
            }
            // 登陆后即可访问/home权限
            String loginInAccessURI = "/home";
            if (!StringUtils.equals(loginInAccessURI, requestURI)) {
                String[] requestURIs = requestURI.split("/");
                // done 获取当前用户角色所能访问的权限
                Map<String, Object> roleAuthMap = roleAuthService.getRoleAuthMap(user.getRoleId());
                Set<String> menuSet = (Set<String>) roleAuthMap.get("menuSet");
                Set<String> authNoSet = (Set<String>) roleAuthMap.get("authNoSet");
                // done 判断菜单是否可以访问，菜单为第一个/path路径，若没有菜单权限，则之后的权限路径统统禁止访问
                if (menuSet == null || !menuSet.contains("/" + requestURIs[1])) {
                    String message = "当前用户没有该菜单的访问权限";
                    httpServletRequest.setCharacterEncoding("utf-8");
                    httpServletResponse.setContentType("text/html; charset=utf-8");
                    httpServletResponse.getWriter().print(message);
                    return false;
                }
                // done 匹配不可访问的权限，匹配上则不可访问
                if (authNoSet != null && authNoSet.contains(requestURI)) {
                    String message = "当前用户没有该请求的访问权限";
                    httpServletRequest.setCharacterEncoding("utf-8");
                    httpServletResponse.setContentType("text/html; charset=utf-8");
                    httpServletResponse.getWriter().print(message);
                    return false;
                }
            }
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
