package sheetimpl;

import api.Cell;
import api.EffectiveValue;
import api.Spreadsheet;
import generated.STLCell;
import generated.STLSheet;
import sheetimpl.cellimpl.CellImpl;
import sheetimpl.cellimpl.coordinate.Coordinate;
import sheetimpl.cellimpl.coordinate.CoordinateFactory;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static converter.SheetConverter.convertSheetToDTO;
import static sheetimpl.cellimpl.coordinate.CoordinateFactory.convertColumnLetterToNumber;
import static sheetimpl.cellimpl.coordinate.CoordinateFactory.createCoordinate;
import static sheetimpl.utils.ExpressionUtils.buildExpressionFromString;

public class SpreadsheetImpl implements Spreadsheet {
    private String sheetName;
    private int sheetVersion; // necessary ?
    private Map<Coordinate, Cell> activeCells;
    private Map<Coordinate, List<Coordinate>> dependenciesAdjacencyList;
    private Map<Coordinate, List<Coordinate>> referencesAdjacencyList;
    private int rows;
    private int columns;
    private int rowHeightUnits;
    private int columnWidthUnits;
    private List<Coordinate> cellsThatHaveChanged;

    public SpreadsheetImpl() {
        activeCells = new HashMap<>();
        dependenciesAdjacencyList = new HashMap<>();
        referencesAdjacencyList = new HashMap<>();
        cellsThatHaveChanged = new ArrayList<>();
    }

    private void validateCoordinateInbound(Coordinate coordinate) {
        if (coordinate.row() < 1 || coordinate.row() > rows || coordinate.column() < 1 || coordinate.column() > columns) {
            throw new IllegalArgumentException("Cell at position (" + coordinate.row() + ", " + coordinate.column() +
                    ") is outside the sheet boundaries: max rows = " + rows +
                    ", max columns = " + columns);
        }
    }

    @Override
    public void init(STLSheet loadedSheetFromXML) {
        this.setTitle(loadedSheetFromXML.getName());
        this.setRows(loadedSheetFromXML.getSTLLayout().getRows());
        this.setColumns(loadedSheetFromXML.getSTLLayout().getColumns());
        this.setRowHeightUnits(loadedSheetFromXML.getSTLLayout().getSTLSize().getRowsHeightUnits());
        this.setColumnWidthUnits(loadedSheetFromXML.getSTLLayout().getSTLSize().getColumnWidthUnits());

        for (STLCell stlCell : loadedSheetFromXML.getSTLCells().getSTLCell()) {
            String originalValue = stlCell.getSTLOriginalValue();
            Coordinate coordinate = CoordinateFactory.createCoordinate(stlCell.getRow(), convertColumnLetterToNumber(stlCell.getColumn()));
            Cell cell = getCell(coordinate);
            cell.setCellOriginalValue(originalValue);
            activeCells.put(coordinate, cell);
        }

        calculateSheetEffectiveValues();
    }

    private void calculateSheetEffectiveValues() {
        Map<Coordinate, List<Coordinate>> dependencyGraph = buildGraphFromSheet();
        List<Coordinate> calculationOrder = topologicalSort(dependencyGraph);
        List<Coordinate> cellsThatHaveChangedLocal = new ArrayList<>();

        for (Coordinate coordinate : calculationOrder) {
            Cell cell = getCell(coordinate);
            if(calculateCellEffectiveValue(cell))
            {
                cellsThatHaveChangedLocal.add(coordinate);
            }
        }

        cellsThatHaveChanged =cellsThatHaveChangedLocal;
        dependenciesAdjacencyList = dependencyGraph;
        referencesAdjacencyList = getTransposedGraph();
    }

    private Map<Coordinate, List<Coordinate>> buildGraphFromSheet() {
        Map<Coordinate, List<Coordinate>> dependencyGraph = new HashMap<>();

        for (Map.Entry<Coordinate, Cell> entry : activeCells.entrySet()) {
            Coordinate cellCoordinate = entry.getKey();
            Cell cell = entry.getValue();
            if (!dependencyGraph.containsKey(cellCoordinate)) {
                dependencyGraph.put(cellCoordinate, new LinkedList<>());
            }
            List<Coordinate> coordinateList = extractRefCoordinates(cell.getOriginalValue());

            for (Coordinate coordinate : coordinateList) {
                if (!dependencyGraph.containsKey(coordinate)) {
                    dependencyGraph.put(coordinate, new LinkedList<>());
                }

                List<Coordinate> neighborsList = dependencyGraph.get(coordinate);
                neighborsList.add(cellCoordinate);
            }
        }

        return dependencyGraph;
    }

    private List<Coordinate> extractRefCoordinates(String expression) {
        List<Coordinate> coordinates = new ArrayList<>();

        // Regular expression to match patterns like {REF,A4}
        Pattern pattern = Pattern.compile("\\{REF,([A-Z]+\\d+)\\}");
        Matcher matcher = pattern.matcher(expression);

        // Find all matches in the expression
        while (matcher.find()) {
            String cellId = matcher.group(1);
            // Convert the coordinate string to a Coordinate object
            Coordinate coordinate = createCoordinate(cellId);
            coordinates.add(coordinate);
        }

        return coordinates;
    }

