package util.requestservice;

import com.google.gson.reflect.TypeToken;
import consts.Constants;
import dto.dtoPackage.CellDTO;
import dto.dtoPackage.SpreadsheetDTO;
import engine.sheetimpl.cellimpl.coordinate.Coordinate;
import javafx.application.Platform;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;
import util.HttpClientUtil;
import util.alert.AlertUtil;

import java.io.IOException;
import java.lang.invoke.ConstantCallSite;
import java.lang.reflect.Type;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import static consts.Constants.*;

public class ShitcellRequestServiceImpl implements ShticellRequestService{

    @Override
    public void updateCell(String cellId, String newValue, Consumer<List<CellDTO>> onSuccess) {
        String url = UPDATE_CELL;

        // Create the request body
        RequestBody requestBody = new FormBody.Builder()
                .add("cellId", cellId)
                .add("newValue", newValue)
                .build();

        Response response = null;
        try {
            HttpClientUtil.runAsyncPost(url, requestBody, new Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                    throw new RuntimeException("Request failed: " + e.getMessage(), e);
                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    if (response.isSuccessful()) {
                        assert response.body() != null;
                        String jsonResponse = response.body().string();

                        Type listType = new TypeToken<List<CellDTO>>() {}.getType();
                        List<CellDTO> cellsThatHaveChanged = ADAPTED_GSON.fromJson(jsonResponse, listType);

                        // Execute the onSuccess consumer in the JavaFX application thread
                        Platform.runLater(() -> onSuccess.accept(cellsThatHaveChanged));
                    } else {
                        throw new RuntimeException("Update failed: " + response.message());
                    }
                }
            });
        } catch (Exception e) {
            throw new RuntimeException("Request failed: " + e.getMessage(), e);
        }
    }

    @Override
    public void getSpreadSheetByVersion(int version, Consumer<SpreadsheetDTO> onSuccess) {

        String url = HttpUrl.get(VERSION).newBuilder()
                .addQueryParameter("version", String.valueOf(version))
                .build()
                .toString();

        HttpClientUtil.runAsyncGet(url, new Callback(){
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Platform.runLater(() -> AlertUtil.showErrorAlert("Version Error", "An error occurred while retrieving the spreadsheet version: " + e.getMessage()));
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    String jsonResponse = response.body().string();

                    SpreadsheetDTO spreadsheetDTO = ADAPTED_GSON.fromJson(jsonResponse, SpreadsheetDTO.class);

                    Platform.runLater(() -> onSuccess.accept(spreadsheetDTO));
                } else {
                    Platform.runLater(() -> AlertUtil.showErrorAlert("Version Error", "Failed to retrieve spreadsheet: " + response.message()));
                }
            }
        });
    }

    @Override
    public void getDependents(String cellId, Consumer<List<Coordinate>> onSuccess) {
        String url = HttpUrl.get(DEPENDENTS).newBuilder()
                .addQueryParameter("cellId", cellId)
                .build()
                .toString();

        HttpClientUtil.runAsyncGet(url, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Platform.runLater(() -> AlertUtil.showErrorAlert("Dependency Error", "Failed to send request: " + e.getMessage()));
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    String responseBody = response.body().string();

                    List<Coordinate> dependents = ADAPTED_GSON.fromJson(responseBody, new TypeToken<List<Coordinate>>() {}.getType());

                    if (dependents != null) {
                        Platform.runLater(() -> onSuccess.accept(dependents));
                    }
                } else {
                    Platform.runLater(() -> AlertUtil.showErrorAlert("Dependency Error", "Failed to retrieve dependents: " + response.message()));
                }
            }
        });
    }

    @Override
    public void getReferences(String cellId, Consumer<List<Coordinate>> onSuccess) {
        String url = HttpUrl.get(REFERENCES).newBuilder()
                .addQueryParameter("cellId", cellId)
                .build()
                .toString();

        HttpClientUtil.runAsyncGet(url, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Platform.runLater(() -> AlertUtil.showErrorAlert("Reference Error", "Failed to send request: " + e.getMessage()));
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    String responseBody = response.body().string();

                    List<Coordinate> references = ADAPTED_GSON.fromJson(responseBody, new TypeToken<List<Coordinate>>() {}.getType());

                    if (references != null) {
                        Platform.runLater(() -> onSuccess.accept(references));
                    }
                } else {
                    Platform.runLater(() -> AlertUtil.showErrorAlert("Reference Error", "Failed to retrieve references: " + response.message()));
                }
            }
        });
    }

    @Override
    public void sort(String cellsRange, List<Character> selectedColumns, Consumer<SpreadsheetDTO> sortedSheetConsumer) {

        String columnsParam = selectedColumns.stream()
                .map(String::valueOf)
                .collect(Collectors.joining(","));

        String url = HttpUrl.get(SORT)
                .newBuilder()
                .addQueryParameter("cellsRange", cellsRange)
                .addQueryParameter("selectedColumns", columnsParam)
                .build()
                .toString();

        HttpClientUtil.runAsyncGet(url, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Platform.runLater(() ->
                        AlertUtil.showErrorAlert("Sorting Error", "Failed to send request: " + e.getMessage())
                );
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    String responseBody = response.body().string();
                    SpreadsheetDTO sortedSheet = ADAPTED_GSON.fromJson(responseBody, SpreadsheetDTO.class);
                    Platform.runLater(() -> sortedSheetConsumer.accept(sortedSheet));
                } else {
                    Platform.runLater(() ->
                            AlertUtil.showErrorAlert("Sorting Error", "Failed to retrieve sorted sheet: " + response.message())
                    );
                }
            }
        });
    }


    @Override
    public void getEffectiveValuesForColumn(char column, Consumer<List<String>> valuesConsumer) {
        String url = HttpUrl.get(GET_UNIQUE_VALUES) // Assuming you have an EFFECTIVE_VALUES constant
                .newBuilder()
                .addQueryParameter("column", String.valueOf(column))
                .build()
                .toString();

        HttpClientUtil.runAsyncGet(url, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Platform.runLater(() ->
                        AlertUtil.showErrorAlert("Column Retrieval Error", "Failed to send request: " + e.getMessage())
                );
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    String responseBody = response.body().string();
                    List<String> values = ADAPTED_GSON.fromJson(responseBody, new TypeToken<List<String>>() {}.getType());
                    Platform.runLater(() -> valuesConsumer.accept(values));
                } else {
                    Platform.runLater(() ->
                            AlertUtil.showErrorAlert("Column Retrieval Error", "Failed to retrieve values: " + response.message())
                    );
                }
            }
        });
    }

    @Override
    public void filterSheet(Character selectedColumn, String filterArea, List<String> selectedValues, Consumer<SpreadsheetDTO> filteredSheetConsumer) {
        String valuesParam = String.join(",", selectedValues); // Assuming selectedValues is a List<String>

        String url = HttpUrl.get(FILTER) // Assuming you have a FILTER constant
                .newBuilder()
                .addQueryParameter("selectedColumn", String.valueOf(selectedColumn))
                .addQueryParameter("filterArea", filterArea)
                .addQueryParameter("selectedValues", valuesParam)
                .build()
                .toString();

        HttpClientUtil.runAsyncGet(url, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Platform.runLater(() ->
                        AlertUtil.showErrorAlert("Filtering Error", "Failed to send request: " + e.getMessage())
                );
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    String responseBody = response.body().string();
                    SpreadsheetDTO filteredSheet = ADAPTED_GSON.fromJson(responseBody, SpreadsheetDTO.class);
                    Platform.runLater(() -> filteredSheetConsumer.accept(filteredSheet));
                } else {
                    Platform.runLater(() ->
                            AlertUtil.showErrorAlert("Filtering Error", "Failed to retrieve filtered sheet: " + response.message())
                    );
                }
            }
        });
    }

    @Override
    public void setSingleCellBackGroundColor(String cellId, String hexString, Runnable callback) {
        String url = HttpUrl.get(Constants.BACKGROUND_COLOR)
                .newBuilder()
                .addQueryParameter("cellId", cellId)
                .addQueryParameter("color", hexString)
                .build()
                .toString();

        HttpClientUtil.runAsyncPut(url, hexString,new Callback() {

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Platform.runLater(() ->
                        AlertUtil.showErrorAlert("Error", "Failed to update cell background color: " + e.getMessage())
                );
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    Platform.runLater(callback);
                } else {
                    Platform.runLater(() ->
                            AlertUtil.showErrorAlert("Error", "Failed to update background color: " + response.message())
                    );
                }
            }
        });
    }

    @Override
    public void setSingleCellTextColor(String cellId, String hexString, Runnable callback) {
        String url = HttpUrl.get(TEXT_COLOR)
                .newBuilder()
                .addQueryParameter("cellId", cellId)
                .addQueryParameter("color", hexString)
                .build()
                .toString();

        HttpClientUtil.runAsyncPut(url, hexString,new Callback() {

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Platform.runLater(() ->
                        AlertUtil.showErrorAlert("Error", "Failed to update cell text color: " + e.getMessage())
                );
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    Platform.runLater(callback);
                } else {
                    Platform.runLater(() ->
                            AlertUtil.showErrorAlert("Error", "Failed to update background color: " + response.message())
                    );
                }
            }
        });
    }

    @Override
    public void removeRangeFromSheet(String selectedRange) {

    }

    @Override
    public void addRangeToSheet(String rangeName, String coordinates) {

    }

    @Override
    public void getRangeByName(String rangeName , Consumer<List<Coordinate>> onSuccess) {
        String url = HttpUrl.get(GET_RANGE).newBuilder()
                .addQueryParameter("rangeName", rangeName)
                .build()
                .toString();

        HttpClientUtil.runAsyncGet(url, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Platform.runLater(() -> AlertUtil.showErrorAlert("Range Error", "Failed to send request: " + e.getMessage()));
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    String responseBody = response.body().string();

                    List<Coordinate> references = ADAPTED_GSON.fromJson(responseBody, new TypeToken<List<Coordinate>>() {}.getType());

                    if (references != null) {
                        Platform.runLater(() -> onSuccess.accept(references));
                    }
                } else {
                    Platform.runLater(() -> AlertUtil.showErrorAlert("Range Error", "Failed to retrieve range cells: " + response.message()));
                }
            }
        });
    }

}
