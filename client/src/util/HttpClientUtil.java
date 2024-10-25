package util;

import javafx.application.Platform;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;
import util.alert.AlertUtil;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;
import java.util.function.Consumer;

public class HttpClientUtil {

    private static final SimpleCookieManager simpleCookieManager = new SimpleCookieManager();
    private static final OkHttpClient HTTP_CLIENT = new OkHttpClient.Builder()
            .cookieJar(simpleCookieManager)
            .followRedirects(false)
            .build();

    // Public HTTP methods
    public static void setCookieManagerLoggingFacility(Consumer<String> logConsumer) {
        simpleCookieManager.setLogData(logConsumer);
    }

    public static void removeCookiesOf(String domain) {
        simpleCookieManager.removeCookiesOf(domain);
    }

    public static void runAsyncGet(String url, Callback callback) {
        runAsyncRequest(buildRequest(url, "GET", null), callback);
    }

    public static void runAsyncPost(String url, RequestBody requestBody, Callback callback) {
        runAsyncRequest(buildRequest(url, "POST", requestBody), callback);
    }

    public static void runAsyncPut(String url, String data, Callback callback) {
        RequestBody requestBody = RequestBody.create(data, MediaType.parse("application/json"));
        runAsyncRequest(buildRequest(url, "PUT", requestBody), callback);
    }

    public static void runAsyncDelete(String url, Callback callback) {
        runAsyncRequest(buildRequest(url, "DELETE", null), callback);
    }


    public static void shutdown() {
        System.out.println("Shutting down HTTP CLIENT");
        HTTP_CLIENT.dispatcher().executorService().shutdown();
        HTTP_CLIENT.connectionPool().evictAll();
    }


    private static Request buildRequest(String url, String method, RequestBody requestBody) {
        Request.Builder builder = new Request.Builder().url(url);
        switch (method) {
            case "POST" -> builder.post(requestBody);
            case "PUT" -> builder.put(requestBody);
            case "DELETE" -> builder.delete();
            default -> builder.get();
        }
        return builder.build();
    }

    private static void runAsyncRequest(Request request, Callback callback) {
        Call call = HTTP_CLIENT.newCall(request);
        call.enqueue(callback);
    }

}
