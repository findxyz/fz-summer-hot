package xyz.fz.controller.role;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import xyz.fz.dao.PagerData;
import xyz.fz.domain.role.TRoleAuth;
import xyz.fz.service.role.RoleAuthService;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by fz on 2016/8/8.
 */
@Controller
@RequestMapping("/role/roleAuth")
public class RoleAuthController {

    private static final Logger logger = LoggerFactory.getLogger(RoleAuthController.class);

    @Autowired
    private RoleAuthService roleAuthService;

    @RequestMapping("/roleAuthMenuPageList")
    @ResponseBody
    public Map<String, Object> roleAuthMenuPageList(@RequestParam(value = "draw", required = false, defaultValue = "0") int draw,
                                                    @RequestParam(value = "start", required = false, defaultValue = "0") int start,
                                                    @RequestParam(value = "length", required = false, defaultValue = "20") int length,
                                                    @RequestParam(value = "roleId", required = false, defaultValue = "0") Long roleId) {
        int curPage = (start / length);
        Map<String, Object> result = new HashMap<>();
        PagerData<Map> page = roleAuthService.roleAuthMenuPageList(roleId, curPage, length);
        result.put("draw", draw);
        result.put("recordsTotal", page.getTotalCount());
        result.put("recordsFiltered", page.getTotalCount());
        result.put("data", page.getData());
        return result;
    }

    @RequestMapping("/roleMenuPageList")
    @ResponseBody
    public Map<String, Object> roleMenuPageList(@RequestParam(value = "draw", required = false, defaultValue = "0") int draw,
                                                @RequestParam(value = "start", required = false, defaultValue = "0") int start,
                                                @RequestParam(value = "length", required = false, defaultValue = "20") int length,
                                                @RequestParam(value = "roleId", required = false, defaultValue = "0") Long roleId) {
        int curPage = (start / length);
        Map<String, Object> result = new HashMap<>();
        PagerData<Map> page = roleAuthService.roleMenuPageList(roleId, curPage, length);
        result.put("draw", draw);
        result.put("recordsTotal", page.getTotalCount());
        result.put("recordsFiltered", page.getTotalCount());
        result.put("data", page.getData());
        return result;
    }

    @RequestMapping("/saveRoleMenu")
    @ResponseBody
    public Map<String, Object> saveRoleMenu(TRoleAuth roleMenu) {

        Map<String, Object> result = new HashMap<>();
        roleMenu.setAuthId(0L);
        TRoleAuth sRoleMenu = roleAuthService.saveRoleAuth(roleMenu);
        if (sRoleMenu.getId() > 0) {
            result.put("success", true);
        } else {
            result.put("success", false);
        }
        return result;
    }

    @RequestMapping("/delRoleMenu")
    @ResponseBody
    public Map<String, Object> delRoleMenu(@RequestParam("roleId") Long roleId,
                                           @RequestParam("menuId") Long menuId) {

        Map<String, Object> result = new HashMap<>();
        try {
            roleAuthService.delRoleMenu(roleId, menuId);
            result.put("success", true);
        } catch (Exception e) {
            e.printStackTrace();
            result.put("success", false);
            result.put("message", e.getMessage());
        }
        return result;
    }

    @RequestMapping("/roleAuthPageList")
    @ResponseBody
    public Map<String, Object> roleAuthPageList(@RequestParam(value = "draw", required = false, defaultValue = "0") int draw,
                                                @RequestParam(value = "start", required = false, defaultValue = "0") int start,
                                                @RequestParam(value = "length", required = false, defaultValue = "20") int length,
                                                @RequestParam(value = "roleId", required = false, defaultValue = "0") Long roleId,
                                                @RequestParam(value = "menuId", required = false, defaultValue = "0") Long menuId) {
        int curPage = (start / length);
        Map<String, Object> result = new HashMap<>();
        PagerData<Map> page = roleAuthService.roleAuthPageList(roleId, menuId, curPage, length);
        result.put("draw", draw);
        result.put("recordsTotal", page.getTotalCount());
        result.put("recordsFiltered", page.getTotalCount());
        result.put("data", page.getData());
        return result;
    }

    @RequestMapping("/saveRoleAuth")
    @ResponseBody
    public Map<String, Object> saveRoleAuth(TRoleAuth roleAuth) {

        Map<String, Object> result = new HashMap<>();
        TRoleAuth sRoleAuth = roleAuthService.saveRoleAuth(roleAuth);
        if (sRoleAuth.getId() > 0) {
            result.put("success", true);
        } else {
            result.put("success", false);
        }
        return result;
    }

    @RequestMapping("/delRoleAuth")
    @ResponseBody
    public Map<String, Object> delRoleAuth(@RequestParam("roleAuthId") Long roleAuthId) {

        Map<String, Object> result = new HashMap<>();
        try {
            roleAuthService.delRoleAuth(roleAuthId);
            result.put("success", true);
        } catch (Exception e) {
            e.printStackTrace();
            result.put("success", false);
            result.put("message", e.getMessage());
        }
        return result;
    }
}
