package xyz.fz.controller.user;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import xyz.fz.dao.PagerData;
import xyz.fz.domain.user.TUser;
import xyz.fz.service.user.UserService;
import xyz.fz.utils.BaseUtil;

import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by fz on 2016/8/8.
 */
@Controller
@RequestMapping("/user")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping("/main")
    public String main() {

        return "user/main";
    }

    @RequestMapping("/userPageList")
    @ResponseBody
    public Map<String, Object> userPageList(@RequestParam(value = "draw", required = false, defaultValue = "0") int draw,
                                            @RequestParam(value = "start", required = false, defaultValue = "0") int start,
                                            @RequestParam(value = "length", required = false, defaultValue = "20") int length,
                                            @RequestParam("name") String name) {

        int curPage = (start / length);
        Map<String, Object> result = new HashMap<>();
        PagerData<Map> page = userService.userPageList(name, curPage, length);
        result.put("draw", draw);
        result.put("recordsTotal", page.getTotalCount());
        result.put("recordsFiltered", page.getTotalCount());
        result.put("data", page.getData());
        return result;
    }

    @RequestMapping("/saveUser")
    @ResponseBody
    public Map<String, Object> saveUser(TUser user) {

        Map<String, Object> result = new HashMap<>();
        user.setPassWord(BaseUtil.sha256Hex(user.getPassWord()));
        user.setCreateTime(new Date());
        user.setIsActivity(1);
        TUser sUser = userService.saveUser(user);
        if (sUser.getId() > 0) {
            result.put("success", true);
        } else {
            result.put("success", false);
        }
        return result;
    }

    @RequestMapping("/resetPassWord")
    @ResponseBody
    public Map<String, Object> resetPassWord(@RequestParam("id") Long id) {

        Map<String, Object> result = new HashMap<>();
        try {
            userService.resetPassWord(id);
            result.put("success", true);
        } catch (Exception e) {
            e.printStackTrace();
            result.put("success", false);
            result.put("message", e.getMessage());
        }
        return result;
    }

    @RequestMapping("/start")
    @ResponseBody
    public Map<String, Object> start(@RequestParam("id") Long id) {

        Map<String, Object> result = new HashMap<>();
        try {
            userService.start(id);
            result.put("success", true);
        } catch (Exception e) {
            e.printStackTrace();
            result.put("success", false);
            result.put("message", e.getMessage());
        }
        return result;
    }

    @RequestMapping("/stop")
    @ResponseBody
    public Map<String, Object> stop(@RequestParam("id") Long id) {

        Map<String, Object> result = new HashMap<>();
        try {
            userService.stop(id);
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
            userService.del(id);
            result.put("success", true);
        } catch (Exception e) {
            e.printStackTrace();
            result.put("success", false);
            result.put("message", e.getMessage());
        }
        return result;
    }

    @RequestMapping("/userRolePageList")
    @ResponseBody
    public Map<String, Object> userRolePageList(@RequestParam(value = "draw", required = false, defaultValue = "0") int draw,
                                                @RequestParam(value = "start", required = false, defaultValue = "0") int start,
                                                @RequestParam(value = "length", required = false, defaultValue = "20") int length,
                                                @RequestParam("userId") Long userId) {

        int curPage = (start / length);
        Map<String, Object> result = new HashMap<>();
        PagerData<Map> page = userService.userRolePageList(userId, curPage, length);
        result.put("draw", draw);
        result.put("recordsTotal", page.getTotalCount());
        result.put("recordsFiltered", page.getTotalCount());
        result.put("data", page.getData());
        return result;
    }

    @RequestMapping("/roleChange")
    @ResponseBody
    public Map<String, Object> roleChange(@RequestParam("roleId") Long roleId,
                                          @RequestParam("userId") Long userId) {

        Map<String, Object> result = new HashMap<>();
        try {
            userService.roleChange(roleId, userId);
            result.put("success", true);
        } catch (Exception e) {
            e.printStackTrace();
            result.put("success", false);
            result.put("message", e.getMessage());
        }
        return result;
    }

    @RequestMapping("/modifyMain")
    public String modifyMain() {

        return "user/modifyMain";
    }

    @RequestMapping("/modifyPassWord")
    @ResponseBody
    public Map<String, Object> modifyPassWord(@RequestParam("oldPassWord") String oldPassWord,
                                              @RequestParam("newPassWord") String newPassWord,
                                              HttpSession session) {
        Map<String, Object> result = new HashMap<>();
        try {
            TUser user = (TUser) session.getAttribute("curUser");
            userService.modifyPassWord(user.getId(), oldPassWord, newPassWord);
            result.put("success", true);
        } catch (Exception e) {
            e.printStackTrace();
            result.put("success", false);
            result.put("message", e.getMessage());
        }
        return result;
    }

}
