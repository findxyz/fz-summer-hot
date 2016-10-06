package xyz.fz.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import xyz.fz.domain.user.TUser;
import xyz.fz.service.role.RoleAuthService;

import javax.servlet.http.HttpSession;

/**
 * Created by fz on 2016/9/2.
 */
@Controller
@RequestMapping("/")
public class HomeController {

    @Autowired
    private RoleAuthService roleAuthService;

    @RequestMapping("/home")
    public String home(HttpSession session, Model model) {
        TUser user = (TUser) session.getAttribute("curUser");
        model.addAttribute("curUser", user);
        if (user.getRoleId() != null) {
            // done 获取当前用户权限列表
            if (user.getRoleId() == 0) {
                model.addAttribute("treeData", roleAuthService.getAllTreeData());
            } else {
                model.addAttribute("treeData", roleAuthService.getTreeData(user.getRoleId()));
            }
        }
        return "home";
    }
}
