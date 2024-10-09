package engine.api;

import dto.dtoPackage.SpreadsheetDTO;

import java.util.Map;

public interface VersionManager {
    void addVersion(int version, Spreadsheet spreadsheet);
    SpreadsheetDTO getVersion(int version);
    Map<Integer, SpreadsheetDTO> getAllVersions();
    void removeVersion(int version);
    int getCurrentVersion();
}
