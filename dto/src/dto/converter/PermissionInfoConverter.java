package dto.converter;

import dto.dtoPackage.PermissionInfoDTO;
import dto.dtoPackage.PermissionType;
import dto.dtoPackage.RequestStatus;

public class PermissionInfoConverter {
    public static PermissionInfoDTO ConvertPermissionsInformationToDTO(String userName , PermissionType permissionType , RequestStatus requestStatus, int requestId) {

        return new PermissionInfoDTO(userName,
                permissionType,
                requestStatus,
                requestId);
    }
}
