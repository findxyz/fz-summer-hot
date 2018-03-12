package xyz.fz.constant;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Created by Administrator on 2017/5/16.
 */
@Component
public class WxConstant {

    private static String appId;

    private static String appSecret;

    private static String url;

    private static String token;

    private static String appIndexUrl;

    public static String getAppId() {
        return appId;
    }

    @Value("${wx.appID}")
    public void setAppId(String appId) {
        WxConstant.appId = appId;
    }

    public static String getAppSecret() {
        return appSecret;
    }

    @Value("${wx.appsecret}")
    public void setAppSecret(String appSecret) {
        WxConstant.appSecret = appSecret;
    }

    public static String getUrl() {
        return url;
    }

    @Value("${wx.URL}")
    public void setUrl(String url) {
        WxConstant.url = url;
    }

    public static String getToken() {
        return token;
    }

    @Value("${wx.Token}")
    public void setToken(String token) {
        WxConstant.token = token;
    }

    public static String getAppIndexUrl() {
        return appIndexUrl;
    }

    @Value("${wx.app.index.url}")
    public void setAppIndexUrl(String appIndexUrl) {
        WxConstant.appIndexUrl = appIndexUrl;
    }
}
