package xyz.fz.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import xyz.fz.domain.TUser;
import xyz.fz.service.UserService;
import xyz.fz.utils.BaseUtil;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by fz on 2016/8/8.
 */
@Controller
@RequestMapping("/user")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    @RequestMapping("/main")
    public String main() {

        return "user/main";
    }

    @RequestMapping("/userAllMapList")
    @ResponseBody
    public List<Map<String, Object>> userAllMapList() {

        return userService.userAllMapList();
    }

    @RequestMapping("/userPageList")
    @ResponseBody
    public Map<String, Object> userPageList(@RequestParam(value = "draw", required = false, defaultValue = "0") int draw,
                                            @RequestParam(value = "start", required = false, defaultValue = "0") int start,
                                            @RequestParam(value = "length", required = false, defaultValue = "20") int length,
                                            @RequestParam("userName") String userName) {

        int curPage = (start / length);
        Map<String, Object> result = new HashMap<>();
        Page<TUser> page = userService.userPageList(userName, curPage, length);
        result.put("draw", draw);
        result.put("recordsTotal", page.getTotalElements());
        result.put("recordsFiltered", page.getTotalElements());
        result.put("data", page.getContent());
        return result;
    }

    @RequestMapping("/saveUser")
    @ResponseBody
    public Map<String, Object> saveUser(TUser user) {

        Map<String, Object> result = new HashMap<>();
        user.setPassWord(BaseUtil.sha1Hex(user.getPassWord()));
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
        }
        return result;
    }

}
