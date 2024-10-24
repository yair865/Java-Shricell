package util;

import javafx.application.Platform;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;
import util.alert.AlertUtil;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;
import java.util.function.Consumer;

public class HttpClientUtil {

    private final static SimpleCookieManager simpleCookieManager = new SimpleCookieManager();
    private final static OkHttpClient HTTP_CLIENT =
            new OkHttpClient.Builder()
                    .cookieJar(simpleCookieManager)
                    .followRedirects(false)
                    .build();

    public static void setCookieManagerLoggingFacility(Consumer<String> logConsumer) {
        simpleCookieManager.setLogData(logConsumer);
    }

    public static void removeCookiesOf(String domain) {
        simpleCookieManager.removeCookiesOf(domain);
    }

    public static void runAsyncGet(String finalUrl, Callback callback) {
        Request request = new Request.Builder()
                .url(finalUrl)
                .build();

        Call call = HTTP_CLIENT.newCall(request);
        call.enqueue(callback);
    }

    public static void runAsyncPost(String url, RequestBody requestBody, Callback callback) {
        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();

        Call call = HTTP_CLIENT.newCall(request);
        call.enqueue(callback);
    }

    public static void shutdown() {
        System.out.println("Shutting down HTTP CLIENT");
        HTTP_CLIENT.dispatcher().executorService().shutdown();
        HTTP_CLIENT.connectionPool().evictAll();
    }

    public static Response runSyncPost(String url, RequestBody requestBody) {
        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();

        Call call = HTTP_CLIENT.newCall(request);
        final CountDownLatch latch = new CountDownLatch(1);
        final Response[] responseHolder = new Response[1];

        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                latch.countDown();
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                responseHolder[0] = response;
                latch.countDown();
            }
        });

        try {
            latch.await();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        return responseHolder[0];
    }

    public static void runAsyncPut(String url, String data, Callback callback) {
        Request request = new Request.Builder()
                .url(url)
                .put(RequestBody.create(data, MediaType.parse("application/json"))) // Assuming data is in JSON format
                .build();

        Call call = HTTP_CLIENT.newCall(request);
        call.enqueue(callback);
    }
}
