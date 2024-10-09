package engine.engineimpl;

import engine.sheetmanager.SheetManager;

public interface Engine {
    void addSheet(String sheetName, SheetManager sheetManager, String filePath);
    SheetManager getSheet(String sheetName);
    void setCurrentSheet(SheetManager currentSheet);
}