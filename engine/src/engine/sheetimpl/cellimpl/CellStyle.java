package engine.sheetimpl.cellimpl;

import java.io.Serializable;

public class CellStyle implements Serializable {
    private String textColor;
    private String backgroundColor;

    public CellStyle() {
        this.textColor = null;
        this.backgroundColor = null;
    }

    public String getTextColor() {return textColor;}
    public String getBackgroundColor() {
        return backgroundColor;
    }

    public void setTextColor(String textColor) {this.textColor = textColor;}
    public void setBackgroundColor(String backgroundColor) {
        this.backgroundColor = backgroundColor;
    }
}
