package xyz.fz.controller;

import org.apache.commons.codec.binary.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import xyz.fz.domain.TUser;
import xyz.fz.service.UserService;
import xyz.fz.utils.BaseUtil;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * Created by fz on 2016/9/15.
 */
@Controller
public class IndexController {

    @Autowired
    private UserService userService;

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
        String sessionValCode = session.getAttribute("valCode").toString();
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
    public void valCode(HttpServletRequest request, HttpServletResponse response, HttpSession session) {

        response.setHeader("Pragma", "No-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);

        ServletOutputStream sos = null;
        try {
            int width = 60, height = 20;
            BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            // 获取图形上下文
            Graphics g = image.getGraphics();
            // 生成随机类
            Random random = new Random();
            // 设定背景色
            g.setColor(BaseUtil.getRandColor(200, 250));
            g.fillRect(0, 0, width, height);
            // 设定字体
            g.setFont(new Font("Times New Roman", Font.PLAIN, 18));
            // 随机产生155条干扰线，使图象中的认证码不易被其它程序探测到
            g.setColor(BaseUtil.getRandColor(160, 200));
            for (int i = 0; i < 155; i++) {
                int x = random.nextInt(width);
                int y = random.nextInt(height);
                int xl = random.nextInt(12);
                int yl = random.nextInt(12);
                g.drawLine(x, y, x + xl, y + yl);
            }
            // 取随机产生的认证码(4位数字)
            String sRand = "";
            for (int i = 0; i < 4; i++) {
                String rand = String.valueOf(random.nextInt(10));
                sRand += rand;
                // 将认证码显示到图象中
                // 调用函数出来的颜色相同，可能是因为种子太接近，所以只能直接生成
                g.setColor(new Color(20 + random.nextInt(110), 20 + random.nextInt(110), 20 + random.nextInt(110)));
                g.drawString(rand, 13 * i + 6, 16);
            }
            // 将认证码存入SESSION
            session.setAttribute("valCode", sRand);
            // 图象生效
            g.dispose();
            sos = response.getOutputStream();
            // 输出图象到页面
            ImageIO.write(image, "JPEG", sos);
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
