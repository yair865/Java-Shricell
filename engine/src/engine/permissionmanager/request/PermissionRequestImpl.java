package engine.permissionmanager.request;

import dto.dtoPackage.PermissionType;
import dto.dtoPackage.RequestStatus;

public class PermissionRequestImpl implements PermissionRequest {
    private final PermissionType type;
    private final String userName;
    private RequestStatus status;

    public PermissionRequestImpl(PermissionType type, String userName) {
        this.type = type;
        this.userName = userName;
        this.status = RequestStatus.PENDING;
    }

    @Override
    public PermissionType getType() {
        return type;
    }

    @Override
    public String getUserName() {
        return userName;
    }

    @Override
    public RequestStatus getStatus() {
        return status;
    }

    @Override
    public void setStatus(RequestStatus status) {
        this.status = status;
    }
}
