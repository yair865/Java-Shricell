package component.dashboard.body.sheetListArea;

import constants.Constants;
import dto.dtoPackage.SheetInfoDTO;
import javafx.beans.property.BooleanProperty;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;
import util.HttpClientUtil;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.TimerTask;
import java.util.function.Consumer;

import static constants.Constants.GSON_INSTANCE;

public class SheetsListRefresher extends TimerTask {

    private final Consumer<List<SheetInfoDTO>> sheetsListConsumer;
    private int requestNumber;

    public SheetsListRefresher(Consumer<List<SheetInfoDTO>> sheetListConsumer) {
        this.sheetsListConsumer = sheetListConsumer;
        requestNumber = 0;
    }

    @Override
    public void run() {
        final int finalRequestNumber = ++requestNumber;
        HttpClientUtil.runAsync(Constants.SHEETS_LIST, new Callback() {

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                System.out.println(e.getMessage());
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String jsonArrayOfSheets = response.body().string();

                System.out.println("Response JSON: " + jsonArrayOfSheets);

                SheetInfoDTO[] sheets = GSON_INSTANCE.fromJson(jsonArrayOfSheets, SheetInfoDTO[].class);

                if (sheets != null) {
                    sheetsListConsumer.accept(List.of(sheets));
                } else {
                    sheetsListConsumer.accept(List.of());
                }
            }

        });
    }
}
