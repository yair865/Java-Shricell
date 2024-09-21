package application.body;

import application.app.ShticellController;
import application.model.CellDataProvider;
import engine.api.Coordinate;
import engine.sheetimpl.cellimpl.coordinate.CoordinateFactory;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.StackPane;

import java.io.IOException;
import java.util.Objects;

public class BodyController {

    private ShticellController shticellController;

    private GridPane gridPane;

    private ScrollPane scrollPane;

    private StackPane centeredGridPane;

    public BodyController() {
        this.gridPane = new GridPane();
        this.centeredGridPane = new StackPane(gridPane);
        this.scrollPane = new ScrollPane(centeredGridPane);

        gridPane.getStylesheets().add(Objects.requireNonNull(getClass().getResource("bodyStyle.css")).toExternalForm());
    }

    public void createGridPane(int rows, int columns, int rowHeightUnits, int columnWidthUnits, CellDataProvider cellDataProvider) {
        gridPane.getChildren().clear();
        gridPane.getRowConstraints().clear();
        gridPane.getColumnConstraints().clear();

        for (int row = 0; row <= rows; row++) {
            RowConstraints rowConstraints = new RowConstraints();
            rowConstraints.setMinHeight(0);
            rowConstraints.setPrefHeight(rowHeightUnits);
            rowConstraints.setMaxHeight(rowHeightUnits);
            gridPane.getRowConstraints().add(rowConstraints);
        }

        for (int col = 0; col <= columns; col++) {
            ColumnConstraints columnConstraints = new ColumnConstraints();
            columnConstraints.setMinWidth(0);
            columnConstraints.setPrefWidth(columnWidthUnits);
            columnConstraints.setMaxWidth(columnWidthUnits);
            gridPane.getColumnConstraints().add(columnConstraints);
        }
        gridPane.getColumnConstraints().set(0, new ColumnConstraints(30));

        // Create header labels
        for (int col = 0; col < columns; col++) {
            Label columnLabel = new Label(getColumnLetter(col + 1));
            columnLabel.setAlignment(Pos.CENTER);
            columnLabel.setPrefWidth(columnWidthUnits);
            columnLabel.setPrefHeight(rowHeightUnits);
            columnLabel.getStyleClass().add("header");
            gridPane.add(columnLabel, col + 1, 0);
        }

        for (int row = 0; row < rows; row++) {
            Label rowLabel = new Label(Integer.toString(row + 1));
            rowLabel.setPrefWidth(30);
            rowLabel.setPrefHeight(rowHeightUnits);
            rowLabel.setAlignment(Pos.CENTER);
            rowLabel.getStyleClass().add("header");
            gridPane.add(rowLabel, 0, row + 1);
        }


        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < columns; col++) {
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("CellView.fxml"));
                    StackPane cellView = loader.load();
                    CellViewController cellViewController = loader.getController();
                    cellViewController.setShticellController(shticellController);

                    Coordinate coordinate = CoordinateFactory.createCoordinate(row + 1, col + 1);
                    BasicCellData cellData = cellDataProvider.getCellData(coordinate);

                    cellViewController.effectiveValue.bind(cellData.effectiveValue);
                    cellViewController.originalValue.bind(cellData.originalValue);
                    cellViewController.cellId.bind(cellData.cellId);
                    cellViewController.lastModifiedVersion.bind(cellData.lastModifiedVersion);
                    cellViewController.textColor.bind(cellData.textColor);
                    cellViewController.backgroundColor.bind(cellData.backgroundColor);

                    String textColor = cellData.textColor.get();
                    String backgroundColor = cellData.backgroundColor.get();

                    if (textColor != null) {
                        updateCellTextColor(cellData.cellId.get(), textColor);
                    }
                    if (backgroundColor != null) {
                        updateCellBackgroundColor(cellData.cellId.get(), backgroundColor);
                    }

                    cellView.setMinSize(columnWidthUnits, rowHeightUnits);
                    cellView.setPrefSize(columnWidthUnits, rowHeightUnits);
                    cellView.setMaxSize(columnWidthUnits, rowHeightUnits);
                    cellView.getStyleClass().add("cell");

