package component.sheetview.body;

import component.sheetview.app.ShticellController;
import component.sheetview.model.CellDataProvider;
import engine.sheetimpl.cellimpl.coordinate.Coordinate;
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
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class BodyController {

    private ShticellController shticellController;

    private GridPane gridPane;

    private ScrollPane scrollPane;


    private Set<String> highlightedCells = new HashSet<>();

    public BodyController() {
        this.gridPane = new GridPane();
        this.scrollPane = new ScrollPane(gridPane);
        scrollPane.getStyleClass().add("scroll-pane");
        gridPane.getStylesheets().add(Objects.requireNonNull(getClass().getResource("bodyStyle.css")).toExternalForm());
    }

    public void createGridPane(int rows, int columns, int rowHeightUnits, int columnWidthUnits, CellDataProvider cellDataProvider) {
        gridPane.getChildren().clear();
        gridPane.getRowConstraints().clear();
        gridPane.getColumnConstraints().clear();

        gridPane.setMinWidth((columnWidthUnits*columns) + 30);
        gridPane.setMinHeight((rowHeightUnits*rows) + 30);

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

                    cellView.setMinSize(columnWidthUnits, rowHeightUnits);
                    cellView.setPrefSize(columnWidthUnits, rowHeightUnits);
                    cellView.setMaxSize(columnWidthUnits, rowHeightUnits);
                    cellView.getStyleClass().add("cell");
                    cellView.setUserData(cellViewController);

                    cellViewController.updateCellStyle();
                    gridPane.add(cellView, col + 1, row + 1);

                } catch (IOException e) {
                    System.out.println("Error creating table: " + e.getMessage());
                }
            }
        }

        gridPane.setGridLinesVisible(false);

/*        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);*/
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

    public void setColumnWidth(int newWidth) { //TODO : fix this method !
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
        result = (columnLetter - 'A' + 1);
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

                    if (color != null) {
                        label.setStyle("-fx-text-fill: " + color + ";");
                    } else {
                        label.setStyle(null);
                    }
                }
            }
        }
    }

    public void updateCellBackgroundColor(String cellId, String color) {
        Node cellView = findCellViewById(cellId);
        if (cellView != null) {
            if (color != null) {
                cellView.setStyle("-fx-background-color: " + color + ";");
            } else {
                cellView.setStyle(null);
            }
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

    public void highlightDependencies(String cellId) {
        clearHighlightedCells();

        shticellController.getDependents(cellId, dependentCells -> {
            if (dependentCells != null) {
                for (Coordinate depCellId : dependentCells) {
                    Node cellView = findCellViewById(depCellId.toString());
                    if (cellView != null) {
                        cellView.getStyleClass().add("depends-on-cell");
                        highlightedCells.add(depCellId.toString());
                    }
                }
            }
        });
    }

    public void highlightDependents(String cellId) {
        shticellController.getReferences(cellId, references -> {
            if (references != null) {
                for (Coordinate influenceCellId : references) {
                    Node cellView = findCellViewById(influenceCellId.toString());
                    if (cellView != null) {
                        cellView.getStyleClass().add("influence-on-cell");
                        highlightedCells.add(influenceCellId.toString());
                    }
                }
            }
        });
    }

    public void clearHighlightedCells() {
        for (String cellId : highlightedCells) {
            Node cellView = findCellViewById(cellId);
            if (cellView != null) {
                cellView.getStyleClass().remove("depends-on-cell");
                cellView.getStyleClass().remove("influence-on-cell");
                cellView.getStyleClass().remove("selected-cell");
                cellView.getStyleClass().remove("selected-range-cell");
            }
        }
        highlightedCells.clear();
    }

    public void addHighlightedCell(String cellId) {
        highlightedCells.add(cellId);
    }

    public void highlightSelectedRange(String rangeName) {
        clearHighlightedCells();

        List<Coordinate> selectedRange = shticellController.getRangeByName(rangeName);

        for (Coordinate coordinate : selectedRange) {
            Node cellView = findCellViewById(coordinate.toString());

            if (cellView != null) {
                cellView.getStyleClass().add("selected-range-cell");
                highlightedCells.add(coordinate.toString());
            }
        }
    }

    public void setSkin(String name) {
        switch (name) {
            case "Default":
                gridPane.setStyle(" -fx-background-color: #e4f1db;");
                break;
            case "Thunder Cats":
                gridPane.setStyle(" -fx-background-color: #ffa1a1;");
                break;
            case "India":
                gridPane.setStyle(" -fx-background-color: #ffcc99;");
                break;
        }
    }
}

