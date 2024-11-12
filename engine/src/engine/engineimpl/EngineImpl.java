package engine.engineimpl;

import dto.converter.SheetInfoConverter;
import dto.dtoPackage.CellDTO;
import dto.dtoPackage.PermissionInfoDTO;
import dto.dtoPackage.SheetInfoDTO;
import dto.dtoPackage.SpreadsheetDTO;
import engine.exception.OutdatedVersionException;
import engine.permissionmanager.PermissionManager;
import engine.permissionmanager.PermissionManagerImpl;
import dto.dtoPackage.PermissionType;
import dto.dtoPackage.RequestStatus;
import dto.dtoPackage.coordinate.Coordinate;
import engine.sheetmanager.SheetManager;
import engine.sheetmanager.SheetManagerImpl;

import java.io.InputStream;
import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class EngineImpl implements Engine, Serializable {

    private static final EngineImpl instance = new EngineImpl();
    private final Map<String, SheetManager> sheets;
    private PermissionManager permissionManager;

    public EngineImpl() {
        this.sheets = new ConcurrentHashMap<>();
        this.permissionManager = new PermissionManagerImpl();
    }

    @Override
    public void addSheet(InputStream fileContent , String userName) {
        String sheetName = null;
        SheetManager sheetManager = new SheetManagerImpl(userName);

             sheetName = sheetManager.loadSpreadsheet(fileContent);
             validateSheetAlreadyExists(sheetName);

        sheets.put(sheetName, sheetManager);
        permissionManager.assignPermission(sheetName, userName, PermissionType.OWNER);
    }

    private void validateSheetAlreadyExists(String sheetName) {
        if (sheets.containsKey(sheetName)) {
            throw new IllegalArgumentException("Sheet with name '" + sheetName + "' already exists.");
        }
    }

    private void ValidateSheetName(String sheetName) {
        if (!sheets.containsKey(sheetName)) {
            throw new IllegalArgumentException("Sheet with name '" + sheetName + "' doesnt exists.");
        }
    }

    @Override
    public synchronized SheetManager getSheet(String sheetName) {
        if (!sheets.containsKey(sheetName)) {
            throw new IllegalArgumentException("No sheet found with name '" + sheetName + "'.");
        }

        return sheets.get(sheetName);
    }

    @Override
    public synchronized List<SheetInfoDTO> getSheets(String user) {
        System.out.println("Current sheets size: " + sheets.size());
        sheets.forEach((key, value) -> System.out.println("Sheet Key: " + key + ", Value: " + value));

        List<SheetInfoDTO> sheetInfoDTOs = sheets.values().stream()
                .map(sheet -> {
                    String sheetName = sheet.getSheetTitle();
                    PermissionType permissionType = permissionManager.getPermission(sheetName, user);
                    return SheetInfoConverter.convertSheetsInformationToDTO(sheet, permissionType);
                })
                .collect(Collectors.toList());

        System.out.println("Sheets list size: " + sheetInfoDTOs.size());

        return sheetInfoDTOs;
    }

    @Override
    public synchronized void requestPermission(String userName, PermissionType permissionType, String sheetName) {
        ValidateSheetName(sheetName);
        permissionManager.createRequest(userName , permissionType , sheetName);
    }

    @Override
    public List<PermissionInfoDTO> getPermissions(String sheetName) {
       return permissionManager.getAllPermissionsForSheet(sheetName);
    }

    @Override
    public void updatePermissions(String usernameFromSession, String sheetName, int requestId, RequestStatus requestStatus) {
        permissionManager.updatePermissions(usernameFromSession , sheetName ,requestId ,requestStatus);
    }

    @Override
    public synchronized SpreadsheetDTO getExpectedValue(String username, Coordinate coordinate, String value, String sheetName) {
        permissionManager.validateReaderPermission(username , sheetName);

        return sheets.get(sheetName).getExpectedValue(coordinate, value);
    }

    @Override
    public void addRangeToSheet(String rangeName, String coordinates, String sheetName ,String username, int clientVersion) {
        permissionManager.validateWriterPermission(username , sheetName);
        SheetManager sheetManager = sheets.get(sheetName);
        validateUpdatedSheet(sheetManager,clientVersion);
        sheetManager.addRangeToSheet(rangeName, coordinates);
    }

    @Override
    public void setSingleCellBackGroundColor(String cellId, String color, String sheetName, String userName,int clientVersion) {
        permissionManager.validateWriterPermission(userName , sheetName);
        SheetManager sheetManager = sheets.get(sheetName);
        validateUpdatedSheet(sheetManager,clientVersion);
        sheetManager.setSingleCellBackGroundColor(cellId, color);
    }

    @Override
    public SpreadsheetDTO getSpreadsheetState(String cellId, String sheetName, String userName) {
        permissionManager.validateReaderPermission(userName , sheetName);
        SheetManager sheetManager = sheets.get(sheetName);

        return sheetManager.getSpreadsheetState();
    }

    @Override
    public SpreadsheetDTO filterSheet(Character selectedColumn, String filterArea, List<String> selectedValues, String sheetName, String userName) {
        permissionManager.validateReaderPermission(userName , sheetName);
        SheetManager sheetManager = sheets.get(sheetName);

        return sheetManager.filterSheet(selectedColumn, filterArea, selectedValues);
    }

    @Override
    public List<Coordinate> getRangeByName(String range, String sheetName, String userName) {
        permissionManager.validateReaderPermission(userName , sheetName);
        SheetManager sheetManager = sheets.get(sheetName);

        return sheetManager.getRangeByName(range);
    }

    @Override
    public List<String> getUniqueValuesFromColumn(char column, String userName, String sheetName) {
        permissionManager.validateReaderPermission(userName , sheetName);
        SheetManager sheetManager = sheets.get(sheetName);

        return sheetManager.getUniqueValuesFromColumn(column);
    }

    @Override
    public void removeRangeFromSheet(String selectedRange, String userName, String sheetName, int clientVersion) {
        permissionManager.validateWriterPermission(userName , sheetName);
        SheetManager sheetManager = sheets.get(sheetName);
        validateUpdatedSheet(sheetManager,clientVersion);
        sheetManager.removeRangeFromSheet(selectedRange);
    }

    @Override
    public SpreadsheetDTO sort(String cellsRange, List<Character> selectedColumns, String userName, String sheetName) {
        permissionManager.validateReaderPermission(userName , sheetName);
        SheetManager sheetManager = sheets.get(sheetName);

        return sheetManager.sort(cellsRange, selectedColumns);
    }

    @Override
    public void setSingleCellTextColor(String cellId, String color, String userName, String sheetName,int clientVersion) {
        permissionManager.validateWriterPermission(userName , sheetName);
        SheetManager sheetManager = sheets.get(sheetName);
        validateUpdatedSheet(sheetManager,clientVersion);
        sheetManager.setSingleCellTextColor(cellId, color);
    }

    @Override
    public List<CellDTO> updateCell(String cellId, String newValue, String userName, String sheetName, int clientVersion) {
        permissionManager.validateWriterPermission(userName , sheetName);
        SheetManager sheetManager = sheets.get(sheetName);
        validateUpdatedSheet(sheetManager,clientVersion);
        return sheetManager.updateCell(cellId, newValue , userName);
    }

    @Override
    public SpreadsheetDTO getSpreadsheetByVersion(int version, String userName, String sheetName) {
        permissionManager.validateReaderPermission(userName , sheetName);
        SheetManager sheetManager = sheets.get(sheetName);

        return sheetManager.getSpreadsheetByVersion(version);
    }

    @Override
    public SpreadsheetDTO getLatestVersion(String sheetName, String userName) {
        permissionManager.validateReaderPermission(userName,sheetName);

        SheetManager sheetManager = sheets.get(sheetName);
        return sheetManager.getSpreadsheetState();
    }

    @Override
    public synchronized int getLatestVersionNumber(String sheetName, String userName) {
        permissionManager.validateReaderPermission(userName,sheetName);

        SheetManager sheetManager = sheets.get(sheetName);
        return sheetManager.getCurrentVersion();
    }

    public void validateUpdatedSheet(SheetManager sheetManager, int clientVersion) throws OutdatedVersionException {
        int latestVersion = sheetManager.getCurrentVersion();

        if (clientVersion < latestVersion) {
            throw new OutdatedVersionException("Client version is outdated. Please refresh to the latest version.");
        }
    }
}
