package engine.engineimpl;

import dto.converter.SheetConverter;
import dto.dtoPackage.SpreadsheetDTO;
import engine.api.Spreadsheet;
import engine.api.VersionManager;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class VersionManagerImpl implements VersionManager , Serializable {
    private final Map<Integer, Spreadsheet> spreadsheetsByVersions;
    private int currentVersion;

    public VersionManagerImpl() {
        spreadsheetsByVersions = new HashMap<>();
        currentVersion = 0;
    }

    @Override
    public void addVersion(int version, Spreadsheet spreadsheet) {
        spreadsheetsByVersions.put(version, spreadsheet);
        currentVersion = version; // Update current version
    }

    @Override
    public SpreadsheetDTO getVersion(int version) {
        Spreadsheet spreadsheet = spreadsheetsByVersions.get(version);
        if (spreadsheet == null) {
            throw new IllegalArgumentException("No spreadsheet found for version: " + version);
        }
        return SheetConverter.convertSheetToDTO(spreadsheet);
    }

    @Override
    public Map<Integer, SpreadsheetDTO> getAllVersions() {
        Map<Integer, SpreadsheetDTO> versionDTOMap = new HashMap<>();
        for (Map.Entry<Integer, Spreadsheet> entry : spreadsheetsByVersions.entrySet()) {
            versionDTOMap.put(entry.getKey(), SheetConverter.convertSheetToDTO(entry.getValue()));
        }
        return versionDTOMap;
    }

    @Override
    public void removeVersion(int version) {
        spreadsheetsByVersions.remove(version);
        if (version == currentVersion) {
            currentVersion--; // Decrement current version if the current one is removed
        }
    }

    @Override
    public int getCurrentVersion() {
        return currentVersion;
    }
}
