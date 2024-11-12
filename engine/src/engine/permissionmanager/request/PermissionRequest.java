package engine.permissionmanager.request;

import dto.dtoPackage.PermissionType;
import dto.dtoPackage.RequestStatus;

public interface PermissionRequest {
    PermissionType getType();

    String getUserName();

    RequestStatus getStatus();

    void setStatus(RequestStatus status);
}
