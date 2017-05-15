package xyz.fz.controller.wx;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import xyz.fz.service.wx.WxService;
import xyz.fz.util.HttpUtil;

import java.io.IOException;
import java.util.LinkedHashMap;

/**
 * Created by Administrator on 2017/5/5.
 */
@Controller
@RequestMapping("/wxCtl")
@SuppressWarnings("unchecked")
public class WxCtlController {

    private static final Logger logger = LoggerFactory.getLogger(WxCtlController.class);

    @Autowired
    private WxService wxService;

    @RequestMapping("/main")
    public String main() {

        return "wx/ctlMain";
    }

    @RequestMapping("/refreshAccessToken")
    @ResponseBody
    public String refreshAccessToken() throws IOException {

        return wxService.wxAccessToken();
    }

    /*
    {
        "button": [{
            "type": "view",
            "name": "盗梦空间",
            "url": "http://pleaseeee.51vip.biz/wxApp/index"
        },
        {
            "name": "功能",
            "sub_button": [{
                "type": "click",
                "name": "点击",
                "key": "Click"
            },
            {
                "type": "scancode_waitmsg",
                "name": "扫码绑定",
                "key": "Scan"
            }]
        }]
    }
    */

    @RequestMapping("/createMenu")
    @ResponseBody
    public String createMenu(@RequestParam("json") String json) throws IOException {

        String url = "https://api.weixin.qq.com/cgi-bin/menu/create?access_token=" + wxService.wxAccessToken();
        return HttpUtil.httpPostJson(url, json);
    }

    @RequestMapping("/delMenu")
    @ResponseBody
    public String delMenu() throws IOException {

        String url = "https://api.weixin.qq.com/cgi-bin/menu/delete";
        LinkedHashMap linkedHashMap = new LinkedHashMap();
        linkedHashMap.put("access_token", wxService.wxAccessToken());
        return HttpUtil.httpGet(url, linkedHashMap);
    }

}
