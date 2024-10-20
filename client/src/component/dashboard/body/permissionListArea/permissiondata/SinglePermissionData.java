package component.dashboard.body.permissionListArea.permissiondata;

import engine.permissionmanager.PermissionType;
import engine.permissionmanager.request.RequestStatus;

public class SinglePermissionData {
    private final String username;
    private final PermissionType permission;
    private RequestStatus status;

    public SinglePermissionData(String username, PermissionType permission, RequestStatus status) {
        this.username = username;
        this.permission = permission;
        this.status = status;
    }

    public String getUsername() {
        return username;
    }

    public PermissionType getPermission() {
        return permission;
    }

    public RequestStatus getStatus() {
        return status;
    }
}
