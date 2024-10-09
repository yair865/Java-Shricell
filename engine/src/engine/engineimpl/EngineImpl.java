package engine.engineimpl;

import engine.sheetmanager.SheetManager;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class EngineImpl implements Engine, Serializable {

    private static final EngineImpl instance = new EngineImpl();
    private final Map<String, SheetManager> sheets;
    private SheetManager currentSheet;

    private EngineImpl() {
        this.sheets = new HashMap<>();
    }

    public static EngineImpl getInstance() {
        return instance;
    }

    @Override
    public void addSheet(String sheetName, SheetManager sheetManager, String filePath) {
        if (sheets.containsKey(sheetName)) {
            throw new IllegalArgumentException("Sheet with name '" + sheetName + "' already exists.");
        }
        try {
            sheetManager.loadSpreadsheet(filePath);
        } catch (Exception e) {
            throw new IllegalArgumentException("Could not load sheet '" + sheetName + "'.");
        }
        sheets.put(sheetName, sheetManager);
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

    // Other methods from the Engine interface...
}
