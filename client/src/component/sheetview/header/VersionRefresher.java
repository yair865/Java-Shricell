package component.sheetview.header;

import com.google.gson.JsonObject;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;
import util.HttpClientUtil;

import java.io.IOException;
import java.util.TimerTask;

import static consts.Constants.ADAPTED_GSON;
import static consts.Constants.HAS_NEW_VERSION;

public class VersionRefresher extends TimerTask {

    private final Runnable updateUserForNewVersion;
    private BooleanProperty shouldUpdate;
    private IntegerProperty currentVersion;

    public VersionRefresher(Runnable updateUserForNewVersion, BooleanProperty shouldUpdate, IntegerProperty currentVersion) {
        this.updateUserForNewVersion = updateUserForNewVersion;
        this.shouldUpdate = shouldUpdate;
        this.currentVersion = currentVersion;
    }

    @Override
    public void run() {
        if (shouldUpdate.get()) {
            String url = HttpUrl.get(HAS_NEW_VERSION).newBuilder()
                    .addQueryParameter("version", String.valueOf(currentVersion.get()))
                    .build()
                    .toString();

            HttpClientUtil.runAsyncGet(url, new Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                    System.out.println(e.getMessage());
                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    String json = response.body().string();
                    System.out.println("Response JSON: " + json);

                    JsonObject jsonObject = ADAPTED_GSON.fromJson(json, JsonObject.class);
                    boolean hasNewVersion = jsonObject.get("hasNewVersion").getAsBoolean();

                    if (hasNewVersion) {
                        updateUserForNewVersion.run();
                    }
                }
            });
        }
    }
}

