package engine.versionmanager;

import dto.converter.SheetConverter;
import dto.dtoPackage.SpreadsheetDTO;
import engine.api.Spreadsheet;
import engine.api.VersionManager;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class VersionManagerImpl implements VersionManager , Serializable {
    private final Map<Integer, Spreadsheet> spreadsheetsByVersions;
    private int currentVersionNumber;

    public VersionManagerImpl() {
        spreadsheetsByVersions = new HashMap<>();
        currentVersionNumber = 0;
    }

    @Override
    public void addVersion(int version, Spreadsheet spreadsheet) {
        spreadsheetsByVersions.put(version, spreadsheet);
        currentVersionNumber = version;
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
        if (version == currentVersionNumber) {
            currentVersionNumber--;
        }
    }

    @Override
    public int getCurrentVersionNumber() {
        return currentVersionNumber;
    }

    @Override
    public SpreadsheetDTO getCurrentVersionDTO() {
        return SheetConverter.convertSheetToDTO(spreadsheetsByVersions.get(currentVersionNumber));
    }

    @Override
    public Spreadsheet getCurrentVersion(){
        return spreadsheetsByVersions.get(currentVersionNumber);
    }

    @Override
    public void setVersionNumber(int version) {
        currentVersionNumber = version;
    }
}
