package sheetimpl;

import api.Cell;
import api.EffectiveValue;
import api.Spreadsheet;
import sheetimpl.cellimpl.CellImpl;
import sheetimpl.cellimpl.coordinate.Coordinate;

import java.util.*;

import static converter.SheetConverter.convertSheetToDTO;
import static sheetimpl.utils.ExpressionUtils.buildExpressionFromString;

public class SpreadsheetImpl implements Spreadsheet {
    private String sheetName;
    private int sheetVersion;
    private Map<Coordinate, Cell> activeCells;
    private Map<Coordinate, List<Coordinate>> dependencies;
    private int rows;
    private int columns;
    private int rowHeightUnits;
    private int columnWidthUnits;

    public SpreadsheetImpl() {
        this.activeCells = new HashMap<>();
    }

    public SpreadsheetImpl(String name, int rows, int columns, int rowsHeightUnits,
                           int columnWidthUnits, HashMap<Coordinate, Cell> activeCells,
                           HashMap<Coordinate, List<Coordinate>> dependencies) {
        this.sheetName = name;
        this.sheetVersion = 0;
        this.activeCells = activeCells;
        this.dependencies = dependencies;
        this.rows = rows;
        this.columns = columns;
        this.rowHeightUnits = rowsHeightUnits;
        this.columnWidthUnits = columnWidthUnits;
    }

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

        validateCoordinateInbound(coordinate , rows, columns);
        return activeCells.computeIfAbsent(coordinate,key->new CellImpl() {
        });
    }

    public static void validateCoordinateInbound(Coordinate coordinate, int rowsLimit, int columnsLimit){

        if (coordinate.row() < 1 || coordinate.row() > rowsLimit || coordinate.column() < 1 || coordinate.column() > columnsLimit) {
            throw new IllegalArgumentException("Cell at position (" + coordinate.row() + ", " + coordinate.column() +
                    ") is outside the sheet boundaries: max rows = " + rowsLimit +
                    ", max columns = " + columnsLimit);
        }
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
    public int getColumns() {
       return this.columns;
    }

    @Override
    public void setTitle(String sheetName) {
        this.sheetName = sheetName;
    }

    @Override
    public void setCell(Coordinate coordinate , String value) {
        Cell cell = getCell(coordinate);
        EffectiveValue effectiveValue = buildExpressionFromString(value).evaluate(convertSheetToDTO(this));
        cell.setCellOriginalValue(value);
        cell.setEffectiveValue(buildExpressionFromString(value).evaluate(convertSheetToDTO(this)));
        cell.setLastModifiedVersion(sheetVersion);
        cell = activeCells.computeIfAbsent(coordinate, c -> new CellImpl(value, effectiveValue, sheetVersion));
    }

    @Override
    public void setRows(int rows) {
        this.rows = rows;
    }

    @Override
    public void setColumns(int columns) {
        this.columns = columns;
    }
    @Override
    public int getRowHeightUnits() {
        return rowHeightUnits;
    }
    @Override
    public void setRowHeightUnits(int rowHeightUnits) {
        this.rowHeightUnits = rowHeightUnits;
    }
    @Override
    public int getColumnWidthUnits() {
        return columnWidthUnits;
    }
    @Override
    public void setColumnWidthUnits(int columnWidthUnits) {
        this.columnWidthUnits = columnWidthUnits;
    }

    @Override
    public void clearSpreadSheet() {
        activeCells.clear();
        dependencies.clear();
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
            throw new IllegalStateException("The graph has at least one cycle. Topological sorting is not possible.");
        }

        return sortedList;
    }
}
