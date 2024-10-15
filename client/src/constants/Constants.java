package constants;

import com.google.gson.Gson;

public class Constants {

    public final static Gson GSON_INSTANCE = new Gson();

    public final static String LINE_SEPARATOR = System.lineSeparator();
    public final static String UNKNOWN = "<Anonymous>";
    public final static int REFRESH_RATE = 10000;

    public final static String LOGIN_PAGE_FXML_RESOURCE_LOCATION = "/component/login/login.fxml";
    public final static String DASHBOARD_PAGE_FXML_RESOURCE_LOCATION = "/component/dashboard/dashboard.fxml";
    public final static String MAIN_PAGE_FXML_RESOURCE_LOCATION = "/component/main/main.fxml";

    public final static String BASE_DOMAIN = "localhost";
    private final static String BASE_URL = "http://" + BASE_DOMAIN + ":8080";
    private final static String CONTEXT_PATH = "/shticellApp";
    private final static String FULL_SERVER_PATH = BASE_URL + CONTEXT_PATH;
    public final static String LOGIN_PAGE = FULL_SERVER_PATH + "/login";
    public static final String UPLOAD_FILE_PAGE = FULL_SERVER_PATH + "/load";

    public final static String SHEETS_LIST = FULL_SERVER_PATH + "/sheetList";


}
