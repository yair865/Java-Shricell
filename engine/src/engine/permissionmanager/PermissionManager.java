package engine.permissionmanager;

import dto.dtoPackage.PermissionInfoDTO;
import engine.permissionmanager.request.RequestStatus;

import java.util.List;

public interface PermissionManager {
    void assignPermission(String sheetName, String userName, PermissionType permission);

    void updatePermissions(String usernameFromSession, String sheetName, int requestId, RequestStatus requestStatus);

    PermissionType getPermission(String sheetName, String userName);
    void removePermission(String sheetName, String userName);
    boolean isOwner(String sheetName, String userName);
    List<PermissionInfoDTO> getAllPermissionsForSheet(String sheetName);
    void createRequest(String userName, PermissionType permissionType, String sheetName);

    void validateReaderPermission(String username, String sheetTitle);
}
