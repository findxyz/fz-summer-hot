package xyz.fz.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by fz on 2016/9/2.
 */
@Controller
@RequestMapping("/")
public class HomeController {

    @RequestMapping("/home")
    public String home() {
        return "home";
    }
}
