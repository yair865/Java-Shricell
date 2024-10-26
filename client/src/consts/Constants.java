package consts;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dto.deserialize.CoordinateTypeAdapter;
import dto.deserialize.EffectiveValueTypeAdapter;
import dto.deserialize.SpreadsheetDTODeserializer;
import dto.dtoPackage.SpreadsheetDTO;
import engine.sheetimpl.cellimpl.api.EffectiveValue;
import engine.sheetimpl.cellimpl.coordinate.Coordinate;

public class Constants {
    public final static Gson GSON_INSTANCE = new Gson();

    public static final Gson ADAPTED_GSON = new GsonBuilder()
            .registerTypeAdapter(SpreadsheetDTO.class, new SpreadsheetDTODeserializer())
            .registerTypeAdapter(Coordinate.class, new CoordinateTypeAdapter())
            .registerTypeAdapter(EffectiveValue.class, new EffectiveValueTypeAdapter())
            .create();

    public final static String LINE_SEPARATOR = System.lineSeparator();
    public final static String UNKNOWN = "<Anonymous>";
    public final static int REFRESH_RATE = 2000;

    public final static String LOGIN_PAGE_FXML_RESOURCE_LOCATION = "/component/login/login.fxml";
    public final static String DASHBOARD_PAGE_FXML_RESOURCE_LOCATION = "/component/dashboard/dashboard.fxml";
    public final static String APPLICATION_PAGE_FXML_RESOURCE_LOCATION = "/component/sheetview/app/ShticellApplication.fxml";
    public final static String MAIN_PAGE_FXML_RESOURCE_LOCATION = "/component/main/main.fxml";

    public final static String BASE_DOMAIN = "localhost";

    private final static String BASE_URL = "http://" + BASE_DOMAIN + ":8080";
    private final static String CONTEXT_PATH = "/shticellApp";
    private final static String FULL_SERVER_PATH = BASE_URL + CONTEXT_PATH;
    public final static String LOGIN_PAGE = FULL_SERVER_PATH + "/login";
    public static final String UPLOAD_FILE_PAGE = FULL_SERVER_PATH + "/load";
    public static final String VIEW_SHEET_URL = FULL_SERVER_PATH + "/view";
    public static final String REQUEST_PERMISSION = FULL_SERVER_PATH + "/requestPermission";
    public final static String PERMISSION_RESPONSE = FULL_SERVER_PATH + "/responsePermissionRequest";

    public final static String UPDATE_CELL = FULL_SERVER_PATH + "/updateCell";
    public final static String DEPENDENTS = FULL_SERVER_PATH + "/dependents";
    public final static String REFERENCES = FULL_SERVER_PATH + "/references";
    public final static String VERSION = FULL_SERVER_PATH + "/version";
    public final static String SORT = FULL_SERVER_PATH + "/sort";
    public final static String FILTER = FULL_SERVER_PATH + "/filter";
    public final static String GET_UNIQUE_VALUES = FULL_SERVER_PATH + "/getUniqueValues";
    public final static String BACKGROUND_COLOR = FULL_SERVER_PATH + "/backgroundColor";
    public final static String TEXT_COLOR = FULL_SERVER_PATH + "/textColor";
    public final static String GET_RANGE = FULL_SERVER_PATH + "/getRange";
    public final static String REMOVE_RANGE = FULL_SERVER_PATH + "/removeRange";
    public final static String ADD_RANGE = FULL_SERVER_PATH + "/addRange";
    public final static String GET_EXPECTED_VALUE_PATH = FULL_SERVER_PATH + "/getExpectedValue";

    public final static String SHEETS_LIST = FULL_SERVER_PATH + "/sheetList";
    public final static String PERMISSIONS_LIST = FULL_SERVER_PATH + "/permissionList";
}
