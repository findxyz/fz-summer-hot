package xyz.fz.util;

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
public class AesCbcUtil {

    // 128位密钥长度需要16个字符
    // 256位密钥长度需要32个字符，注：256位密钥需要自行安装加密许可 http://blog.csdn.net/wangjunjun2008/article/details/50847426
    private static final String DEFAULT_TEST_KEY = "0000111122223333";

    private static final String DEFAULT_ENCODING = "UTF-8";

    private static final String KEY_ALGORITHM = "AES";

    private static final String CIPHER_ALGORITHM = "AES/CBC/PKCS5Padding";

    private static final String SIGNATURE_ALGORITHM = "HmacSHA512";

    // 最好的使用方式是每次随机然后将iv向量也随请求传递
    private static final byte[] DEFAULT_IV_BYTES = {0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00};

    public static String encrypt(String content, String key) throws UnsupportedEncodingException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
        byte[] contentBytes = content.getBytes(DEFAULT_ENCODING);
        AlgorithmParameterSpec ivSpec = new IvParameterSpec(DEFAULT_IV_BYTES);
        SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes(DEFAULT_ENCODING), KEY_ALGORITHM);
        Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivSpec);
        return Base64.encodeBase64String(cipher.doFinal(contentBytes));
    }

    public static String decrypt(String content, String key) throws UnsupportedEncodingException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
        byte[] contentBytes = Base64.decodeBase64(content);
        AlgorithmParameterSpec ivSpec = new IvParameterSpec(DEFAULT_IV_BYTES);
        SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes(DEFAULT_ENCODING), KEY_ALGORITHM);
        Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, ivSpec);
        return new String(cipher.doFinal(contentBytes), DEFAULT_ENCODING);
    }

    /* 用于对数据签名，密钥可随意指定 */
    public static String hmacSHA512(String data, String key) throws NoSuchAlgorithmException, InvalidKeyException, UnsupportedEncodingException {
        byte[] bytesKey = key.getBytes();
        final SecretKeySpec secretKey = new SecretKeySpec(bytesKey, SIGNATURE_ALGORITHM);
        Mac mac = Mac.getInstance(SIGNATURE_ALGORITHM);
        mac.init(secretKey);
        final byte[] macData = mac.doFinal(data.getBytes());
        byte[] hex = new Hex().encode(macData);
        return new String(hex, DEFAULT_ENCODING);
    }

    public static void main(String[] args) throws NoSuchPaddingException, InvalidAlgorithmParameterException, UnsupportedEncodingException, IllegalBlockSizeException, BadPaddingException, NoSuchAlgorithmException, InvalidKeyException {
        String content = "<?xml version=\"1.0\" encoding=\"utf-8\"?><orderRequest><orderId>1234567890</orderId></orderRequest>";
        System.out.println(DEFAULT_TEST_KEY.getBytes(DEFAULT_ENCODING).length);
        String encryptContent = AesCbcUtil.encrypt(content, DEFAULT_TEST_KEY);
        System.out.println(encryptContent);
        System.out.println(AesCbcUtil.decrypt(encryptContent, DEFAULT_TEST_KEY));
        String hmacKey = "任意字符串";
        System.out.println(AesCbcUtil.hmacSHA512(encryptContent, hmacKey));
    }
}
