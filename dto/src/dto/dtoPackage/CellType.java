package dto.dtoPackage;

public enum CellType {
    NUMERIC(Double.class) ,
    STRING(String.class) ,
    EMPTY(String.class) ,
    UNKNOWN(void.class),
    BOOLEAN(Boolean.class),
    ERROR(Error.class);


    private final Class<?> type;

    CellType(Class<?> type) {
        this.type = type;
    }

    public boolean isAssignableFrom(Class<?> aType) {
        return type.isAssignableFrom(aType);
    }

    public  Class<?> getType() {
        return  type;
    }
}
