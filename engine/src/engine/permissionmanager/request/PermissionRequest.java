package engine.permissionmanager.request;

import engine.permissionmanager.PermissionType;

public interface PermissionRequest {
    PermissionType getType();

    String getUserName();

    RequestStatus getStatus();

    void setStatus(RequestStatus status);
}
