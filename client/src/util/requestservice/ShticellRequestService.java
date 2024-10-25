package util.requestservice;

import dto.dtoPackage.CellDTO;
import dto.dtoPackage.SpreadsheetDTO;
import engine.sheetimpl.cellimpl.coordinate.Coordinate;

import java.util.List;
import java.util.function.Consumer;

public interface ShticellRequestService {

    void updateCell(String cellId, String newValue, Consumer<List<CellDTO>> onSuccess);

    void getSpreadSheetByVersion(int version, Consumer<SpreadsheetDTO> onSuccess);

    void getDependents(String cellId, Consumer<List<Coordinate>> onSuccess);

    void getReferences(String cellId, Consumer<List<Coordinate>> onSuccess);

    void sort(String cellsRange, List<Character> selectedColumns, Consumer<SpreadsheetDTO> sortedSheetConsumer);

    void getEffectiveValuesForColumn(char column, Consumer<List<String>> valuesConsumer);

    void filterSheet(Character selectedColumn, String filterArea, List<String> selectedValues, Consumer<SpreadsheetDTO> filteredSheetConsumer);

    void setSingleCellBackGroundColor(String cellId, String hexString, Runnable task);

    void setSingleCellTextColor(String cellId, String hexString, Runnable task);

    void removeRangeFromSheet(String selectedRange, Consumer<String> callback);

    void addRangeToSheet(String rangeName, String coordinates , Consumer<String> callback);

    void getRangeByName(String rangeName , Consumer<List<Coordinate>> onSuccess);

}