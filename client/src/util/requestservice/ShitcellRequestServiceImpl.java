package util.requestservice;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import dto.deserialize.CoordinateTypeAdapter;
import dto.deserialize.EffectiveValueTypeAdapter;
import dto.dtoPackage.CellDTO;
import dto.dtoPackage.SpreadsheetDTO;
import engine.sheetimpl.cellimpl.api.EffectiveValue;
import engine.sheetimpl.cellimpl.coordinate.Coordinate;
import javafx.application.Platform;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;
import util.HttpClientUtil;
import util.alert.AlertUtil;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import static consts.Constants.*;

public class ShitcellRequestServiceImpl implements ShticellRequestService{

    @Override
    public List<CellDTO> updateCell(String cellId, String newValue) {
        String url = UPDATE_CELL; // Your servlet path

        // Create the request body
        RequestBody requestBody = new FormBody.Builder()
                .add("cellId", cellId)
                .add("newValue", newValue)
                .build();

        // Create a Gson instance with the CoordinateTypeAdapter
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Coordinate.class, new CoordinateTypeAdapter())
                .registerTypeAdapter(EffectiveValue.class, new EffectiveValueTypeAdapter())
                .create();

        Response response = null; // Declare the response variable outside the try block
        try {
            response = HttpClientUtil.runSyncPost(url, requestBody); // Execute the POST request

            if (response.isSuccessful()) {
                assert response.body() != null;
                String jsonResponse = response.body().string();

                Type listType = new TypeToken<List<CellDTO>>() {
                }.getType();

                return gson.fromJson(jsonResponse, listType);
            } else {
                throw new RuntimeException("Update failed: " + response.message());
            }
        } catch (IOException e) {
            throw new RuntimeException("Request failed: " + e.getMessage(), e);
        }
    }

    @Override
    public SpreadsheetDTO getSpreadsheetState() throws Exception {
        return null;
    }

    @Override
    public SpreadsheetDTO getSpreadsheetByVersion(int version) throws Exception {
        return null;
    }

    @Override
    public List<Coordinate> getDependents(String cellId) {
        String url = HttpUrl.get(DEPENDENTS).newBuilder()
                .addQueryParameter("cellId", cellId)
                .build()
                .toString();

        List<Coordinate> dependentsList = new ArrayList<>(); // Create a list to hold the dependents

        HttpClientUtil.runAsync(url, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Platform.runLater(() -> AlertUtil.showErrorAlert("Dependency Error", "Failed to send request: " + e.getMessage()));
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    String responseBody = response.body().string();

                    // Create a Gson instance with the CoordinateTypeAdapter
                    Gson gson = new GsonBuilder()
                            .registerTypeAdapter(Coordinate.class, new CoordinateTypeAdapter())
                            .create();

                    dependentsList.addAll(gson.fromJson(responseBody, new TypeToken<List<Coordinate>>(){}.getType()));
                } else {
                    Platform.runLater(() -> AlertUtil.showErrorAlert("Dependency Error", "Failed to retrieve dependents: " + response.message()));
                }
            }
        });

        return dependentsList; // Return the empty list initially; actual data will be updated later
    }

    @Override
    public List<Coordinate> getReferences(String cellId) {
        String url = HttpUrl.get(REFERENCES).newBuilder()
                .addQueryParameter("cellId", cellId)
                .build()
                .toString();

        List<Coordinate> referencesList = new ArrayList<>(); // Create a list to hold the references

        HttpClientUtil.runAsync(url, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Platform.runLater(() -> AlertUtil.showErrorAlert("Reference Error", "Failed to send request: " + e.getMessage()));
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    String responseBody = response.body().string();

                    // Create a Gson instance with the CoordinateTypeAdapter
                    Gson gson = new GsonBuilder()
                            .registerTypeAdapter(Coordinate.class, new CoordinateTypeAdapter())
                            .create();

                    referencesList.addAll(gson.fromJson(responseBody, new TypeToken<List<Coordinate>>(){}.getType()));
                } else {
                    Platform.runLater(() -> AlertUtil.showErrorAlert("Reference Error", "Failed to retrieve references: " + response.message()));
                }
            }
        });

        return referencesList; // Return the empty list initially; actual data will be updated later
    }

    @Override
    public SpreadsheetDTO sort(String cellsRange, List<Character> selectedColumns) throws Exception {
        return null;
    }

    @Override
    public List<String> getUniqueValuesFromColumn(char column) throws Exception {
        return List.of();
    }

    @Override
    public SpreadsheetDTO filterSheet(Character selectedColumn, String filterArea, List<String> selectedValues) throws Exception {
        return null;
    }

    @Override
    public void setSingleCellBackGroundColor(String cellId, String hexString) {

    }

    @Override
    public void setSingleCellTextColor(String cellId, String hexString) {

    }

    @Override
    public void removeRangeFromSheet(String selectedRange) {

    }

    @Override
    public void addRangeToSheet(String rangeName, String coordinates) {

    }

    @Override
    public List<Coordinate> getRangeByName(String rangeName) {
        return List.of();
    }
}