    private List<Coordinate> topologicalSort(Map<Coordinate, List<Coordinate>> dependencyGraph) {
        List<Coordinate> sortedList = new ArrayList<>();
        Map<Coordinate, Integer> inDegree = new HashMap<>();
        Queue<Coordinate> queue = new LinkedList<>();

        // Step 1: Calculate in-degree for each node
        for (Coordinate node : dependencyGraph.keySet()) {
            inDegree.putIfAbsent(node, 0); // Ensure all nodes are in the in-degree map

            for (Coordinate dependent : dependencyGraph.get(node)) {
                inDegree.put(dependent, inDegree.getOrDefault(dependent, 0) + 1);
            }
        }

        // Step 2: Add all nodes with in-degree 0 to the queue
        for (Map.Entry<Coordinate, Integer> entry : inDegree.entrySet()) {
            if (entry.getValue() == 0) {
                queue.add(entry.getKey());
            }
        }

        // Step 3: Process nodes in queue
        while (!queue.isEmpty()) {
            Coordinate node = queue.poll();
            sortedList.add(node);

            if (dependencyGraph.containsKey(node)) {
                for (Coordinate dependent : dependencyGraph.get(node)) {
                    inDegree.put(dependent, inDegree.get(dependent) - 1);

                    if (inDegree.get(dependent) == 0) {
                        queue.add(dependent);
                    }
                }
            }
        }

        // Step 4: Check for cycles (i.e., if sortedList doesn't contain all nodes)
        if (sortedList.size() != inDegree.size()) {
            throw new IllegalStateException("This update may result circular dependency, " +
                    "thus, can`t use this value to update the cell.");
        }

        return sortedList;
    }

    private boolean calculateCellEffectiveValue(Cell cellToCalculate) {
        String originalValue = cellToCalculate.getOriginalValue();
        EffectiveValue previousEffectiveValue = cellToCalculate.getEffectiveValue();
        EffectiveValue effectiveValue = buildExpressionFromString(originalValue).evaluate(convertSheetToDTO(this));
        if(effectiveValue.equals(previousEffectiveValue)) {return false;}
        cellToCalculate.setEffectiveValue(effectiveValue);

        return true;
    }

    private Map<Coordinate, List<Coordinate>> getTransposedGraph() {
        Map<Coordinate, List<Coordinate>> transposedGraph = new HashMap<>();

        for (Map.Entry<Coordinate, List<Coordinate>> entry : dependenciesAdjacencyList.entrySet()) {
            Coordinate source = entry.getKey();
            List<Coordinate> targets = entry.getValue();

            for (Coordinate target : targets) {
                if (!transposedGraph.containsKey(target)) {
                    transposedGraph.put(target, new LinkedList<>());
                }
                transposedGraph.get(target).add(source);
            }
        }

        return transposedGraph;
    }

    // Getters:
    @Override
    public String getSheetName() {
        return this.sheetName;
    }

    @Override
    public int getVersion() {
        return sheetVersion;
    }

    @Override
    public Cell getCell(Coordinate coordinate) {
        validateCoordinateInbound(coordinate);
        return activeCells.computeIfAbsent(coordinate, key -> new CellImpl() {
        });
    }

    @Override
    public Map<Coordinate, Cell> getActiveCells() {
        return activeCells;
    }

    @Override
    public int getRows() {
        return this.rows;
    }

    @Override
    public int getColumns() {return this.columns;}

    @Override
    public int getRowHeightUnits() {return rowHeightUnits;}

    @Override
    public int getColumnWidthUnits() {return columnWidthUnits;}

    @Override
    public Map<Coordinate, List<Coordinate>> getReferencesAdjacencyList() {return referencesAdjacencyList;}

    @Override
    public int getSheetVersion() {return sheetVersion;}

    @Override
    public Map<Coordinate, List<Coordinate>> getDependenciesAdjacencyList() {return dependenciesAdjacencyList;}

    //  Setters:
    @Override
    public void setCell(Coordinate coordinate, String value) {
        Cell cellToCalculate = getCell(coordinate);

        String originalValueBackup = cellToCalculate.getOriginalValue();
        EffectiveValue effectiveValueBackup = cellToCalculate.getEffectiveValue();

        try {
            cellToCalculate.setCellOriginalValue(value);
            calculateSheetEffectiveValues();
            cellToCalculate.setLastModifiedVersion(sheetVersion);
        } catch (Exception e) {
            cellToCalculate.setCellOriginalValue(originalValueBackup);
            cellToCalculate.setEffectiveValue(effectiveValueBackup);

            throw e;
        }
    }

    @Override
    public void setTitle(String sheetName) {
        this.sheetName = sheetName;
    }

    @Override
    public void setRows(int rows) {this.rows = rows;}

    @Override
    public void setColumns(int columns) {this.columns = columns;}

    @Override
    public void setRowHeightUnits(int rowHeightUnits) {this.rowHeightUnits = rowHeightUnits;}

    @Override
    public void setColumnWidthUnits(int columnWidthUnits) {this.columnWidthUnits = columnWidthUnits;}

    @Override
    public List<Coordinate> getCellsThatHaveChanged() {return cellsThatHaveChanged;}

    public void setSheetVersion(int sheetVersion) {
        this.sheetVersion = sheetVersion;
    }
}

