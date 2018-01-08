package xyz.fz.util;

import org.apache.commons.codec.binary.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

/**
 * Created by fz on 2016/7/4.
 */
public class RSAUtil {

    private static final String KEY_PAIR_ALGORITHM = "RSA";

    private static final String KEY_ALGORITHM = "RSA";

    private static final String CIPHER_ALGORITHM = "RSA/ECB/PKCS1Padding";

    private static final String SIGNATURE_ALGORITHM = "MD5withRSA";

    private static final int KEY_BITS_1024 = 1024;

    private static final int KEY_BITS_2048 = 2048;

    // 密钥位数1024或2048
    private static final int KEY_SIZE = KEY_BITS_1024;

    private static final int MAX_DECRYPT_BLOCK = KEY_SIZE / 8;

    private static final int MAX_ENCRYPT_BLOCK = MAX_DECRYPT_BLOCK - 11;

    private static final int ENCRYPT_MODE = Cipher.ENCRYPT_MODE;

    private static final int DECRYPT_MODE = Cipher.DECRYPT_MODE;

    public static class RetKeyPair {

        private String base64PubKey;

        private String base64PriKey;

        RetKeyPair(String base64PubKey, String base64PriKey) {
            this.base64PubKey = base64PubKey;
            this.base64PriKey = base64PriKey;
        }

        public String getBase64PubKey() {
            return base64PubKey;
        }

        public String getBase64PriKey() {
            return base64PriKey;
        }
    }

    // 生成公私匙
    public static RetKeyPair generateKeyPair() throws Exception {

        KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance(KEY_PAIR_ALGORITHM);
        keyPairGen.initialize(KEY_SIZE);
        KeyPair keyPair = keyPairGen.generateKeyPair();
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
        return new RetKeyPair(Base64.encodeBase64String(publicKey.getEncoded()), Base64.encodeBase64String(privateKey.getEncoded()));
    }

    // 私匙签名
    public static String sign(byte[] data, String privateKey) throws Exception {

        byte[] keyBytes = Base64.decodeBase64(privateKey);
        PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        PrivateKey privateK = keyFactory.generatePrivate(pkcs8EncodedKeySpec);
        Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
        signature.initSign(privateK);
        signature.update(data);
        return Base64.encodeBase64String(signature.sign());
    }

    // 公匙校验签名
    public static boolean verify(byte[] data, String publicKey, String sign) throws Exception {

        byte[] keyBytes = Base64.decodeBase64(publicKey);
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        PublicKey publicK = keyFactory.generatePublic(keySpec);
        Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
        signature.initVerify(publicK);
        signature.update(data);
        return signature.verify(Base64.decodeBase64(sign));
    }

    // 获取公匙加解密法
    private static Cipher getPubCipher(String publicKey, int mode) throws Exception {

        byte[] keyBytes = Base64.decodeBase64(publicKey);
        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        Key publicK = keyFactory.generatePublic(x509KeySpec);
        Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
        cipher.init(mode, publicK);
        return cipher;
    }

    // 获取私匙加解密法
    private static Cipher getPriCipher(String privateKey, int mode) throws Exception {

        byte[] keyBytes = Base64.decodeBase64(privateKey);
        PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        Key privateK = keyFactory.generatePrivate(pkcs8EncodedKeySpec);
        Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
        cipher.init(mode, privateK);
        return cipher;
    }

    private static byte[] encryptData(byte[] originData, Cipher cipher) throws Exception {

        return cipherData(originData, cipher, MAX_ENCRYPT_BLOCK);
    }

    private static byte[] decryptData(byte[] originData, Cipher cipher) throws BadPaddingException, IllegalBlockSizeException, IOException {

        return cipherData(originData, cipher, MAX_DECRYPT_BLOCK);
    }

    private static byte[] cipherData(byte[] originData, Cipher cipher, int maxSize) throws BadPaddingException, IllegalBlockSizeException, IOException {

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        // 对数据分段加解密
        int i = 0;
        int offSet = 0;
        int inputLen = originData.length;
        byte[] cache;
        while (inputLen - offSet > 0) {
            if (inputLen - offSet > maxSize) {
                cache = cipher.doFinal(originData, offSet, maxSize);
            } else {
                cache = cipher.doFinal(originData, offSet, inputLen - offSet);
            }
            out.write(cache, 0, cache.length);
            i++;
            offSet = i * maxSize;
        }
        byte[] cipherData = out.toByteArray();
        out.close();
        return cipherData;
    }

    // 公匙加密
    public static byte[] encryptByPublicKey(byte[] data, String publicKey) throws Exception {

        return encryptData(data, getPubCipher(publicKey, ENCRYPT_MODE));
    }

