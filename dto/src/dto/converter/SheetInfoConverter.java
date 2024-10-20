package dto.converter;
import dto.dtoPackage.SheetInfoDTO;
import engine.permissionmanager.PermissionType;
import engine.sheetmanager.SheetManager;

public class SheetInfoConverter {
    public static SheetInfoDTO convertSheetsInformationToDTO(SheetManager sheet, PermissionType permissionType) {
        return  new SheetInfoDTO(
                sheet.getUserName(),
                sheet.getSheetTitle(),
                sheet.getSheetNumberOfRows(),
                sheet.getSheetNumberOfColumns(),
                permissionType);
    }
}

