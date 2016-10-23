package xyz.fz.controller.role;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import xyz.fz.domain.role.TMenu;
import xyz.fz.service.role.MenuService;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by fz on 2016/8/8.
 */
@Controller
@RequestMapping("/role/menu")
public class MenuController {

    private static final Logger logger = LoggerFactory.getLogger(MenuController.class);

    @Autowired
    private MenuService menuService;

    @RequestMapping("/main")
    public String main() {
        return "role/menuMain";
    }

    @RequestMapping("/menuPageList")
    @ResponseBody
    public Map<String, Object> menuPageList(@RequestParam(value = "draw", required = false, defaultValue = "0") int draw,
                                            @RequestParam(value = "start", required = false, defaultValue = "0") int start,
                                            @RequestParam(value = "length", required = false, defaultValue = "20") int length,
                                            @RequestParam(value = "name", required = false, defaultValue = "") String name) {
        int curPage = (start / length);
        Map<String, Object> result = new HashMap<>();
        Page<TMenu> page = menuService.menuPageList(name, curPage, length);
        result.put("draw", draw);
        result.put("recordsTotal", page.getTotalElements());
        result.put("recordsFiltered", page.getTotalElements());
        result.put("data", page.getContent());
        return result;
    }

    @RequestMapping("/saveMenu")
    @ResponseBody
    public Map<String, Object> saveMenu(TMenu menu) {

        Map<String, Object> result = new HashMap<>();
        menu.setIsActivity(1);
        TMenu sMenu = menuService.saveMenu(menu);
        if (sMenu.getId() > 0) {
            result.put("success", true);
        } else {
            result.put("success", false);
        }
        return result;
    }

    @RequestMapping("/toggle")
    @ResponseBody
    public Map<String, Object> toggle(@RequestParam("id") Long id,
                                      @RequestParam("isActivity") int isActivity) {
        Map<String, Object> result = new HashMap<>();
        try {
            menuService.toggle(id, isActivity);
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
            menuService.del(id);
            result.put("success", true);
        } catch (Exception e) {
            e.printStackTrace();
            result.put("success", false);
            result.put("message", e.getMessage());
        }
        return result;
    }

}
