package dto.dtoPackage;

import engine.permissionmanager.PermissionType;
import engine.permissionmanager.request.RequestStatus;

public record PermissionInfoDTO(String username , PermissionType permissionType , RequestStatus status) { }
