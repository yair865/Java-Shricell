package component.dashboard.body.sheetListArea;

import component.dashboard.body.sheetListArea.sheetdata.SingleSheetData;
import dto.dtoPackage.SheetInfoDTO;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;
import util.HttpClientUtil;

import java.io.IOException;
import java.util.List;
import java.util.TimerTask;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import static consts.Constants.*;

public class SheetsListRefresher extends TimerTask {

    private final Consumer<List<SingleSheetData>> sheetsListConsumer;


    public SheetsListRefresher(Consumer<List<SingleSheetData>> sheetListConsumer) {
        this.sheetsListConsumer = sheetListConsumer;
    }

    @Override
    public void run() {

        HttpClientUtil.runAsyncGet(SHEETS_LIST, new Callback() {

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                System.out.println(e.getMessage());
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String jsonArrayOfSheets = response.body().string();

                System.out.println("Response JSON: " + jsonArrayOfSheets);

                SheetInfoDTO[] sheetsDTO = ADAPTED_GSON.fromJson(jsonArrayOfSheets, SheetInfoDTO[].class);

                List<SingleSheetData> sheets = List.of(sheetsDTO).stream()
                        .map(dto -> new SingleSheetData(
                                dto.userName(),
                                dto.sheetName(),
                                dto.numberOfRow() + " x " + dto.numberOfColumn(),
                                dto.getPermission()))
                        .collect(Collectors.toList());

                if (sheets != null) {
                    sheetsListConsumer.accept(sheets);
                } else {
                    sheetsListConsumer.accept(List.of());
                }
            }
        });
    }
}
