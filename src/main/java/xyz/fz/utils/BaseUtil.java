package xyz.fz.utils;

import org.apache.commons.codec.digest.DigestUtils;

import java.awt.*;
import java.util.Random;

/**
 * Created by fz on 2016/9/7.
 */
public class BaseUtil {

    public static String sha1Hex(String str) {
        return DigestUtils.sha1Hex(str);
    }

    public static Color getRandColor(int fc, int bc) {
        Random random = new Random();
        if (fc > 255) {
            fc = 255;
        }
        if (bc > 255) {
            bc = 255;
        }
        int r = fc + random.nextInt(bc - fc);
        int g = fc + random.nextInt(bc - fc);
        int b = fc + random.nextInt(bc - fc);
        return new Color(r, g, b);
    }
}
