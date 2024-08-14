package enginePackage.api;

import java.util.List;

public interface Cell {
    String getOriginalValue();
    EffectiveValue getEffectiveValue();
    int getLastModifiedVersion();
    List<Cell> getDependencies();
    List<Cell> getDependents();

    void setOriginalValue(String value);
    void updateEffectiveValue(String value) throws Exception;
}
