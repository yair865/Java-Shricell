package engine.api;

public interface Cell extends CellReadActions , CellWriteActions{
    void setTextColor(String textColor);

    void setBackgroundColor(String backgroundColor);
}
