package engine.permissionmanager;

import dto.dtoPackage.PermissionInfoDTO;

import java.util.List;

public interface PermissionManager {
    void assignPermission(String sheetName, String userName, PermissionType permission);
    PermissionType getPermission(String sheetName, String userName);
    void removePermission(String sheetName, String userName);
    boolean isOwner(String sheetName, String userName);
    List<PermissionInfoDTO> getAllPermissionsForSheet(String sheetName);
    void createRequest(String userName, PermissionType permissionType, String sheetName);
}
