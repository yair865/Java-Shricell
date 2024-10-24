package engine.engineimpl;

import dto.dtoPackage.PermissionInfoDTO;
import dto.dtoPackage.SheetInfoDTO;
import engine.permissionmanager.PermissionType;
import engine.permissionmanager.request.RequestStatus;
import engine.sheetmanager.SheetManager;

import java.io.InputStream;
import java.util.List;

public interface Engine {
    void addSheet(InputStream fileContent , String userName);
    SheetManager getSheet(String sheetName);
    void setCurrentSheet(SheetManager currentSheet);
    List<SheetInfoDTO> getSheets(String user);
    void requestPermission(String userName, PermissionType permissionType, String sheetName);
    List<PermissionInfoDTO> getPermissions(String sheetName);
    void updatePermissions(String usernameFromSession, String sheetName, int requestId, RequestStatus requestStatus);
    SheetManager getCurrentSheet();
}