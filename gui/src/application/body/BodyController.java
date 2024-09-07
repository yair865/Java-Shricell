package application.body;

import application.app.ShticellController;
import application.model.DataManager;
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

    public void createGridPane(int rows, int columns, int rowHeightUnits, int columnWidthUnits) {
        // Clear existing content in the GridPane
        gridPane.getChildren().clear();
        gridPane.getRowConstraints().clear();
        gridPane.getColumnConstraints().clear();

        // Initialize cell data in DataManager
        DataManager dataManager = shticellController.getDataManager();

        // Populate the GridPane with Labels
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < columns; col++) {
                try {
                    // Load the FXML file for the StackPane (CellView)
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("CellView.fxml"));
                    StackPane cellView = loader.load();

                    // Get the controller for further customization
                    CellViewController cellViewController = loader.getController();
                    cellViewController.setShticellController(shticellController);

                    // Calculate the coordinate based on row and column (1-based index)
                    Coordinate coordinate = CoordinateFactory.createCoordinate(row + 1, col + 1);

                    // Fetch or create the BasicCellData for this coordinate
                    BasicCellData cellData = dataManager.getCellDataMap()
                            .computeIfAbsent(coordinate, key -> new BasicCellData("", "", coordinate.toString()));

                    // Bind the effective value of the cell to the label in the CellView
                    cellViewController.effectiveValue.bind(cellData.effectiveValue);
                    cellViewController.originalValue.bind(cellData.originalValue);
                    cellViewController.cellId.bind(cellData.cellId);
                    cellViewController.lastModifiedVersion.bind(cellData.lastModifiedVersion);

                    // Add custom row constraints for this specific cell
                    RowConstraints rowConstraints = new RowConstraints();
                    rowConstraints.setMinHeight(rowHeightUnits); // Custom row height for this cell
                    rowConstraints.setMaxHeight(rowHeightUnits); // Custom row height for this cell
                    rowConstraints.setPrefHeight(rowHeightUnits); // Custom row height for this cell
                    gridPane.getRowConstraints().add(rowConstraints);

                    // Add custom column constraints for this specific cell
                    ColumnConstraints columnConstraints = new ColumnConstraints();
                    columnConstraints.setMinWidth(columnWidthUnits); // Custom column width for this cell
                    columnConstraints.setMaxWidth(columnWidthUnits); // Custom column width for this cell
                    columnConstraints.setPrefWidth(columnWidthUnits); // Custom column width for this cell
                    gridPane.getColumnConstraints().add(columnConstraints);

                    // Add the cell to the GridPane
                    gridPane.add(cellView, col, row);
                } catch (IOException e) {
                    System.out.println("Error creating table: " + e.getMessage());
                }
            }
        }
        gridPane.setGridLinesVisible(false);

        // Set ScrollPane properties to ensure it scrolls when needed
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);

        // Center the grid inside the ScrollPane
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


