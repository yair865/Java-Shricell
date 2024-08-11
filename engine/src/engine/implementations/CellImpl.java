package engine.implementations;

import engine.interfaces.Cell;

import java.util.ArrayList;
import java.util.List;

public class CellImpl implements Cell {
    private String originalValue;
    private String effectiveValue;
    private int lastModifiedVersion;
    private List<Cell> dependencies;
    private List<Cell> dependents;

    public CellImpl() {
        dependencies = new ArrayList<>();
        dependents = new ArrayList<>();
    }

    @Override
    public String getOriginalValue() {
        return originalValue;
    }

    @Override
    public String getEffectiveValue() {
        return effectiveValue;
    }

    @Override
    public int getLastModifiedVersion() {
        return lastModifiedVersion;
    }

    @Override
    public List<Cell> getDependencies() {
        return dependencies;
    }

    @Override
    public List<Cell> getDependents() {
        return dependents;
    }

    @Override
    public void setOriginalValue(String value) {
        this.originalValue = value;
    }

    @Override
    public void updateEffectiveValue(String value) throws Exception {
        // Implementation for updating the effective value
    }
}
