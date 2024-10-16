package component.dashboard.body.sheetListArea.sheetdata;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class SingleSheetData {
    private final String userName;
    private final StringProperty sheetName;
    private final String sheetSize;

    public SingleSheetData(String userName, String sheetName, String sheetSize) {
        this.userName = userName;
        this.sheetName = new SimpleStringProperty(sheetName);
        this.sheetSize = sheetSize;
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
}
