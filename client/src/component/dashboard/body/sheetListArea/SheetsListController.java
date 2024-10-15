package component.dashboard.body.sheetListArea;

import dto.dtoPackage.SheetInfoDTO;
import javafx.application.Platform;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;

import java.io.Closeable;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static constants.Constants.REFRESH_RATE;

public class SheetsListController implements Closeable {

    private Timer timer;
    private TimerTask listRefresher;
    private final IntegerProperty totalSheets;

    @FXML
    private TableView<SheetInfoDTO> sheetsTableView;

    @FXML
    private TableColumn<SheetInfoDTO, String> userNameColumn;

    @FXML
    private TableColumn<SheetInfoDTO, String> sheetNameColumn;

    @FXML
    private TableColumn<SheetInfoDTO, String> sizeColumn;

    public SheetsListController() {
        totalSheets = new SimpleIntegerProperty();
    }

    @FXML
    public void initialize() {
        userNameColumn.setCellValueFactory(new PropertyValueFactory<>("userName"));

        sheetNameColumn.setCellValueFactory(new PropertyValueFactory<>("sheetName"));

        sizeColumn.setCellValueFactory(cellData -> {
            SheetInfoDTO sheetInfo = cellData.getValue();
            String size = sheetInfo.numberOfRow() + " x " + sheetInfo.numberOfColumn();
            return new javafx.beans.property.SimpleStringProperty(size);
        });
    }

    private void updateSheetsList(List<SheetInfoDTO> sheets) {
        Platform.runLater(() -> {
            ObservableList<SheetInfoDTO> items = sheetsTableView.getItems();
            items.clear();
            items.addAll(sheets);
            totalSheets.set(sheets.size());
        });
    }

    public void startListRefresher() {
        listRefresher = new SheetsListRefresher(
                this::updateSheetsList);
        timer = new Timer();
        timer.schedule(listRefresher, REFRESH_RATE, REFRESH_RATE);
    }

    @Override
    public void close() {
        sheetsTableView.getItems().clear();
        totalSheets.set(0);
        if (listRefresher != null && timer != null) {
            listRefresher.cancel();
            timer.cancel();
        }
    }


}
