package engine.engineimpl;

import dto.dtoPackage.SheetInfoDTO;
import engine.sheetmanager.SheetManager;

import java.io.File;
import java.io.InputStream;
import java.util.List;

public interface Engine {
    void addSheet(InputStream fileContent , String userName);
    SheetManager getSheet(String sheetName);
    void setCurrentSheet(SheetManager currentSheet);
    List<SheetInfoDTO> getSheets();
}