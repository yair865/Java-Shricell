package application.body;

import application.app.ShticellController;
import application.model.CellDataProvider;
import application.body.BasicCellData;
import engine.api.Coordinate;
import engine.sheetimpl.cellimpl.coordinate.CoordinateFactory;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.StackPane;

import java.io.IOException;

public class BodyController {

    private ShticellController shticellController;

    @FXML
    private GridPane gridPane;

    @FXML
    private ScrollPane scrollPane;

    private StackPane centeredGridPane;

    public BodyController() {
        this.gridPane = new GridPane();
        this.centeredGridPane = new StackPane(gridPane);  // Wrap GridPane in StackPane to center it
        this.scrollPane = new ScrollPane(centeredGridPane);  // Wrap StackPane in ScrollPane
    }

    public void createGridPane(int rows, int columns, int rowHeightUnits, int columnWidthUnits, CellDataProvider cellDataProvider) {
        // Clear existing content in the GridPane
        gridPane.getChildren().clear();
        gridPane.getRowConstraints().clear();
        gridPane.getColumnConstraints().clear();

        // Populate the GridPane with Labels
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < columns; col++) {
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("CellView.fxml"));
                    StackPane cellView = loader.load();

                    CellViewController cellViewController = loader.getController();
                    cellViewController.setShticellController(shticellController);

                    Coordinate coordinate = CoordinateFactory.createCoordinate(row + 1, col + 1);

                    BasicCellData cellData = cellDataProvider.getCellData(coordinate);

                    // Bind the data to the cellViewController
                    cellViewController.effectiveValue.bind(cellData.effectiveValue);
                    cellViewController.originalValue.bind(cellData.originalValue);
                    cellViewController.cellId.bind(cellData.cellId);
                    cellViewController.lastModifiedVersion.bind(cellData.lastModifiedVersion);

                    RowConstraints rowConstraints = new RowConstraints();
                    rowConstraints.setMinHeight(rowHeightUnits);
                    rowConstraints.setMaxHeight(rowHeightUnits);
                    rowConstraints.setPrefHeight(rowHeightUnits);
                    gridPane.getRowConstraints().add(rowConstraints);

                    ColumnConstraints columnConstraints = new ColumnConstraints();
                    columnConstraints.setMinWidth(columnWidthUnits);
                    columnConstraints.setMaxWidth(columnWidthUnits);
                    columnConstraints.setPrefWidth(columnWidthUnits);
                    gridPane.getColumnConstraints().add(columnConstraints);

                    gridPane.add(cellView, col, row);
                } catch (IOException e) {
                    System.out.println("Error creating table: " + e.getMessage());
                }
            }
        }
        gridPane.setGridLinesVisible(false);

        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);

        centeredGridPane.setPrefSize(ScrollPane.USE_COMPUTED_SIZE, ScrollPane.USE_COMPUTED_SIZE);
        centeredGridPane.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
    }

    public ScrollPane getBody() {
        return scrollPane;
    }

    public void setShticellController(ShticellController shticellController) {
        this.shticellController = shticellController;
    }
}
