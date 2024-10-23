package util.requestservice;

import dto.dtoPackage.CellDTO;
import dto.dtoPackage.SpreadsheetDTO;
import engine.sheetimpl.cellimpl.coordinate.Coordinate;

import java.util.List;

public interface ShticellRequestService {

    List<CellDTO> updateCell(String cellId, String newValue) throws Exception;

    SpreadsheetDTO getSpreadsheetState() throws Exception;

    SpreadsheetDTO getSpreadsheetByVersion(int version) throws Exception;

    List<Coordinate> getDependents(String cellId) throws Exception;

    List<Coordinate> getReferences(String cellId) throws Exception;

    SpreadsheetDTO sort(String cellsRange, List<Character> selectedColumns) throws Exception;

    List<String> getUniqueValuesFromColumn(char column) throws Exception;

    SpreadsheetDTO filterSheet(Character selectedColumn, String filterArea, List<String> selectedValues) throws Exception;

    void setSingleCellBackGroundColor(String cellId, String hexString);

    void setSingleCellTextColor(String cellId, String hexString);

    void removeRangeFromSheet(String selectedRange);

    void addRangeToSheet(String rangeName, String coordinates);

    List<Coordinate> getRangeByName(String rangeName);
}