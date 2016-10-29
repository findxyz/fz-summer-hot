package xyz.fz.controller;

import com.google.code.kaptcha.impl.DefaultKaptcha;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import xyz.fz.domain.user.TUser;
import xyz.fz.service.user.UserService;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by fz on 2016/9/15.
 */
@Controller
public class IndexController {

    @Autowired
    private UserService userService;

    @Autowired
    private DefaultKaptcha googleKaptcha;

    @RequestMapping("/")
    public String root() {
        return "redirect:index";
    }

    @RequestMapping("/index")
    public String index() {
        return "index";
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login() {
        return "login";
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> login(HttpSession session,
                                     @RequestParam("userName") String userName,
                                     @RequestParam("passWord") String passWord,
                                     @RequestParam("valCode") String valCode) {

        Map<String, Object> result = new HashMap<>();
        String sessionValCode = session.getAttribute("valCode") != null ? session.getAttribute("valCode").toString() : session.toString();
        if (StringUtils.equals(sessionValCode, valCode)) {
            TUser user = userService.findUser(userName, passWord);
            if (user != null) {
                session.setAttribute("curUser", user);
                result.put("success", true);
            } else {
                result.put("success", false);
                result.put("message", "用户名或密码错误");
            }
        } else {
            result.put("success", false);
            result.put("message", "验证码错误");
        }

        return result;
    }

    @RequestMapping("/index/valCode")
    public void valCode(HttpServletResponse response, HttpSession session) {

        response.setHeader("Pragma", "No-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);

        ServletOutputStream sos = null;
        try {
            // 生成验证码
            String capText = googleKaptcha.createText();
            session.setAttribute("valCode", capText);
            BufferedImage capImage = googleKaptcha.createImage(capText);
            // 输出验证码到页面
            sos = response.getOutputStream();
            ImageIO.write(capImage, "JPEG", sos);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (sos != null) {
                    sos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @RequestMapping("/logout")
    @ResponseBody
    public Map<String, Object> logout(HttpSession session) {
        Map<String, Object> result = new HashMap<>();
        try {
            session.removeAttribute("curUser");
            result.put("success", true);
        } catch (Exception e) {
            result.put("success", false);
        }
        return result;
    }

}
