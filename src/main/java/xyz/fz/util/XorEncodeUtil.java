package xyz.fz.util;

import org.apache.commons.codec.binary.Base64;

import java.io.UnsupportedEncodingException;

public class XorEncodeUtil {

    private static final String key = "abc";

    private static final String CHARSET_ENCODE = "utf-8";

    private static byte[] xorEncode(byte[] data) {
        // 将任意长的key转变成长度为256的新key
        byte[] keyBytes = new byte[256];
        for (int i = 0; i < key.getBytes().length; i++) {
            keyBytes[i] = key.getBytes()[i % key.getBytes().length];
        }

        // 该byte[]为输出密文
        byte[] encodeBytes = new byte[data.length];

        // 在每一轮循环中对key下功夫，比如置换
        int j = 0;
        int k = 0;
        for (int i = 0; i < encodeBytes.length; i++) {
            k = 0xff & k + 1;
            j = 0xff & j + keyBytes[k];
            int m = keyBytes[k];
            keyBytes[k] = keyBytes[j];
            keyBytes[j] = (byte) m;
            int n = 0xff & keyBytes[j] + keyBytes[k];
            encodeBytes[i] = (byte) (data[i] ^ keyBytes[n]);
        }

        return encodeBytes;
    }

    public static String encode(String data) {
        try {
            return Base64.encodeBase64String(xorEncode(data.getBytes(CHARSET_ENCODE)));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return "";
        }
    }

    public static String decode(String encodeData) {
        try {
            return new String(xorEncode(Base64.decodeBase64(encodeData.getBytes(CHARSET_ENCODE))), CHARSET_ENCODE);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return "";
        }
    }

    public static void main(String[] args) {
        String a = "def";
        String encode = encode(a);
        System.out.println(encode);
        String decode = decode(encode);
        System.out.println(decode);
    }
}
