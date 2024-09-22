package engine.api;

public interface Spreadsheet extends SheetReadActions, SheetWriteActions {
    void addRange(String name, String rangeDefinition);
}
