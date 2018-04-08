package xyz.fz.controller.wx;

import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import xyz.fz.constant.WxConstant;
import xyz.fz.service.wx.WxService;
import xyz.fz.util.BaseUtil;

import javax.xml.bind.JAXBException;
import java.io.IOException;

/**
 * Created by Administrator on 2017/5/5.
 */
@Controller
@RequestMapping("/wx")
@SuppressWarnings("unchecked")
public class WxController {

    private static final Logger logger = LoggerFactory.getLogger(WxController.class);

    private final WxService wxService;

    @Autowired
    public WxController(WxService wxService) {
        this.wxService = wxService;
    }

    @RequestMapping(value = "/", method = RequestMethod.GET)
    @ResponseBody
    public String wxServerCheck(@RequestParam(value = "signature", required = false) String signature,
                                @RequestParam(value = "timestamp", required = false) String timestamp,
                                @RequestParam(value = "nonce", required = false) String nonce,
                                @RequestParam(value = "echostr", required = false) String echostr) {

        String[] sortedArr = StringUtils.sortStringArray(new String[]{WxConstant.getToken(), timestamp, nonce});
        String calcSignature = DigestUtils.sha1Hex(String.join("", sortedArr));
        if (signature.equals(calcSignature)) {
            return echostr;
        } else {
            return "";
        }
    }

    @RequestMapping(value = "/", method = RequestMethod.POST)
    @ResponseBody
    public String wxMessage(@RequestBody String xml) throws JAXBException, IOException {

        xyz.fz.model.wxMessage.Xml xmlObj = BaseUtil.jaxbUnmarshal(xml, xyz.fz.model.wxMessage.Xml.class);
        String toUserName = xmlObj.getToUserName();
        String fromUserName = xmlObj.getFromUserName();
        String msgType = xmlObj.getMsgType();
        String event = xmlObj.getEvent();
        String eventKey = xmlObj.getEventKey();
        switch (msgType) {
            case "event":
                switch (event) {
                    case "CLICK":
                        return wxService.clickEvent(toUserName, fromUserName, eventKey);
                    case "scancode_waitmsg":
                        String scanResult = xmlObj.getScanCodeInfo().getScanResult();
                        return wxService.scanCodeWaitMsg(toUserName, fromUserName, scanResult);
                    default:
                        return "";
                }
            default:
                return "";
        }
    }
}