    // 公匙解密数据
    public static byte[] decryptByPublicKey(byte[] data, String publicKey) throws Exception {

        return decryptData(data, getPubCipher(publicKey, DECRYPT_MODE));
    }

    // 私匙加密
    public static byte[] encryptByPrivateKey(byte[] data, String privateKey) throws Exception {

        return encryptData(data, getPriCipher(privateKey, ENCRYPT_MODE));
    }

    // 私匙解密数据
    public static byte[] decryptByPrivateKey(byte[] data, String privateKey) throws Exception {

        return decryptData(data, getPriCipher(privateKey, DECRYPT_MODE));
    }

    public static void main(String[] args) throws Exception {

        // RetKeyPair keyPair = generateKeyPair();
        String priKey = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAJG5vzs1cndiJQnAXh8kZAQ2/24dIrHX1/QS5+PqS+76Z+zVHtasMT/uZO2iU9Jr7boaP6lVfoSO1IKQn18XDOx85lpVlFgWT4JPYHlwXXGJPUi+sPp9SRr2ZKX809BIUdgKF3JjQ0M/tn1rUA/JvDSnOm/jZ8yDdm7fs1vdNNa/AgMBAAECgYAmtuVCEqH1a4KRg6S9u0pDAGV92Im2G7TIqBkpKL7Bn47qkXge6iIvbOMtarnAJmJC1eRh1U/GYbe9dDSS66nH44qVBIfA+u7kHE6j1geUzLQc9XsIcs5Erwjy9MTcp421xJcxrRSHtsiyteF1StNmWSrgO4osZgvzPLRAGLU8SQJBAP8c7g9MHkJ9SAf1ZjWju9DwiL3MZ08I6cpsoSeFG7kDDMFMRgPEW+cKL9S6BHBpVKsJix26DytAmDaXy8p0pQsCQQCSO3Qyr5j9+kOkYGI4fACdVcwrTCV3dn/Bto9Iuz08a251Kz24DQtDFR2w34s4QqtWux0rCZ8riVHovJFACD2dAkEAlc8SSO9lEZxqMSo9NCCLST4GvpYK0JGmYJV76S40Qmf1FInKz6l+YmVMAzqdIrnn22yGQhWhlL+g4uYu6RuoUwJAJDPl65p/lPk9f9eA2Z0RheI03s9GQ1IqBOpOhOIeIfQy730aFZPdIul6ZUyiqfYPdzRx3zLNnjir96Ofjiu8HQJBAPA9TIRvdJKB5IHFG7Sm6Do3zGu5fMhvwI6t6U7DPa4bX5RHCikEWNd5xd39r5HLAP/eIl/rFJAF3EHdpqA3ZOI=";
        String pubKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCRub87NXJ3YiUJwF4fJGQENv9uHSKx19f0Eufj6kvu+mfs1R7WrDE/7mTtolPSa+26Gj+pVX6EjtSCkJ9fFwzsfOZaVZRYFk+CT2B5cF1xiT1IvrD6fUka9mSl/NPQSFHYChdyY0NDP7Z9a1APybw0pzpv42fMg3Zu37Nb3TTWvwIDAQAB";

        System.out.println("\n========================================\n");

        System.out.println("私钥：" + priKey);
        System.out.println("公钥：" + pubKey);

        String content = "xml";
        System.out.println("原始内容：" + content);

        System.out.println("\n========================================\n");

        long begin2 = System.currentTimeMillis();
        String encBase64Content = Base64.encodeBase64String(encryptByPrivateKey(content.getBytes("utf-8"), priKey));
        System.out.println("私钥加密后的内容：" + encBase64Content);
        long end2 = System.currentTimeMillis();
        System.out.println("私钥加密用时：" + (end2 - begin2));

        long begin3 = System.currentTimeMillis();
        String decBase64Content = new String(decryptByPublicKey(Base64.decodeBase64(encBase64Content), pubKey), "utf-8");
        System.out.println("公钥解密后的内容：" + decBase64Content);
        long end3 = System.currentTimeMillis();
        System.out.println("公钥解密用时：" + (end3 - begin3));

        System.out.println("\n========================================\n");

        long begin4 = System.currentTimeMillis();
        String encBase64Content2 = Base64.encodeBase64String(encryptByPublicKey(content.getBytes("utf-8"), pubKey));
        System.out.println("公钥加密后的内容：" + encBase64Content2);
        long end4 = System.currentTimeMillis();
        System.out.println("公钥加密用时：" + (end4 - begin4));

        long begin5 = System.currentTimeMillis();
        String decBase64Content2 = new String(decryptByPrivateKey(Base64.decodeBase64(encBase64Content2), priKey), "utf-8");
        System.out.println("私钥解密后的内容：" + decBase64Content2);
        long end5 = System.currentTimeMillis();
        System.out.println("私钥解密用时：" + (end5 - begin5));
    }

}
