package component.dashboard.right;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import component.main.MainController;
import constant.Constants;
import dto.deserialize.CoordinateTypeAdapter;
import dto.deserialize.EffectiveValueTypeAdapter;
import dto.deserialize.SpreadsheetDTODeserializer;
import dto.dtoPackage.SpreadsheetDTO;
import engine.sheetimpl.cellimpl.api.EffectiveValue;
import engine.sheetimpl.cellimpl.coordinate.Coordinate;
import javafx.application.Platform;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;
import util.HttpClientUtil;

import java.io.IOException;

import static consts.Constants.GSON_INSTANCE;
import static consts.Constants.VIEW_SHEET_URL;


public class DashboardRightController {

    private MainController mainController;

    @FXML
    private VBox dashboardRight;

    @FXML
    private Button permissionApproveButton;

    @FXML
    private Button requestPermissionButton;

    @FXML
    private HBox right;

    @FXML
    private Button viewSheetButton;

    private StringProperty currentSheetName;

    @FXML
    void permissionApproveListener(ActionEvent event) {
    }

    @FXML
    void requestPermissionButtonListener(ActionEvent event) {
    }

    @FXML
    void viewSheetListener(ActionEvent event) {
        String selectedSheetName = currentSheetName.get();

        if (selectedSheetName == null || selectedSheetName.isEmpty()) {
            System.out.println("No sheet selected.");
            return;
        }

        String url = VIEW_SHEET_URL + "?sheetName=" + selectedSheetName;

        HttpClientUtil.runAsync(url, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                System.out.println("Failed to send request: " + e.getMessage());
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    String responseBody = response.body().string();


                    Gson gson = new GsonBuilder()
                            .registerTypeAdapter(SpreadsheetDTO.class, new SpreadsheetDTODeserializer())
                            .registerTypeAdapter(Coordinate.class, new CoordinateTypeAdapter())
                            .registerTypeAdapter(EffectiveValue.class ,new EffectiveValueTypeAdapter())
                            .create();

                    SpreadsheetDTO spreadsheetDTO = gson.fromJson(responseBody, SpreadsheetDTO.class);

                    System.out.println("Spreadsheet data received: " + spreadsheetDTO);
                    handleSpreadsheetData(spreadsheetDTO);
                } else {
                    System.out.println("Failed to get spreadsheet data: " + response.message());
                }
            }
        });
    }

    private void handleSpreadsheetData(SpreadsheetDTO spreadsheetDTO) {
        Platform.runLater(() -> {
            mainController.loadApplicationPage(spreadsheetDTO);
        });
    }

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    public void setCurrentSheetNameProperty(StringProperty currentSheetName) {
        this.currentSheetName = currentSheetName;
    }
}

