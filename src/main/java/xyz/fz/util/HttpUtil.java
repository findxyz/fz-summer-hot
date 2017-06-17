package xyz.fz.util;

import okhttp3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
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

    private static OkHttpClient client = new OkHttpClient
            .Builder()
            .connectTimeout(5, TimeUnit.SECONDS)
            .readTimeout(5, TimeUnit.SECONDS)
            .writeTimeout(5, TimeUnit.SECONDS)
            .build();

    public static String httpPostJson(String url, String json) throws IOException {

        RequestBody requestBody = RequestBody.create(JSON, json);
        Request request = new Request
                .Builder()
                .url(url)
                .post(requestBody)
                .build();
        logger.debug("Http PostJson Url: {}", request.url().toString());
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

    private static String result(Response response) {
        if (response.isSuccessful()) {
            ResponseBody responseBody = response.body();
            if (responseBody != null) {
                return responseBody.toString();
            } else {
                return "";
            }
        } else {
            return "";
        }
    }
}
