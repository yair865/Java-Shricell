package engine.sheetimpl;

import dto.dtoPackage.SpreadsheetDTO;
import engine.api.*;
import engine.generated.STLCell;
import engine.generated.STLRange;
import engine.generated.STLSheet;
import engine.sheetimpl.cellimpl.CellImpl;
import engine.sheetimpl.cellimpl.EmptyCell;
import engine.sheetimpl.cellimpl.coordinate.CoordinateFactory;
import engine.sheetimpl.range.RangeImpl;
import engine.sheetimpl.sortfilter.SortManager;
import engine.sheetimpl.sortfilter.SortManagerImpl;
import engine.sheetimpl.utils.ExpressionUtils;

import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static engine.sheetimpl.cellimpl.coordinate.CoordinateFactory.createCoordinate;

public class SpreadsheetImpl implements Spreadsheet, Serializable {
    private String sheetName;
    private int sheetVersion;
    private Map<Coordinate, Cell> activeCells;
    private Map<Coordinate, List<Coordinate>> dependenciesAdjacencyList;
    private Map<Coordinate, List<Coordinate>> referencesAdjacencyList;
    private int rows;
    private int columns;
    private int rowHeightUnits;
    private int columnWidthUnits;
    private List<Coordinate> cellsThatHaveChanged;
    private Map<String, Range> ranges;
    private SortManager sortManager;


