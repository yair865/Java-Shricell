package api;

public interface CellReadActions {
    String getOriginalValue();
    EffectiveValue getEffectiveValue();
    int getLastModifiedVersionVersion();
}
