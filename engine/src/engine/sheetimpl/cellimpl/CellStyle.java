package engine.sheetimpl.cellimpl;

import java.io.Serializable;

public class CellStyle implements Serializable {
    private String textColor;
    private String backgroundColor;

    public CellStyle() {
        this.textColor = "BLACK";
        this.backgroundColor = "WHITE";
    }

    public String getTextColor() {
        return textColor;
    }

    public void setTextColor(String textColor) {
        this.textColor = textColor;
    }

    public String getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(String backgroundColor) {
        this.backgroundColor = backgroundColor;
    }
}