    public SpreadsheetImpl() {
        activeCells = new HashMap<>();
        dependenciesAdjacencyList = new HashMap<>();
        referencesAdjacencyList = new HashMap<>();
        cellsThatHaveChanged = new ArrayList<>();
        ranges = new HashMap<>();
        sortManager = new SortManagerImpl();
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
            return this;
        }
    }

    private void validateCoordinateInbound(Coordinate coordinate) {
        if (coordinate.row() < 1 || coordinate.row() > rows || coordinate.column() < 1 || coordinate.column() > columns) {
            throw new IllegalArgumentException("Cell at position " + coordinate +
                    " is outside the sheet boundaries: max row = " + rows +
                    ", max column = " + CoordinateFactory.convertColumnNumberToLetter(columns));
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
            Coordinate coordinate = createCoordinate(stlCell.getRow(), CoordinateFactory.convertColumnLetterToNumber(stlCell.getColumn().toUpperCase()));
            Cell cell = createNewEmptyCell(coordinate);
            cell.setCellOriginalValue(originalValue);
            activeCells.put(coordinate, cell);
        }

        for (STLRange stlRange : loadedSheetFromXML.getSTLRanges().getSTLRange()) {
            String from = stlRange.getSTLBoundaries().getFrom();
            String to = stlRange.getSTLBoundaries().getTo();
            String rangeName = stlRange.getName();
            Coordinate start = CoordinateFactory.createCoordinate(from);
            Coordinate end = CoordinateFactory.createCoordinate(to);
            addRangeHelper(rangeName, start, end);
        }

        calculateSheetEffectiveValues();
    }

    private void calculateSheetEffectiveValues() {
        Map<Coordinate, List<Coordinate>> dependencyGraph = buildGraphFromSheet();
        List<Coordinate> calculationOrder = topologicalSort(dependencyGraph);

        for (Coordinate coordinate : calculationOrder) {
            if (getCell(coordinate) != EmptyCell.INSTANCE) {
                Cell cell = createNewEmptyCell(coordinate);
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
            List<Coordinate> coordinateList = extractDependencies(cell.getOriginalValue());

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

        Pattern pattern = Pattern.compile("\\{REF,([A-Z]+\\d+)\\}", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(expression);

        while (matcher.find()) {
            String cellId = matcher.group(1);
            Coordinate coordinate = createCoordinate(cellId);
            coordinates.add(coordinate);
        }

        return coordinates;
    }

    private List<Coordinate> topologicalSort(Map<Coordinate, List<Coordinate>> dependencyGraph) {
        List<Coordinate> sortedList = new ArrayList<>();
        Map<Coordinate, Integer> inDegree = new HashMap<>();
        Queue<Coordinate> queue = new LinkedList<>();

        for (Coordinate node : dependencyGraph.keySet()) {
            inDegree.putIfAbsent(node, 0);

            for (Coordinate dependent : dependencyGraph.get(node)) {
                inDegree.put(dependent, inDegree.getOrDefault(dependent, 0) + 1);
            }
        }

        for (Map.Entry<Coordinate, Integer> entry : inDegree.entrySet()) {
            if (entry.getValue() == 0) {
                queue.add(entry.getKey());
            }
        }

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

        if (sortedList.size() != inDegree.size()) {
            throw new IllegalStateException("This update may result circular dependency, " +
                    "thus, can`t use this value to update the cell.");
        }

        return sortedList;
    }

    private void calculateCellEffectiveValue(Cell cellToCalculate) {
        String originalValue = cellToCalculate.getOriginalValue();
        EffectiveValue previousEffectiveValue = cellToCalculate.getEffectiveValue();
        EffectiveValue effectiveValue = ExpressionUtils.buildExpressionFromString(originalValue).evaluate(this);


        if (!(effectiveValue.equals(previousEffectiveValue))) {
            cellToCalculate.setEffectiveValue(effectiveValue);
            cellToCalculate.setLastModifiedVersion(sheetVersion);
            cellsThatHaveChanged.add(cellToCalculate.getCoordinate());
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
            activeCells.put(coordinate, EmptyCell.INSTANCE);
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
    public int getColumns() {
        return this.columns;
    }

    @Override
    public int getRowHeightUnits() {
        return rowHeightUnits;
    }

    @Override
    public int getColumnWidthUnits() {
        return columnWidthUnits;
    }

    @Override
    public Map<Coordinate, List<Coordinate>> getReferencesAdjacencyList() {
        return referencesAdjacencyList;
    }

    @Override
    public int getSheetVersion() {
        return sheetVersion;
    }

    @Override
    public Map<Coordinate, List<Coordinate>> getDependenciesAdjacencyList() {
        return dependenciesAdjacencyList;
    }

    @Override
    public int getNumberOfModifiedCells() {
        return cellsThatHaveChanged.size();
    }

    //  Setters:
    @Override
    public void setCell(Coordinate coordinate, String value) {
        Cell cellToCalculate = createNewEmptyCell(coordinate);

        cellToCalculate.setCellOriginalValue(value);
        cellsThatHaveChanged.clear();
        calculateSheetEffectiveValues();
        cellToCalculate.setLastModifiedVersion(sheetVersion);
    }

    @Override
    public void setTitle(String sheetName) {
        this.sheetName = sheetName;
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
    public List<Coordinate> getCellsThatHaveChanged() {
        return cellsThatHaveChanged;
    }

    @Override
    public Map<String, Range> getRanges() {
        return ranges;
    }

    @Override
    public void setSheetVersion(int sheetVersion) {
        this.sheetVersion = sheetVersion;
    }

    @Override
    public void setBackgroundColor(String cellId, String backGroundColor) {
        Coordinate coordinate = createCoordinate(cellId);
        Cell cell = this.createNewEmptyCell(coordinate);
        cell.setBackgroundColor(backGroundColor);
    }

    @Override
    public void setTextColor(String cellId, String textColor) {
        Coordinate coordinate = createCoordinate(cellId);
        Cell cell = this.createNewEmptyCell(coordinate);
        cell.setTextColor(textColor);
    }

    //INSIDE
    private Cell createNewEmptyCell(Coordinate coordinate) {
        Cell newCell = getCell(coordinate);

        if (newCell instanceof EmptyCell) {
            Cell cell = new CellImpl(coordinate);
            activeCells.put(coordinate, cell);
            return cell;
        }

        return newCell;
    }

    @Override
    public void addRange(String name, String rangeDefinition) {
        List<Coordinate> startToEnd = ExpressionUtils.parseRange(rangeDefinition);
        addRangeHelper(name, startToEnd.getFirst(), startToEnd.get(1));
    }

    @Override
    public Range getRangeByName(String name) {

        if (ranges.get(name) != null) {
            return ranges.get(name);
        } else {
            throw new IllegalArgumentException("Range " + name + " not found");
        }
    }

    @Override
    public boolean rangeExists(String rangeName) {
        return ranges.containsKey(rangeName);
    }

    @Override
    public void sortSheet(String cellsRange, List<Character> selectedColumns) {
    List<Coordinate> rangesToSort = ExpressionUtils.parseRange(cellsRange);
    sortManager.sortRowsByColumns(rangesToSort , selectedColumns , this);
    }

    @Override
    public void removeRange(String name) {
        if (!ranges.containsKey(name)) {
            throw new IllegalArgumentException("The range '" + name + "' does not exist.");
        }

        List<Coordinate> usingCells = new ArrayList<>();
        for (Map.Entry<Coordinate, Cell> entry : activeCells.entrySet()) {
            Cell cell = entry.getValue();
            if (cell.getOriginalValue().contains("{SUM," + name + "}") || cell.getOriginalValue().contains("{AVG," + name + "}")) {
                usingCells.add(entry.getKey());
            }
        }

        if (!usingCells.isEmpty()) {
            String cellReferences = usingCells.stream()
                    .map(Coordinate::toString)
                    .collect(Collectors.joining(", "));
            throw new IllegalStateException("Cannot delete the range '" + name + "' because it is in use by cells: " + cellReferences);
        }

        ranges.remove(name);
    }

    private void addRangeHelper(String name, Coordinate start, Coordinate end) {
        validateCoordinateInbound(start);
        validateCoordinateInbound(end);
        if (!ranges.containsKey(name)) {
            ranges.put(name, new RangeImpl(start, end));
        } else {
            throw new IllegalArgumentException("Range '" + name + "' already exists");
        }
    }

    private List<Coordinate> extractRangeFunction(String expression) {
        List<Coordinate> rangeCoordinates = new ArrayList<>();
        if (expression.startsWith("{SUM") || expression.startsWith("{AVG")) {
            String rangeName = extractRangeNameFromBrackets(expression);

            rangeCoordinates = this.getRangeByName(rangeName).getCoordinates();
        }

        return rangeCoordinates;
    }

    private String extractRangeNameFromBrackets(String expression) {
        int start = expression.indexOf(",") + 1;
        int end = expression.indexOf("}");
        return expression.substring(start, end).trim();
    }

    private List<Coordinate> extractDependencies(String expression) {
        List<Coordinate> rangeList = extractRangeFunction(expression);
        List<Coordinate> refList = extractRefCoordinates(expression);

        return Stream.of(rangeList, refList)
                .filter(Objects::nonNull)
                .flatMap(List::stream)
                .distinct()
                .collect(Collectors.toList());
    }
}

