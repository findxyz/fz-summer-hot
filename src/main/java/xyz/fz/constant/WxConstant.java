package xyz.fz.constant;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Created by Administrator on 2017/5/16.
 */
@Component
public class WxConstant {

    private static String appID;

    private static String appsecret;

    private static String URL;

    private static String Token;

    private static String appIndexUrl;

    public static String getAppID() {
        return appID;
    }

    @Value("${wx.appID}")
    public void setAppID(String appID) {
        WxConstant.appID = appID;
    }

    public static String getAppsecret() {
        return appsecret;
    }

    @Value("${wx.appsecret}")
    public void setAppsecret(String appsecret) {
        WxConstant.appsecret = appsecret;
    }

    public static String getURL() {
        return URL;
    }

    @Value("${wx.URL}")
    public void setURL(String URL) {
        WxConstant.URL = URL;
    }

    public static String getToken() {
        return Token;
    }

    @Value("${wx.Token}")
    public void setToken(String token) {
        Token = token;
    }

    public static String getAppIndexUrl() {
        return appIndexUrl;
    }

    @Value("${wx.app.index.url}")
    public void setAppIndexUrl(String appIndexUrl) {
        WxConstant.appIndexUrl = appIndexUrl;
    }
}
