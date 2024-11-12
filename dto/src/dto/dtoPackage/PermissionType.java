package dto.dtoPackage;

public enum PermissionType {
    OWNER,   // The owner of the sheet, with full control over permissions
    READER,  // Can only view the sheet, but not make changes
    WRITER,  // Can make changes to the sheet
    NONE     // No permission, default for new users until granted a permission
    ;
}
