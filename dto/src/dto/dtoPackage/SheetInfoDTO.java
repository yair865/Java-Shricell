package dto.dtoPackage;

import java.util.Objects;

public class SheetInfoDTO {
    private String ownerName;
    private String sheetName;
    private int numberOfRow;
    private int numberOfColumn;

    public SheetInfoDTO(String userName, String sheetName, int numberOfRow, int numberOfColumn) {
        this.ownerName = userName;
        this.sheetName = sheetName;
        this.numberOfRow = numberOfRow;
        this.numberOfColumn = numberOfColumn;
    }

    public String userName() {
        return ownerName;
    }

    public String sheetName() {
        return sheetName;
    }

    public int numberOfRow() {
        return numberOfRow;
    }

    public int numberOfColumn() {
        return numberOfColumn;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (SheetInfoDTO) obj;
        return Objects.equals(this.ownerName, that.ownerName) &&
                Objects.equals(this.sheetName, that.sheetName) &&
                this.numberOfRow == that.numberOfRow &&
                this.numberOfColumn == that.numberOfColumn;
    }

    @Override
    public int hashCode() {
        return Objects.hash(ownerName, sheetName, numberOfRow, numberOfColumn);
    }

    @Override
    public String toString() {
        return "SheetInfoDTO[" +
                "userName=" + ownerName + ", " +
                "sheetName=" + sheetName + ", " +
                "numberOfRow=" + numberOfRow + ", " +
                "numberOfColumn=" + numberOfColumn + ']';
    }
}
