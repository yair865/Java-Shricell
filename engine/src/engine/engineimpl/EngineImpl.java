package engine.engineimpl;

import dto.converter.SheetInfoConverter;
import dto.dtoPackage.PermissionInfoDTO;
import dto.dtoPackage.SheetInfoDTO;
import engine.permissionmanager.PermissionManager;
import engine.permissionmanager.PermissionManagerImpl;
import engine.permissionmanager.PermissionType;
import engine.sheetmanager.SheetManager;
import engine.sheetmanager.SheetManagerImpl;

import java.io.InputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class EngineImpl implements Engine, Serializable {

    private static final EngineImpl instance = new EngineImpl();
    private final Map<String, SheetManager> sheets;
    private SheetManager currentSheet;
    private PermissionManager permissionManager;

    public EngineImpl() {
        this.sheets = new HashMap<>();
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

    //validate if the sheet already in the sheets Map.
    private void validateSheetAlreadyExists(String sheetName) {
        if (sheets.containsKey(sheetName)) {
            throw new IllegalArgumentException("Sheet with name '" + sheetName + "' already exists.");
        }
    }

    //validate sheet name is a valid sheet.
    private void ValidateSheetName(String sheetName) {
        if (!sheets.containsKey(sheetName)) {
            throw new IllegalArgumentException("Sheet with name '" + sheetName + "' doesnt exists.");
        }
    }

    @Override
    public SheetManager getSheet(String sheetName) {
        if (!sheets.containsKey(sheetName)) {
            throw new IllegalArgumentException("No sheet found with name '" + sheetName + "'.");
        }
        return sheets.get(sheetName);
    }

    @Override
    public void setCurrentSheet(SheetManager currentSheet) {
        this.currentSheet = currentSheet;
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


}
