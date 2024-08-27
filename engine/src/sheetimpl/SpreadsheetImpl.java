package sheetimpl;

import api.*;
import generated.STLCell;
import generated.STLSheet;
import sheetimpl.cellimpl.CellImpl;
import sheetimpl.cellimpl.EmptyCell;
import sheetimpl.cellimpl.coordinate.CoordinateFactory;
import sheetimpl.utils.CellType;

import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static sheetimpl.cellimpl.coordinate.CoordinateFactory.*;
import static sheetimpl.utils.ExpressionUtils.buildExpressionFromString;

public class SpreadsheetImpl implements Spreadsheet , Serializable {
    private String sheetName;
    private int sheetVersion;
    private Map<Coordinate, Cell> activeCells;
    private Map<Coordinate, List<Coordinate>> dependenciesAdjacencyList;
    private Map<Coordinate, List<Coordinate>> referencesAdjacencyList;
    private int rows;
    private int columns;
    private int rowHeightUnits;
    private int columnWidthUnits;
    private List<Cell> cellsThatHaveChanged;



    public SpreadsheetImpl()  {
        activeCells = new HashMap<>();
        dependenciesAdjacencyList = new HashMap<>();
        referencesAdjacencyList = new HashMap<>();
        cellsThatHaveChanged = new ArrayList<>();
        sheetVersion = 1;
    }

    @Override
    public Spreadsheet copySheet() {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(this);
            oos.close();

            ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(baos.toByteArray()));
            return (SpreadsheetImpl) ois.readObject();
        } catch (Exception e) {
            // deal with the runtime error that was discovered as part of invocation
            return this;
        }
    }

    private void validateCoordinateInbound(Coordinate coordinate) {
        if (coordinate.row() < 1 || coordinate.row() > rows || coordinate.column() < 1 || coordinate.column() > columns) {
            throw new IllegalArgumentException("Cell at position " + coordinate +
                    " is outside the sheet boundaries: max row = " + rows +
                    ", max column = " + convertColumnNumberToLetter(columns));
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
            Coordinate coordinate = CoordinateFactory.createCoordinate(stlCell.getRow(), convertColumnLetterToNumber(stlCell.getColumn().toUpperCase()));
            Cell cell = CreateNewEmptyCell(coordinate);
            cell.setCellOriginalValue(originalValue);
            activeCells.put(coordinate, cell);
        }

        calculateSheetEffectiveValues();
    }

    private void calculateSheetEffectiveValues() {
        Map<Coordinate, List<Coordinate>> dependencyGraph = buildGraphFromSheet();
        List<Coordinate> calculationOrder = topologicalSort(dependencyGraph);

        for (Coordinate coordinate : calculationOrder) {
            if (getCell(coordinate) != EmptyCell.INSTANCE) {
                Cell cell = CreateNewEmptyCell(coordinate);
                calculateCellEffectiveValue(cell);
            }
        }
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
        Pattern pattern = Pattern.compile("\\{REF,([A-Z]+\\d+)\\}", Pattern.CASE_INSENSITIVE);
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

    private void calculateCellEffectiveValue(Cell cellToCalculate) {
        String originalValue = cellToCalculate.getOriginalValue();
        EffectiveValue previousEffectiveValue = cellToCalculate.getEffectiveValue();
        EffectiveValue effectiveValue = buildExpressionFromString(originalValue).evaluate(this);

        if(!(effectiveValue.equals(previousEffectiveValue))) {
            cellToCalculate.setEffectiveValue(effectiveValue);
            cellToCalculate.setLastModifiedVersion(sheetVersion);
            cellsThatHaveChanged.add(cellToCalculate);
        }

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

        Cell cell = activeCells.get(coordinate);
        if (cell == null) {
            activeCells.put(coordinate,EmptyCell.INSTANCE);
            return EmptyCell.INSTANCE;
        }
        return cell;
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

    @Override
    public int getNumberOfModifiedCells()
    {
        return cellsThatHaveChanged.size();
    }

    //  Setters:
    @Override
    public void setCell(Coordinate coordinate, String value) {
        if (value.isEmpty()) {
            activeCells.remove(coordinate);
        }else {
            Cell cellToCalculate = CreateNewEmptyCell(coordinate);
            try {
                cellToCalculate.setCellOriginalValue(value);
                cellsThatHaveChanged.clear();
                calculateSheetEffectiveValues();
                cellToCalculate.setLastModifiedVersion(sheetVersion);
            } catch (Exception e) {
                throw e;
            }
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
    public void setRowHeightUnits(int rowHeightUnits) {
        if (rowHeightUnits < 1) {
            throw new IllegalArgumentException("Row height units must be at least 1, but was " + rowHeightUnits);
        }
        this.rowHeightUnits = rowHeightUnits;
    }

    @Override
    public void setColumnWidthUnits(int columnWidthUnits) {
        if (columnWidthUnits < 1) {
            throw new IllegalArgumentException("Column width units must be at least 1, but was " + columnWidthUnits);
        }
        this.columnWidthUnits = columnWidthUnits;
    }

    @Override
    public List<Cell> getCellsThatHaveChanged() {return cellsThatHaveChanged;}

    @Override
    public void setSheetVersion(int sheetVersion) {
        this.sheetVersion = sheetVersion;
    }

    //INSIDE
    private Cell CreateNewEmptyCell (Coordinate coordinate) {
        Cell newCell = getCell(coordinate);

        if(newCell instanceof EmptyCell){
            Cell cell = new CellImpl();
            activeCells.put(coordinate, cell);
            return cell;
        }

        return  newCell;
    }
}

