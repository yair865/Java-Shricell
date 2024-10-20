package component.dashboard.body.sheetListArea.sheetdata;

import engine.permissionmanager.PermissionType;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class SingleSheetData {
    private final String userName;
    private final StringProperty sheetName;
    private final String sheetSize;
    private PermissionType permission;

    public SingleSheetData(String userName, String sheetName, String sheetSize , PermissionType permission) {
        this.userName = userName;
        this.sheetName = new SimpleStringProperty(sheetName);
        this.sheetSize = sheetSize;
        this.permission = permission;
    }

    public String getUserName() {
        return userName;
    }

    public String getSheetName() {
        return sheetName.get();
    }

    public String getSheetSize() {
        return sheetSize;
    }

    public StringProperty sheetNameProperty() {
        return sheetName;
    }

    public PermissionType getPermission() {
        return permission;
    }

    public void setPermission(PermissionType permission) {
        this.permission = permission;
    }
}
