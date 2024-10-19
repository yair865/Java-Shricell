package engine.permissionmanager;

import java.util.Map;

public interface PermissionManager {
    void assignPermission(String sheetName, String userName, PermissionType permission);
    PermissionType getPermission(String sheetName, String userName);
    void removePermission(String sheetName, String userName);
    boolean isOwner(String sheetName, String userName);
    Map<String, PermissionType> getAllPermissionsForSheet(String sheetName);
}
