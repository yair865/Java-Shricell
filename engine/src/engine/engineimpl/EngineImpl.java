package engine.engineimpl;

import dto.converter.SheetListConverter;
import dto.dtoPackage.SheetInfoDTO;
import engine.sheetmanager.SheetManager;
import engine.sheetmanager.SheetManagerImpl;

import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class EngineImpl implements Engine, Serializable {

    private static final EngineImpl instance = new EngineImpl();
    private final Map<String, SheetManager> sheets;
    private SheetManager currentSheet;

    public EngineImpl() {
        this.sheets = new HashMap<>();
    }

    @Override
    public void addSheet(InputStream fileContent , String userName) {
        String sheetName = null;
        SheetManager sheetManager = new SheetManagerImpl(userName);

             sheetName = sheetManager.loadSpreadsheet(fileContent);
             validateSheetExists(sheetName);

        sheets.put(sheetName, sheetManager);
    }

    private void validateSheetExists(String sheetName) {
        if (sheets.containsKey(sheetName)) {
            throw new IllegalArgumentException("Sheet with name '" + sheetName + "' already exists.");
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
    public synchronized List<SheetInfoDTO> getSheets() {
        System.out.println("Current sheets size: " + sheets.size());
        sheets.forEach((key, value) -> System.out.println("Sheet Key: " + key + ", Value: " + value));

        List<SheetInfoDTO> sheetInfoDTOs = SheetListConverter.convertSheetsListToDTO(
                new ArrayList<>(sheets.values())
        );
        System.out.println("Sheets list size: " + sheetInfoDTOs.size()); // Debug log
        return sheetInfoDTOs;
    }



}
