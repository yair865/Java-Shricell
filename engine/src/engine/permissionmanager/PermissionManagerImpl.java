package engine.permissionmanager;

import dto.converter.PermissionInfoConverter;
import dto.dtoPackage.PermissionInfoDTO;
import engine.permissionmanager.request.PermissionRequest;
import engine.permissionmanager.request.PermissionRequestImpl;
import engine.permissionmanager.request.RequestStatus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PermissionManagerImpl implements PermissionManager {

    // This map stores permissions for each sheet <sheetName, <userName, PermissionType>>
    private Map<String, Map<String, PermissionType>> sheetPermissions;
    private Map<String, Map<Integer, PermissionRequest>> permissionRequests; // Sheet Name -> (Request ID -> (User, PermissionType, Status))
    private Map<String, String> owners; // Sheet Name -> UserName
    private static int requestID = 0;

    public PermissionManagerImpl() {
        this.sheetPermissions = new HashMap<>();
        this.permissionRequests = new HashMap<>();
        this.owners = new HashMap<>();
    }

    @Override
    public void assignPermission(String sheetName, String userName, PermissionType permission) {
        sheetPermissions.putIfAbsent(sheetName, new HashMap<>());
        Map<String, PermissionType> userPermissions = sheetPermissions.get(sheetName);
        userPermissions.put(userName, permission);

        if (permission == PermissionType.OWNER) {
            owners.put(sheetName, userName);
        }
    }

    @Override
    public void updatePermissions(String usernameFromSession, String sheetName, int requestId, RequestStatus requestStatus) {

        if (isOwner(sheetName, usernameFromSession)) {

            if (!permissionRequests.containsKey(sheetName) || !permissionRequests.get(sheetName).containsKey(requestId)) {
                throw new IllegalArgumentException("Request ID " + requestId + " not found for sheet " + sheetName);
            }

            PermissionRequest request = permissionRequests.get(sheetName).get(requestId);

            if (request.getStatus() != RequestStatus.PENDING) {
                throw new IllegalArgumentException("Request ID " + requestId + " is not in pending status.");
            }

            PermissionType currentPermission = getPermission(sheetName, request.getUserName());
            if (currentPermission == PermissionType.OWNER) {
                throw new IllegalArgumentException("Cannot change permission from OWNER for user " + request.getUserName());
            }

            assignPermission(sheetName, request.getUserName(), request.getType());
            request.setStatus(requestStatus);

            System.out.println("Permissions updated for user " + request.getUserName() + " on sheet " + sheetName + " to " + request.getType());

        } else {
            throw new IllegalArgumentException("User " + usernameFromSession + " is not an owner and not eligible for changing permissions.");
        }
    }

    @Override
    public PermissionType getPermission(String sheetName, String userName) {
        if (sheetPermissions.containsKey(sheetName)) {
            return sheetPermissions.get(sheetName).getOrDefault(userName, PermissionType.NONE);
        }
        return PermissionType.NONE; // Default if no permission is found
    }

    @Override
    public void removePermission(String sheetName, String userName) {
        if (sheetPermissions.containsKey(sheetName)) {
            sheetPermissions.get(sheetName).remove(userName);
        }
    }

    @Override
    public boolean isOwner(String sheetName, String userName) {
        return getPermission(sheetName, userName) == PermissionType.OWNER;
    }

    @Override
    public List<PermissionInfoDTO> getAllPermissionsForSheet(String sheetName) {
        List<PermissionInfoDTO> permissions = new ArrayList<>();

        String owner = owners.get(sheetName);
        if (owner != null) {
            permissions.add(PermissionInfoConverter.ConvertPermissionsInformationToDTO(owner, PermissionType.OWNER, null, -1));
        }

        if (!permissionRequests.containsKey(sheetName)) {
            return permissions;
        }

        Map<Integer, PermissionRequest> requestsForSheet = permissionRequests.get(sheetName);

        for (Map.Entry<Integer, PermissionRequest> entry : requestsForSheet.entrySet()) {
            PermissionRequest request = entry.getValue();

            PermissionInfoDTO dto = PermissionInfoConverter.ConvertPermissionsInformationToDTO(
                    request.getUserName(),
                    request.getType(),
                    request.getStatus(),
                    entry.getKey()
            );

            permissions.add(dto);
        }

        return permissions;
    }

    @Override
    public void createRequest(String userName, PermissionType permissionType, String sheetName) {
        if(isOwner(sheetName, userName)) {
            throw new IllegalArgumentException("Cannot create request for user " + userName + " because he is already owner of the sheet " + sheetName);
        }
        PermissionRequest request = new PermissionRequestImpl(permissionType, userName);
        Map<Integer, PermissionRequest> requestWithID = permissionRequests.computeIfAbsent(sheetName, k -> new HashMap<>());
        requestWithID.put(requestID++, request);
    }

    @Override
    public void validateReaderPermission(String username, String sheetName) {
        validateSheet(sheetName);
        PermissionType permission = sheetPermissions.get(sheetName).get(username);

        if(permission == PermissionType.NONE) {
            throw new IllegalArgumentException("User [" + username + "] does not have permission for sheet [" + sheetName + "].");
        }
    }

    @Override
    public void validateWriterPermission(String userName, String sheetName) {
        validateSheet(sheetName);
        PermissionType permission = sheetPermissions.get(sheetName).get(userName);
        if(permission == PermissionType.NONE || permission == PermissionType.READER ) {
            throw new IllegalArgumentException("User [" + userName + "] does not have write permission for sheet [" + sheetName + "].");
        }
    }

    private void validateSheet(String sheetName) {
        if (!sheetPermissions.containsKey(sheetName)) {
            throw new IllegalArgumentException("Sheet " + sheetName + " does not exist");
        }
    }
}
