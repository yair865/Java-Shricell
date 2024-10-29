package dto.deserialize;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import engine.sheetimpl.cellimpl.EffectiveValueImpl;
import engine.sheetimpl.cellimpl.api.EffectiveValue;
import engine.sheetimpl.utils.CellType;

import java.io.IOException;

public class EffectiveValueTypeAdapter extends TypeAdapter<EffectiveValue> {
    @Override
    public void write(JsonWriter out, EffectiveValue effectiveValue) throws IOException {
        out.beginObject();
        out.name("cellType").value(effectiveValue.getCellType().toString());
        out.name("value");

        Object value = effectiveValue.getValue();
        if (value instanceof Double && ((Double) value).isNaN()) {
            out.value("NaN");
        } else {
            Gson gson = new Gson();
            gson.toJson(value, value.getClass(), out);
        }

        out.endObject();
    }

    @Override
    public EffectiveValueImpl read(JsonReader in) throws IOException {
        CellType cellType = null;
        Object value = null;

        in.beginObject();
        while (in.hasNext()) {
            String name = in.nextName();
            switch (name) {
                case "cellType":
                    cellType = CellType.valueOf(in.nextString().toUpperCase());
                    break;
                case "value":
                    value = deserializeValue(in, cellType);
                    break;
                default:
                    in.skipValue();
            }
        }
        in.endObject();

        return new EffectiveValueImpl(cellType, value);
    }

    private Object deserializeValue(JsonReader in, CellType cellType) throws IOException {
        switch (cellType) {
            case NUMERIC:
                String numericValue = in.nextString();
                if ("NaN".equals(numericValue)) {
                    return Double.NaN;
                } else {
                    return Double.parseDouble(numericValue);
                }
            case BOOLEAN:
                return in.nextBoolean();
            default:
                return in.nextString();
        }
    }
}
