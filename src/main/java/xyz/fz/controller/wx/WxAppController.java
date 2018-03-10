package xyz.fz.controller.wx;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import xyz.fz.constant.WxConstant;
import xyz.fz.service.wx.WxService;
import xyz.fz.util.BaseUtil;

import javax.servlet.http.HttpSession;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;

/**
 * Created by Administrator on 2017/5/5.
 */
@Controller
@RequestMapping("/wxApp")
@SuppressWarnings("unchecked")
public class WxAppController {

    private static final Logger logger = LoggerFactory.getLogger(WxAppController.class);

    private final WxService wxService;

    @Autowired
    public WxAppController(WxService wxService) {
        this.wxService = wxService;
    }

    @RequestMapping("/index")
    public String appIndex(@RequestParam(value = "code", required = false) String code,
                           @RequestParam(value = "state", required = false) String state,
                           HttpSession session,
                           Model model) throws UnsupportedEncodingException {

        // 0.如果用户已登陆则直接返回
        if (isAlreadyLogin(session, model)) {
            return "wx/appIndex";
        }
        // 1.没有任何参数的进入，说明需要进行权限验证，跳转到微信用户信息授权页
        String authUrl = neededWxAuth(code, state);
        if (authUrl != null) {
            return authUrl;
        }
        // 3.有code也有state，说明授权完毕，可以通过code换取用于拉取用户个人信息的access_token
        if (doWxAuth(code, state, session, model)) {
            return "wx/appIndex";
        }
        return "wx/appSorry";
    }

    private boolean doWxAuth(String code, String state, HttpSession session, Model model) {
        if (!StringUtils.isEmpty(state) && !StringUtils.isEmpty(code)) {
            // 3.1code获取access_token失败，说明code错误，不排除恶意行为，直接跳转sorry页
            // 3.2code获取access_token成功，使用access_token拉取用户个人信息，并存入数据库
            try {
                Map userInfoMap = wxService.fetchUserInfo(WxConstant.getAppID(), WxConstant.getAppsecret(), code);
                logger.debug("拉取到的用户信息：{}", userInfoMap.toString());
                session.setAttribute("openid", userInfoMap.get("openid"));
                session.setAttribute("nickname", userInfoMap.get("nickname"));
                session.setAttribute("headimgurl", userInfoMap.get("headimgurl"));
                model.addAttribute("openid", userInfoMap.get("openid"));
                model.addAttribute("nickname", userInfoMap.get("nickname"));
                model.addAttribute("headimgurl", userInfoMap.get("headimgurl"));
                return true;
            } catch (Exception e) {
                logger.error(BaseUtil.getExceptionStackTrace(e));
            }
        }
        return false;
    }

    private String neededWxAuth(String code, String state) throws UnsupportedEncodingException {
        if (StringUtils.isEmpty(state) && StringUtils.isEmpty(code)) {
            String encodeAppIndexUrl = URLEncoder.encode(WxConstant.getAppIndexUrl(), "utf-8");
            String authUrl = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=" + WxConstant.getAppID();
            authUrl += "&redirect_uri=" + encodeAppIndexUrl;
            authUrl += "&response_type=code";
            authUrl += "&scope=snsapi_userinfo";
            authUrl += "&state=STATE#wechat_redirect";
            return "redirect:" + authUrl;
        }
        return null;
    }

    private boolean isAlreadyLogin(HttpSession session, Model model) {
        if (session.getAttribute("openid") != null) {
            model.addAttribute("openid", session.getAttribute("openid"));
            model.addAttribute("nickname", session.getAttribute("nickname"));
            model.addAttribute("headimgurl", session.getAttribute("headimgurl"));
            return true;
        }
        return false;
    }
}
