package xyz.fz.controller.role;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import xyz.fz.domain.role.TRole;
import xyz.fz.service.role.RoleService;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by fz on 2016/8/8.
 */
@Controller
@RequestMapping("/role")
public class RoleController {

    private static final Logger logger = LoggerFactory.getLogger(RoleController.class);

    @Autowired
    private RoleService roleService;

    @RequestMapping("/main")
    public String main() {
        return "role/main";
    }

    @RequestMapping("/rolePageList")
    @ResponseBody
    public Map<String, Object> rolePageList(@RequestParam(value = "draw", required = false, defaultValue = "0") int draw,
                                            @RequestParam(value = "start", required = false, defaultValue = "0") int start,
                                            @RequestParam(value = "length", required = false, defaultValue = "20") int length,
                                            @RequestParam("name") String name) {
        int curPage = (start / length);
        Map<String, Object> result = new HashMap<>();
        Page<TRole> page = roleService.rolePageList(name, curPage, length);
        result.put("draw", draw);
        result.put("recordsTotal", page.getTotalElements());
        result.put("recordsFiltered", page.getTotalElements());
        result.put("data", page.getContent());
        return result;
    }

    /*
    TRole role = new TRole();
    Class<?> c = role.getClass();
    Method m = c.getMethod("setRoleName", String.class);
    m.invoke(role, "myRole");
    System.out.println(role.getRoleName());
    */
    @RequestMapping("/saveRole")
    @ResponseBody
    public Map<String, Object> saveRole(TRole role) {

        Map<String, Object> result = new HashMap<>();
        role.setIsActivity(1);
        TRole sRole = roleService.saveRole(role);
        if (sRole.getId() > 0) {
            result.put("success", true);
        } else {
            result.put("success", false);
        }
        return result;
    }

}