                    cellView.setUserData(cellViewController);

                    gridPane.add(cellView, col + 1, row + 1);
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

    private String getColumnLetter(int columnNumber) {
        StringBuilder columnLetter = new StringBuilder();
        while (columnNumber > 0) {
            columnNumber--;
            columnLetter.insert(0, (char) ('A' + (columnNumber % 26)));
            columnNumber /= 26;
        }
        return columnLetter.toString();
    }

    public void setColumnWidth(int newWidth) {
        int colIndex = getColumnIndex(this.shticellController.getHeaderController().getSelectedCellColumn());
        if (colIndex < 0 || colIndex >= gridPane.getColumnConstraints().size()) {
            System.err.println("Invalid column index: " + colIndex);
            return;
        }

        ColumnConstraints colConstraints = gridPane.getColumnConstraints().get(colIndex);
        colConstraints.setPrefWidth(newWidth);
        colConstraints.setMaxWidth(newWidth);

        for (Node node : gridPane.getChildren()) {
            Integer nodeColIndex = GridPane.getColumnIndex(node);
            if (nodeColIndex != null && nodeColIndex == colIndex) {
                if (node instanceof StackPane stackPane) {
                    stackPane.setPrefWidth(newWidth);
                    stackPane.setMaxWidth(newWidth);
                } else if (node instanceof Label label) {
                    label.setPrefWidth(newWidth);
                }
            }
        }
    }

    public void setRowHeight(int newHeight) {
        int rowIndex = this.shticellController.getHeaderController().getSelectedCellRow();
        if (rowIndex < 0 || rowIndex >= gridPane.getRowConstraints().size()) {
            System.err.println("Invalid row index: " + rowIndex);
            return;
        }

        RowConstraints rowConstraints = gridPane.getRowConstraints().get(rowIndex);
        rowConstraints.setPrefHeight(newHeight);
        rowConstraints.setMaxHeight(newHeight);

        for (Node node : gridPane.getChildren()) {
            Integer nodeRowIndex = GridPane.getRowIndex(node);
            if (nodeRowIndex != null && nodeRowIndex == rowIndex) {
                if (node instanceof StackPane stackPane) {
                    stackPane.setPrefHeight(newHeight);
                    stackPane.setMaxHeight(newHeight);
                } else if (node instanceof Label label) {

                    label.setPrefHeight(newHeight);
                }
            }
        }
    }

    public void alignCells(Pos pos) {
        char columnLetter = this.shticellController.getHeaderController().getSelectedCellColumn();

        int columnNumber = getColumnIndex(columnLetter);

        for (Node node : gridPane.getChildren()) {
            Integer colIndex = GridPane.getColumnIndex(node);

            if (colIndex == null || isHeaderNode(node)) {
                continue;
            }

            if (colIndex == columnNumber) {
                if (node instanceof StackPane stackPane) {
                    stackPane.setAlignment(pos);
                }
            }
        }
    }

    private int getColumnIndex(char columnLetter) {
        int result;
        result =  (columnLetter - 'A' + 1);
        return result;
    }

    private boolean isHeaderNode(Node node) {
        return (node instanceof Label) && (node).getStyleClass().contains("header");
    }

    public void updateCellTextColor(String cellId, String color) {
        Node cellView = findCellViewById(cellId);
        if (cellView != null) {
            for (Node child : ((StackPane) cellView).getChildren()) {
                if (child instanceof Label label) {
                    label.setStyle("-fx-text-fill: " + color + ";");
                }
            }
        }
    }

    public void updateCellBackgroundColor(String cellId, String color) {
        Node cellView = findCellViewById(cellId);
        if (cellView != null) {
            cellView.setStyle("-fx-background-color: " + color + ";");
        }
    }

    private Node findCellViewById(String cellId) {
        for (Node node : gridPane.getChildren()) {
            CellViewController cellViewController = (CellViewController) node.getUserData();
            if (cellViewController != null && cellViewController.cellId.get().equals(cellId)) {
                return node;
            }
        }

        return null;
    }
}
