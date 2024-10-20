package engine.permissionmanager;

import dto.converter.PermissionInfoConverter;
import dto.dtoPackage.PermissionInfoDTO;
import engine.permissionmanager.request.PermissionRequest;
import engine.permissionmanager.request.PermissionRequestImpl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PermissionManagerImpl implements PermissionManager {

    // This map stores permissions for each sheet <sheetName, <userName, PermissionType>>
    private Map<String, Map<String, PermissionType>> sheetPermissions;
    private Map<String, Map<Integer, PermissionRequest>> permissionRequests; // Sheet Name -> (Request ID -> (User, PermissionType, Status))
    private Map<String,String> owners; // Sheet Name -> UserName
    private static int requestID = 1;


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

        if(permission == PermissionType.OWNER) {
            owners.put(sheetName, userName);
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
            permissions.add(PermissionInfoConverter.ConvertPermissionsInformationToDTO(owner, PermissionType.OWNER, null));
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
                    request.getStatus()
            );

            permissions.add(dto);
        }

        return permissions;
    }

    @Override
    public void createRequest(String userName, PermissionType permissionType, String sheetName) {
        PermissionRequest request = new PermissionRequestImpl(permissionType , userName);
        Map<Integer,PermissionRequest> requestWithID = new HashMap<>();
        requestWithID.put(requestID++, request);
        permissionRequests.putIfAbsent(sheetName, requestWithID);
    }

}
