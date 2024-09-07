package engine.api;

public interface CellReadActions {
    String getOriginalValue();
    EffectiveValue getEffectiveValue();
    int getLastModifiedVersionVersion();
    Coordinate getCoordinate();
}
