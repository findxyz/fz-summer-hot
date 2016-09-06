package xyz.fz.utils;

import org.apache.commons.codec.digest.DigestUtils;

/**
 * Created by fz on 2016/9/7.
 */
public class BaseUtil {

    public static String sha1Hex(String str) {
        return DigestUtils.sha1Hex(str);
    }

    public static void main(String[] args) {
        System.out.println(sha1Hex("88888888"));
    }
}
