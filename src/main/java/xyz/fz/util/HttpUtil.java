package xyz.fz.util;

import okhttp3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.*;
import java.io.File;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Created by fz on 2015/8/30.
 */
public class HttpUtil {

    static {
        System.setProperty("jsse.enableSNIExtension", "false");
    }

    private static final Logger logger = LoggerFactory.getLogger(HttpUtil.class);

    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    public static final MediaType XML = MediaType.parse("application/xml; charset=utf-8");

    private static final TrustManager[] trustAllCerts = new TrustManager[]{
            new X509TrustManager() {
                @Override
                public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) {
                }

                @Override
                public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) {
                }

                @Override
                public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                    return new java.security.cert.X509Certificate[]{};
                }
            }
    };

    private static final SSLContext trustAllSslContext;

    static {
        try {
            trustAllSslContext = SSLContext.getInstance("SSL");
            trustAllSslContext.init(null, trustAllCerts, new java.security.SecureRandom());
        } catch (NoSuchAlgorithmException | KeyManagementException e) {
            throw new RuntimeException(e);
        }
    }

    private static final SSLSocketFactory trustAllSslSocketFactory = trustAllSslContext.getSocketFactory();

    private static OkHttpClient client = new OkHttpClient
            .Builder()
            .connectTimeout(10, TimeUnit.SECONDS)
            .readTimeout(10, TimeUnit.SECONDS)
            .writeTimeout(10, TimeUnit.SECONDS)
            .sslSocketFactory(trustAllSslSocketFactory, (X509TrustManager) trustAllCerts[0])
            .hostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            })
            .build();

    public static String httpPostJson(String url, String json) throws IOException {

        return httpPostContent(url, JSON, json);
    }

    public static String httpPostXml(String url, String xml) throws IOException {

        return httpPostContent(url, XML, xml);
    }

    private static String httpPostContent(String url, MediaType mediaType, String content) throws IOException {
        RequestBody requestBody = RequestBody.create(mediaType, content);
        Request request = new Request
                .Builder()
                .url(url)
                .post(requestBody)
                .build();
        logger.debug("Http PostContent Url: {}", request.url().toString());
        Response response = client.newCall(request).execute();
        return result(response);
    }

    public static String httpPost(String url, Map formParams) throws IOException {

        FormBody.Builder formBodyBuilder = new FormBody.Builder();
        if (formParams != null) {
            for (Object o : formParams.entrySet()) {
                Map.Entry entry = (Map.Entry) o;
                formBodyBuilder.add(entry.getKey().toString(), entry.getValue().toString());
            }
        }
        RequestBody formBody = formBodyBuilder.build();
        Request request = new Request.Builder()
                .url(url)
                .post(formBody)
                .build();
        logger.debug("Http Post Url: {}", request.url().toString());
        Response response = client.newCall(request).execute();
        return result(response);
    }

    public static String httpGet(String url, LinkedHashMap queryParams) throws IOException {

        StringBuilder urlBuilder = new StringBuilder(url);
        if (queryParams != null) {
            urlBuilder.append("?");
            for (Object o : queryParams.entrySet()) {
                Map.Entry entry = (Map.Entry) o;
                urlBuilder.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
            }
            url = urlBuilder.toString();
            url = url.substring(0, url.length() - 1);
        }
        Request request = new Request
                .Builder()
                .url(url)
                .build();
        logger.debug("Http Get Url: {}", request.url().toString());
        Response response = client.newCall(request).execute();
        return result(response);
    }

    public static String httpUpload(String url, Map<String, Object> params) throws IOException {

        MultipartBody.Builder formBodyBuilder = new MultipartBody.Builder().setType(MultipartBody.FORM);
        for (Map.Entry entry : params.entrySet()) {
            String key = entry.getKey().toString();
            Object value = entry.getValue();
            if (value instanceof File) {
                File file = (File) value;
                formBodyBuilder.addFormDataPart("file", file.getName(), RequestBody.create(MediaType.parse("image/jpg"), file));
            } else {
                formBodyBuilder.addFormDataPart(key, value.toString());
            }
        }
        RequestBody formBody = formBodyBuilder.build();
        Request request = new Request
                .Builder()
                .url(url)
                .post(formBody)
                .build();
        logger.debug("Http Upload Url: {}", request.url().toString());
        Response response = client.newCall(request).execute();
        return result(response);
    }

    private static String result(Response response) {
        if (response.isSuccessful()) {
            ResponseBody responseBody = response.body();
            if (responseBody != null) {
                try {
                    return responseBody.string();
                } catch (Exception e) {
                    logger.error(BaseUtil.getExceptionStackTrace(e));
                    return "";
                }
            } else {
                return "";
            }
        } else {
            return "";
        }
    }

    public static void main(String[] args) throws IOException {
        System.out.println(httpGet("https://www.baidu.com", null));
    }
}
