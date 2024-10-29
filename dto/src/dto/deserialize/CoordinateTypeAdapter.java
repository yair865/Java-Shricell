package dto.deserialize;

import com.google.gson.*;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import dto.dtoPackage.coordinate.Coordinate;
import dto.dtoPackage.coordinate.CoordinateImpl;

import java.io.IOException;

public class CoordinateTypeAdapter extends TypeAdapter<Coordinate> {
    @Override
    public void write(JsonWriter out, Coordinate coordinate) throws IOException {
        out.beginObject();
        out.name("row").value(coordinate.row());
        out.name("column").value(coordinate.column());
        out.endObject();
    }

    @Override
    public Coordinate read(JsonReader in) throws IOException {
        in.beginObject();
        int row = 0;
        int column = 0;
        while (in.hasNext()) {
            String name = in.nextName();
            switch (name) {
                case "row":
                    row = in.nextInt();
                    break;
                case "column":
                    column = in.nextInt();
                    break;
            }
        }
        in.endObject();
        return new CoordinateImpl(row, column);
    }
}


