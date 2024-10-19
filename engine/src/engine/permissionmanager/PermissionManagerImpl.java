package engine.permissionmanager;

import java.util.HashMap;
import java.util.Map;

public class PermissionManagerImpl implements PermissionManager {

    // This map stores permissions for each sheet <sheetName, <userName, PermissionType>>
    private Map<String, Map<String, PermissionType>> sheetPermissions;

    // Constructor
    public PermissionManagerImpl() {
        this.sheetPermissions = new HashMap<>();
    }

    // Assign permission to a user for a specific sheet
    @Override
    public void assignPermission(String sheetName, String userName, PermissionType permission) {
        // Ensure the sheet's permission map exists
        sheetPermissions.putIfAbsent(sheetName, new HashMap<>());

        // Assign the permission to the user
        Map<String, PermissionType> userPermissions = sheetPermissions.get(sheetName);
        userPermissions.put(userName, permission);
    }

    // Get the permission of a user for a specific sheet
    @Override
    public PermissionType getPermission(String sheetName, String userName) {
        if (sheetPermissions.containsKey(sheetName)) {
            return sheetPermissions.get(sheetName).getOrDefault(userName, PermissionType.NONE);
        }
        return PermissionType.NONE; // Default if no permission is found
    }

    // Remove a user's permission for a specific sheet
    @Override
    public void removePermission(String sheetName, String userName) {
        if (sheetPermissions.containsKey(sheetName)) {
            sheetPermissions.get(sheetName).remove(userName);
        }
    }

    // Check if a user is the owner of a sheet
    @Override
    public boolean isOwner(String sheetName, String userName) {
        return getPermission(sheetName, userName) == PermissionType.OWNER;
    }

    // List all users and their permissions for a specific sheet
    @Override
    public Map<String, PermissionType> getAllPermissionsForSheet(String sheetName) {
        return sheetPermissions.getOrDefault(sheetName, new HashMap<>());
    }

    // Additional methods for managing permissions can be added here
}
