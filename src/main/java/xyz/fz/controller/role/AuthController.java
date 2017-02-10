package xyz.fz.controller.role;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import xyz.fz.domain.role.TAuth;
import xyz.fz.service.role.AuthService;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by fz on 2016/8/8.
 */
@Controller
@RequestMapping("/role/auth")
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    private final AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @RequestMapping("/authPageList")
    @ResponseBody
    public Map<String, Object> authPageList(@RequestParam(value = "draw", required = false, defaultValue = "0") int draw,
                                            @RequestParam(value = "start", required = false, defaultValue = "0") int start,
                                            @RequestParam(value = "length", required = false, defaultValue = "20") int length,
                                            @RequestParam("menuId") Long menuId) {
        int curPage = (start / length);
        Map<String, Object> result = new HashMap<>();
        Page<TAuth> page = authService.authPageList(menuId, curPage, length);
        result.put("draw", draw);
        result.put("recordsTotal", page.getTotalElements());
        result.put("recordsFiltered", page.getTotalElements());
        result.put("data", page.getContent());
        return result;
    }

    @RequestMapping("/saveAuth")
    @ResponseBody
    public Map<String, Object> saveAuth(TAuth auth) {

        Map<String, Object> result = new HashMap<>();
        if (auth.getMenuId() == -1) {
            result.put("success", false);
        } else {
            auth.setIsActivity(1);
            TAuth sAuth = authService.saveAuth(auth);
            if (sAuth.getId() > 0) {
                result.put("success", true);
            } else {
                result.put("success", false);
            }
        }
        return result;
    }

    @RequestMapping("/toggle")
    @ResponseBody
    public Map<String, Object> toggle(@RequestParam("id") Long id,
                                      @RequestParam("isActivity") int isActivity) {
        Map<String, Object> result = new HashMap<>();
        try {
            authService.toggle(id, isActivity);
            result.put("success", true);
        } catch (Exception e) {
            e.printStackTrace();
            result.put("success", false);
            result.put("message", e.getMessage());
        }
        return result;
    }

    @RequestMapping("/del")
    @ResponseBody
    public Map<String, Object> del(@RequestParam("id") Long id) {

        Map<String, Object> result = new HashMap<>();
        try {
            authService.del(id);
            result.put("success", true);
        } catch (Exception e) {
            e.printStackTrace();
            result.put("success", false);
            result.put("message", e.getMessage());
        }
        return result;
    }

}
