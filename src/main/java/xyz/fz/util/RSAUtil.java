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
import java.util.Hashtable;
import java.util.Map;

/**
 * Created by fz on 2016/7/4.
 */
public class RSAUtil {

    private static final String KEY_ALGORITHM = "RSA";

    private static final String SIGNATURE_ALGORITHM = "MD5withRSA";

    private static final int KEY_SIZE = 1024;

    private static final int MAX_ENCRYPT_BLOCK = 117;

    private static final int MAX_DECRYPT_BLOCK = 128;

    private static final int ENCRYPT_MODE = Cipher.ENCRYPT_MODE;

    private static final int DECRYPT_MODE = Cipher.DECRYPT_MODE;

    private static final Map<String, Cipher> cipherCache = new Hashtable<>();

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

        KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance(KEY_ALGORITHM);
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

        if (cipherCache.containsKey(publicKey + "_" + mode)) {
            return cipherCache.get(publicKey + "_" + mode);
        } else {
            byte[] keyBytes = Base64.decodeBase64(publicKey);
            X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(keyBytes);
            KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
            Key publicK = keyFactory.generatePublic(x509KeySpec);
            Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
            cipher.init(mode, publicK);
            cipherCache.put(publicKey + "_" + mode, cipher);
            return cipher;
        }
    }

    // 获取私匙加解密法
    private static Cipher getPriCipher(String privateKey, int mode) throws Exception {

        if (cipherCache.containsKey(privateKey + "_" + mode)) {
            return cipherCache.get(privateKey + "_" + mode);
        } else {
            byte[] keyBytes = Base64.decodeBase64(privateKey);
            PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(keyBytes);
            KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
            Key privateK = keyFactory.generatePrivate(pkcs8EncodedKeySpec);
            Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
            cipher.init(mode, privateK);
            cipherCache.put(privateKey + "_" + mode, cipher);
            return cipher;
        }
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

        long begin = System.currentTimeMillis();
        // RetKeyPair keyPair = generateKeyPair();
        String priKey = "MIICeAIBADANBgkqhkiG9w0BAQEFAASCAmIwggJeAgEAAoGBAKEFSIyJN1BofFcRz6yRtOR519UCxzsPRAN4bFJ6QhAN/YaW2O1ZFvoptPWLYEFO/Z22sq+Y5PIjbXR5Ut3ud/lwMp/DxTi6DTDNzJQbeGOugQqVQYxpX/ZQ9SsUITcXpsI3Z5NW1odpyEUuMuufM0VuKQlYibLUc6zGhlk67/yrAgMBAAECgYEAiZTXGWWBAs5UN507YgsZkgLdvN7j1n3DsmdpvstBuTALCL3JWnu920BZo1hUhVj18JUTdmBgdth4hIXJnWFN1lPb10JbKGWCnhbgRtoPI8QJpgyv+z6JRaWbF4nNjf+Z2pX17IWgVidWGmI8RSNaYm/zzlAQC5PuJtDyhDTV9QkCQQDOXdH4wTdBECxHGn55K0sF/pWu/4+4w6HSi10l2zIl9P4f4EGHttRojhyfmZkxisjNpmPB546j+IB+ZgcyupWnAkEAx795+smomvlfDocMSuLf0Vw0ff78JLu+6e0xk09899bjGjgO6fEg7EwAwSZFAMkytxfiIbYP0W/yM+d66Q1JXQJAE9gVvtPlmk4R0+yKSOCO4E6w2hkdGulFAFgCZweC9P9wCGSqKIC+QCeaPQaqIodz88KSSS0ZHE9jG8R34LrsdQJBAKOuvH5OEeQsxUBaWjBjiZU+QaWd9XEEiIWY4S8dzAIsDR3HIjGCbsAz58pfWdwzA2QWJjAJyRyO56Kw5X9ka+ECQQCRqn1t9ZoaXu/QciMnwzgqM1F74BiTybCnepIDCd2kzfy7ZMll1KoBIMtrq/dYFQFf7PaFnkIq3OAoHC4OAAM+";
        System.out.println(Base64.decodeBase64(priKey).length);
        getPriCipher(priKey, ENCRYPT_MODE);
        getPriCipher(priKey, DECRYPT_MODE);
        String pubKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQChBUiMiTdQaHxXEc+skbTkedfVAsc7D0QDeGxSekIQDf2GltjtWRb6KbT1i2BBTv2dtrKvmOTyI210eVLd7nf5cDKfw8U4ug0wzcyUG3hjroEKlUGMaV/2UPUrFCE3F6bCN2eTVtaHachFLjLrnzNFbikJWImy1HOsxoZZOu/8qwIDAQAB";
        getPubCipher(pubKey, ENCRYPT_MODE);
        getPubCipher(pubKey, DECRYPT_MODE);
        System.out.println(Base64.decodeBase64(pubKey).length);
        long end = System.currentTimeMillis();
        System.out.println(end - begin);

        System.out.println(priKey);
        System.out.println(pubKey);

        String content = "xml";
        long begin2 = System.currentTimeMillis();
        String encBase64Content = Base64.encodeBase64String(encryptByPrivateKey(content.getBytes("utf-8"), priKey));
        long end2 = System.currentTimeMillis();
        System.out.println("私钥加密" + (end2 - begin2));

        long begin3 = System.currentTimeMillis();
        System.out.println(new String(decryptByPublicKey(Base64.decodeBase64(encBase64Content), pubKey), "utf-8"));
        long end3 = System.currentTimeMillis();
        System.out.println("公钥解密" + (end3 - begin3));

        long begin4 = System.currentTimeMillis();
        String encBase64Content2 = Base64.encodeBase64String(encryptByPublicKey(content.getBytes("utf-8"), pubKey));
        long end4 = System.currentTimeMillis();
        System.out.println("公钥加密" + (end4 - begin4));

        long begin5 = System.currentTimeMillis();
        System.out.println(new String(decryptByPrivateKey(Base64.decodeBase64(encBase64Content2), priKey), "utf-8"));
        long end5 = System.currentTimeMillis();
        System.out.println("私钥解密" + (end5 - begin5));
    }

}
