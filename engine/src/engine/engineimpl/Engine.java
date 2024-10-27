package engine.engineimpl;

import dto.dtoPackage.CellDTO;
import dto.dtoPackage.PermissionInfoDTO;
import dto.dtoPackage.SheetInfoDTO;
import dto.dtoPackage.SpreadsheetDTO;
import engine.permissionmanager.PermissionType;
import engine.permissionmanager.request.RequestStatus;
import engine.sheetimpl.cellimpl.coordinate.Coordinate;
import engine.sheetmanager.SheetManager;

import java.io.InputStream;
import java.util.List;

public interface Engine {
    void addSheet(InputStream fileContent , String userName);
    SheetManager getSheet(String sheetName);
    List<SheetInfoDTO> getSheets(String user);
    void requestPermission(String userName, PermissionType permissionType, String sheetName);
    List<PermissionInfoDTO> getPermissions(String sheetName);
    void updatePermissions(String usernameFromSession, String sheetName, int requestId, RequestStatus requestStatus);

    SpreadsheetDTO getExpectedValue(String username, Coordinate coordinate, String value, String sheetName);
    void addRangeToSheet(String rangeName, String coordinates, String sheetName , String userName);

    void setSingleCellBackGroundColor(String cellId, String color, String sheetName, String userName);

    SpreadsheetDTO getSpreadsheetState(String cellId, String sheetName, String userName);

    SpreadsheetDTO filterSheet(Character selectedColumn, String filterArea, List<String> selectedValues, String sheetName, String userName);

    List<Coordinate> getRangeByName(String range, String sheetName, String userName);

    List<String> getUniqueValuesFromColumn(char column, String userName, String sheetName);

    void removeRangeFromSheet(String selectedRange, String userName, String sheetName);

    SpreadsheetDTO sort(String cellsRange, List<Character> selectedColumns, String userName, String sheetName);

    void setSingleCellTextColor(String cellId, String color, String userName, String sheetName);

    List<CellDTO> updateCell(String cellId, String newValue, String userName, String sheetName);

    SpreadsheetDTO getSpreadsheetByVersion(int version, String userName, String sheetName);

    SpreadsheetDTO getLatestVersion(String sheetName, String userName);

    int getLatestVersionNumber(String sheetName, String userName);

}