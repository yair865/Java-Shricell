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

import static consts.Constants.GSON_INSTANCE;
import static consts.Constants.SHEETS_LIST;

public class SheetsListRefresher extends TimerTask {

    private final Consumer<List<SingleSheetData>> sheetsListConsumer;
    private int requestNumber;

    public SheetsListRefresher(Consumer<List<SingleSheetData>> sheetListConsumer) {
        this.sheetsListConsumer = sheetListConsumer;
        requestNumber = 0;
    }

    @Override
    public void run() {
        final int finalRequestNumber = ++requestNumber;
        HttpClientUtil.runAsync(SHEETS_LIST, new Callback() {

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                System.out.println(e.getMessage());
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String jsonArrayOfSheets = response.body().string();

                System.out.println("Response JSON: " + jsonArrayOfSheets);

                SheetInfoDTO[] sheetsDTO = GSON_INSTANCE.fromJson(jsonArrayOfSheets, SheetInfoDTO[].class);

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
