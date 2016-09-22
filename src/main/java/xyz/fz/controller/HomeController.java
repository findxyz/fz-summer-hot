package xyz.fz.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import xyz.fz.domain.user.TUser;

import javax.servlet.http.HttpSession;

/**
 * Created by fz on 2016/9/2.
 */
@Controller
@RequestMapping("/")
public class HomeController {

    @RequestMapping("/home")
    public String home(HttpSession session, Model model) {
        TUser user = (TUser) session.getAttribute("curUser");
        model.addAttribute("curUser", user);
        return "home";
    }
}
