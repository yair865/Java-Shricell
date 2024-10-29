package component.dashboard.body.permissionListArea.permissiondata;

import dto.dtoPackage.PermissionType;
import dto.dtoPackage.RequestStatus;

public class SinglePermissionData {
    private final String username;
    private final PermissionType permission;
    private RequestStatus status;
    private final int requestID;

    public SinglePermissionData(String username, PermissionType permission, RequestStatus status, int requestID) {
        this.username = username;
        this.permission = permission;
        this.status = status;
        this.requestID = requestID;
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

    public int getRequestID() {
        return requestID;
    }
}
