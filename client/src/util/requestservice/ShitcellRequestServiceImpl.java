package util.requestservice;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
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
import java.lang.reflect.Type;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import static consts.Constants.*;

public class ShitcellRequestServiceImpl implements ShticellRequestService{

    @Override
    public void updateCell(String cellId, String newValue, int clientVersion, Consumer<List<CellDTO>> onSuccess) {
        String url = UPDATE_CELL;

        RequestBody requestBody = new FormBody.Builder()
                .add("cellId", cellId)
                .add("newValue", newValue)
                .add("clientVersion", String.valueOf(clientVersion))
                .build();

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

                    Platform.runLater(() -> onSuccess.accept(cellsThatHaveChanged));
                } else {
                    handleErrorResponse(response, "Failed to update the cell.");
                }
            }
        });
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
                    handleErrorResponse(response, "Failed to retrieve spreadsheet.");
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
                    handleErrorResponse(response, "Failed to retrieve dependencies");
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
                    handleErrorResponse(response, "Failed to retrieve references");
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
                    handleErrorResponse(response, "Failed to sort sheet");
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
                    handleErrorResponse(response, "Failed to retrieve effective value");;
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
                    handleErrorResponse(response, "Failed to filter sheet.");
                }
            }
        });
    }

    @Override
    public void setSingleCellBackGroundColor(String cellId, String hexString,int clientVersion ,Runnable callback) {
        String url = HttpUrl.get(Constants.BACKGROUND_COLOR)
                .newBuilder()
                .addQueryParameter("cellId", cellId)
                .addQueryParameter("color", hexString)
                .addQueryParameter("clientVersion", String.valueOf(clientVersion))
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
                    String errorJson = response.body().string();
                    Platform.runLater(callback);
                } else {
                    handleErrorResponse(response, "Failed to set cell background color");
                }
            }
        });
    }

    @Override
    public void setSingleCellTextColor(String cellId, String hexString, int clientVersion, Runnable callback) {
        String url = HttpUrl.get(TEXT_COLOR)
                .newBuilder()
                .addQueryParameter("cellId", cellId)
                .addQueryParameter("color", hexString)
                .addQueryParameter("clientVersion", String.valueOf(clientVersion))
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
                    handleErrorResponse(response, "Failed to set cell text color");
                }
            }
        });
    }

    @Override
    public void removeRangeFromSheet(String selectedRange,int clientVersion, Consumer<String> callback) {
        String url = HttpUrl.get(REMOVE_RANGE)
                .newBuilder()
                .addQueryParameter("range", selectedRange)
                .addQueryParameter("clientVersion", String.valueOf(clientVersion))
                .build()
                .toString();

        HttpClientUtil.runAsyncDelete(url, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Platform.runLater(() -> AlertUtil.showErrorAlert("Failed to remove range: " , e.getMessage()));
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    String result = response.body().string();
                    Platform.runLater(() -> callback.accept(result));
                } else {
                    handleErrorResponse(response, "Failed to remove range");
                }
            }
        });
    }

    @Override
    public void addRangeToSheet(String rangeName, String coordinates, int clientVersion, Consumer<String> callback) {
        String url = HttpUrl.get(ADD_RANGE)
                .newBuilder()
                .addQueryParameter("rangeName", rangeName)
                .addQueryParameter("coordinates", coordinates)
                .addQueryParameter("clientVersion", String.valueOf(clientVersion))
                .build()
                .toString();

        HttpClientUtil.runAsyncPut(url, coordinates, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Platform.runLater(() -> AlertUtil.showErrorAlert("Failed to add range: ", e.getMessage()));
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    String json = response.body().string();
                    Platform.runLater(() -> callback.accept(json));
                } else {
                    handleErrorResponse(response, "Failed to add range");
                }
            }
        });
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
                    handleErrorResponse(response, "Failed to retrieve range");
                }
            }
        });
    }

    @Override
    public void getExpectedValue(Coordinate cellToCalculate, String newValueOfCell, Consumer<SpreadsheetDTO> updateView) {
        String jsonObject = String.format("{\"row\":%d,\"column\":%d,\"value\":\"%s\"}",
                cellToCalculate.row(),
                cellToCalculate.column(),
                newValueOfCell);

        RequestBody requestBody = RequestBody.create(
                jsonObject,
                okhttp3.MediaType.parse("application/json")
        );

        HttpClientUtil.runAsyncPost(Constants.GET_EXPECTED_VALUE_PATH, requestBody, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Platform.runLater(() -> AlertUtil.showErrorAlert("Expected Value Error", "Request failed: " + e.getMessage()));
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    String responseBody = response.body().string();
                    SpreadsheetDTO sheetDTO = ADAPTED_GSON.fromJson(responseBody, SpreadsheetDTO.class);
                    Platform.runLater(() -> updateView.accept(sheetDTO));
                } else {
                    handleErrorResponse(response, "Failed to retrieve expected value ");
                }
            }
        });
    }

    @Override
    public void getLatestVersion(Consumer<SpreadsheetDTO> versionConsumer) {
        String url = HttpUrl.get(GET_LATEST_VERSION).newBuilder()
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

                    Platform.runLater(() -> versionConsumer.accept(spreadsheetDTO));
                } else {
                    handleErrorResponse(response, "Failed to retrieve updated sheet");
                }
            }
        });
    }

    private void handleErrorResponse(Response response, String defaultErrorMessage) throws IOException {
        String errorJson = response.body().string();
        System.out.println("Error JSON Response: " + errorJson);

        try {
            JsonObject errorObject = JsonParser.parseString(errorJson).getAsJsonObject();
            String errorMessage = errorObject.get("error").getAsString();
            Platform.runLater(() ->
                    AlertUtil.showErrorAlert("Error Updating Sheet", defaultErrorMessage + ": " + errorMessage)
            );
        } catch (Exception e) {
            Platform.runLater(() ->
                    AlertUtil.showErrorAlert("Error Updating Sheet", "An unexpected error occurred.")
            );
        }
    }

}
