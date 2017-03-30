package xyz.fz.utils;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.AlgorithmParameterSpec;

/**
 * Created by Administrator on 2017/3/30.
 */
public class AES256CBCUtil {

    private static String DEFAULT_TEST_256KEY = "xxooxxooooxxooxxxxooxxooooxxooxx";

    private static String AES256CBC_ALGORITHM = "AES/CBC/PKCS5Padding";

    private static byte[] ivBytes = {0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00};

    public static String encrypt(String content, String key) throws UnsupportedEncodingException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
        byte[] contentBytes = content.getBytes("UTF-8");
        AlgorithmParameterSpec ivSpec = new IvParameterSpec(ivBytes);
        SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes("UTF-8"), "AES");
        Cipher cipher = Cipher.getInstance(AES256CBC_ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivSpec);
        return Base64.encodeBase64String(cipher.doFinal(contentBytes));
    }

    public static String decrypt(String content, String key) throws UnsupportedEncodingException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
        byte[] contentBytes = Base64.decodeBase64(content);
        AlgorithmParameterSpec ivSpec = new IvParameterSpec(ivBytes);
        SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes("UTF-8"), "AES");
        Cipher cipher = Cipher.getInstance(AES256CBC_ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, ivSpec);
        return new String(cipher.doFinal(contentBytes), "UTF-8");
    }

    public static String hmacSHA512(String data, String key) throws NoSuchAlgorithmException, InvalidKeyException, UnsupportedEncodingException {
        byte[] bytesKey = key.getBytes();
        final SecretKeySpec secretKey = new SecretKeySpec(bytesKey, "HmacSHA512");
        Mac mac = Mac.getInstance("HmacSHA512");
        mac.init(secretKey);
        final byte[] macData = mac.doFinal(data.getBytes());
        byte[] hex = new Hex().encode(macData);
        return new String(hex, "UTF-8");
    }

    public static void main(String[] args) throws NoSuchPaddingException, InvalidAlgorithmParameterException, UnsupportedEncodingException, IllegalBlockSizeException, BadPaddingException, NoSuchAlgorithmException, InvalidKeyException {
        String key = "SvEpI11JO2QpqEPTCxA7JTspRa56OBDq";
        String content = "<?xml version=\"1.0\" encoding=\"utf-8\"?><orderRequest><orderId>1234567890</orderId></orderRequest>";
        String encryptContent = AES256CBCUtil.encrypt(content, key);
        System.out.println(encryptContent);
        System.out.println(AES256CBCUtil.decrypt(encryptContent, key));
        String hmacKey = "VJ4fpM0q30/LwkbykbYa7HnBYTv9qB+t";
        System.out.println(AES256CBCUtil.hmacSHA512(encryptContent, hmacKey));
    }
}
