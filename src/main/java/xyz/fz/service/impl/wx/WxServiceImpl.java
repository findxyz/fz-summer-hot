package xyz.fz.service.impl.wx;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.fz.constant.WxConstant;
import xyz.fz.dao.wx.WxUserDao;
import xyz.fz.domain.wx.TWxUser;
import xyz.fz.service.wx.WxService;
import xyz.fz.util.BaseUtil;
import xyz.fz.util.HttpUtil;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by Administrator on 2017/5/7.
 */
@Service
@Transactional
public class WxServiceImpl implements WxService {

    public static final String WX_ACCESS_TOKEN = "WX_ACCESS_TOKEN";

    private final WxUserDao wxUserDao;

    @Autowired
    public WxServiceImpl(WxUserDao wxUserDao) {
        this.wxUserDao = wxUserDao;
    }

    @Override
    public String clickEvent(String publicName, String subscriberUserName, String eventKey) throws IOException, JAXBException {
        xyz.fz.model.wxResponse.ObjectFactory objectFactory = new xyz.fz.model.wxResponse.ObjectFactory();
        xyz.fz.model.wxResponse.Xml xml = objectFactory.createXml();
        xml.setToUserName(subscriberUserName);
        xml.setFromUserName(publicName);
        xml.setCreateTime((new Date().getTime() / 1000) + "");
        xml.setMsgType("text");
        xml.setContent("Hello Developer! The key is: " + eventKey);
        return BaseUtil.JAXBMarshal(xml);
    }

    @Override
    public String scanCodeWaitMsg(String publicName, String subscriberUserName, String scanResult) throws IOException, JAXBException {
        xyz.fz.model.wxResponse.ObjectFactory objectFactory = new xyz.fz.model.wxResponse.ObjectFactory();
        xyz.fz.model.wxResponse.Xml xml = objectFactory.createXml();
        xml.setToUserName(subscriberUserName);
        xml.setFromUserName(publicName);
        xml.setCreateTime((new Date().getTime() / 1000) + "");
        xml.setMsgType("text");
        xml.setContent("Scan Result: " + scanResult);
        return BaseUtil.JAXBMarshal(xml);
    }

    @Override
    public Map fetchUserInfo(String appID, String appsecret, String code) throws Exception {
        String userAccessTokenUrl = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=" + appID;
        userAccessTokenUrl += "&secret=" + appsecret;
        userAccessTokenUrl += "&code=" + code;
        userAccessTokenUrl += "&grant_type=authorization_code";
        String userAccessTokenJson = HttpUtil.httpGet(userAccessTokenUrl, null);
        Map userAccessTokenMap = BaseUtil.parseJson(userAccessTokenJson, Map.class);
        if (userAccessTokenMap.get("openid") == null) {
            throw new RuntimeException("code换取用户access_token失败");
        }
        String openid = userAccessTokenMap.get("openid").toString();
        String userAccessToken = userAccessTokenMap.get("access_token").toString();
        String userInfoUrl = "https://api.weixin.qq.com/sns/userinfo?access_token=" + userAccessToken;
        userInfoUrl += "&openid=" + openid;
        userInfoUrl += "&lang=zh_CN";
        String userInfoJson = HttpUtil.httpGet(userInfoUrl, null);
        Map userInfoMap = BaseUtil.parseJson(userInfoJson, Map.class);
        // done 检查是否有该微信用户信息，如果有则更新用户信息，没有则进行新增
        TWxUser wxUser = wxUserDao.findByOpenid(userInfoMap.get("openid").toString());
        if (wxUser == null) {
            wxUser = new TWxUser();
            wxUser.setOpenid(userInfoMap.get("openid").toString());
            wxUser.setNickname(userInfoMap.get("nickname").toString());
            wxUser.setHeadImgUrl(userInfoMap.get("headimgurl").toString());
            wxUser.setIsSubscribe(1);
            wxUser.setCreateTime(new Date());
        } else {
            wxUser.setNickname(userInfoMap.get("nickname").toString());
            wxUser.setHeadImgUrl(userInfoMap.get("headimgurl").toString());
        }
        wxUserDao.save(wxUser);
        return userInfoMap;
    }

    @Override
    @Cacheable(value = "wxAccessToken", cacheManager = "wxAccessTokenRedisCacheManager", key = "#root.target.WX_ACCESS_TOKEN")
    @SuppressWarnings("unchecked")
    public String wxAccessToken() throws IOException {

        String url = "https://api.weixin.qq.com/cgi-bin/token";
        LinkedHashMap linkedHashMap = new LinkedHashMap();
        linkedHashMap.put("grant_type", "client_credential");
        linkedHashMap.put("appid", WxConstant.getAppID());
        linkedHashMap.put("secret", WxConstant.getAppsecret());
        String json = HttpUtil.httpGet(url, linkedHashMap);
        Map data = BaseUtil.parseJson(json, Map.class);
        return data.get("access_token").toString();
    }
}
