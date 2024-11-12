package dto.deserialize;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import dto.dtoPackage.CellDTO;
import dto.dtoPackage.SpreadsheetDTO;
import dto.dtoPackage.coordinate.Coordinate;
import dto.dtoPackage.coordinate.CoordinateImpl;
import dto.dtoPackage.range.Range;
import dto.dtoPackage.range.RangeImpl;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SpreadsheetDTODeserializer implements JsonDeserializer<SpreadsheetDTO> {

    @Override
    public SpreadsheetDTO deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();

        String name = jsonObject.get("name").getAsString();
        int version = jsonObject.get("version").getAsInt();
        int rows = jsonObject.get("rows").getAsInt();
        int columns = jsonObject.get("columns").getAsInt();
        int rowHeightUnits = jsonObject.get("rowHeightUnits").getAsInt();
        int columnWidthUnits = jsonObject.get("columnWidthUnits").getAsInt();

        Map<Coordinate, CellDTO> cells = new HashMap<>();
        JsonObject cellsJson = jsonObject.get("cells").getAsJsonObject();
        for (Map.Entry<String, JsonElement> entry : cellsJson.entrySet()) {
            Coordinate coordinate = deserializeCoordinate(entry.getKey());
            CellDTO cellDTO = context.deserialize(entry.getValue(), CellDTO.class);
            cells.put(coordinate, cellDTO);
        }

        Map<Coordinate, List<Coordinate>> dependenciesAdjacencyList = deserializeAdjacencyList(
                jsonObject.get("dependenciesAdjacencyList").getAsJsonObject(), context);

        Map<Coordinate, List<Coordinate>> referencesAdjacencyList = deserializeAdjacencyList(
                jsonObject.get("referencesAdjacencyList").getAsJsonObject(), context);

        Type listOfCoordinatesType = new TypeToken<List<Coordinate>>() {}.getType();
        List<Coordinate> cellsThatHaveChanged = context.deserialize(
                jsonObject.get("cellsThatHaveChanged").getAsJsonArray(), listOfCoordinatesType);

        Map<String, Range> ranges = new HashMap<>();
        JsonObject rangesJson = jsonObject.get("ranges").getAsJsonObject();
        for (Map.Entry<String, JsonElement> entry : rangesJson.entrySet()) {
            Range range = context.deserialize(entry.getValue(), RangeImpl.class);
            ranges.put(entry.getKey(), range);
        }

        return new SpreadsheetDTO(
                name, version, cells, rows, columns, rowHeightUnits, columnWidthUnits,
                dependenciesAdjacencyList, referencesAdjacencyList, cellsThatHaveChanged, ranges
        );
    }

    private Coordinate deserializeCoordinate(String coordinateStr) {
        int row = Integer.parseInt(coordinateStr.substring(1));
        int column = coordinateStr.charAt(0) - 'A' + 1;
        return new CoordinateImpl(row, column);
    }

    private Map<Coordinate, List<Coordinate>> deserializeAdjacencyList(JsonObject adjacencyListJson, JsonDeserializationContext context) {
        Map<Coordinate, List<Coordinate>> adjacencyList = new HashMap<>();
        Type listOfCoordinatesType = new TypeToken<List<Coordinate>>() {}.getType();
        for (Map.Entry<String, JsonElement> entry : adjacencyListJson.entrySet()) {
            Coordinate key = deserializeCoordinate(entry.getKey());
            List<Coordinate> value = context.deserialize(entry.getValue(), listOfCoordinatesType);
            adjacencyList.put(key, value);
        }
        return adjacencyList;
    }
}
