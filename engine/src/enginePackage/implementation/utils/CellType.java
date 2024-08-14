package enginePackage.implementation.utils;

public enum CellType {
    NUMERIC(Double.class) ,
    STRING(String.class) ,
    BOOLEAN(Boolean.class) ;

    private final Class<?> type;

    CellType(Class<?> type) {
        this.type = type;
    }

    public boolean isAssignableFrom(Class<?> aType) {
        return type.isAssignableFrom(aType);
    }
}
