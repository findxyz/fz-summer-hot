package xyz.fz.service.wx;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.util.Map;

/**
 * Created by Administrator on 2017/5/7.
 */
public interface WxService {

    String clickEvent(String publicName, String subscriberUserName, String eventKey) throws IOException, JAXBException;

    String scanCodeWaitMsg(String publicName, String subscriberUserName, String scanResult) throws IOException, JAXBException;

    Map fetchUserInfo(String appID, String appsecret, String code) throws Exception;

    String wxAccessToken() throws IOException;
}
