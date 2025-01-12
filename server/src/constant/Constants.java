package constant;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dto.deserialize.CoordinateTypeAdapter;
import dto.deserialize.EffectiveValueTypeAdapter;
import dto.deserialize.SpreadsheetDTODeserializer;
import dto.dtoPackage.SpreadsheetDTO;
import dto.dtoPackage.effectivevalue.EffectiveValue;
import dto.dtoPackage.coordinate.Coordinate;

public class Constants {
    public static final String USERNAME = "username";

    public static final Gson GSON_INSTANCE = new GsonBuilder()
            .registerTypeAdapter(SpreadsheetDTO.class, new SpreadsheetDTODeserializer())
            .registerTypeAdapter(Coordinate.class, new CoordinateTypeAdapter())
            .registerTypeAdapter(EffectiveValue.class, new EffectiveValueTypeAdapter())
            .create();
}
