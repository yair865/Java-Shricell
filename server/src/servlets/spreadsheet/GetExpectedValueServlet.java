package servlets.spreadsheet;

import com.google.gson.JsonObject;
import constant.Constants;
import dto.dtoPackage.SpreadsheetDTO;
import engine.engineimpl.Engine;
import dto.dtoPackage.coordinate.Coordinate;
import dto.dtoPackage.coordinate.CoordinateFactory;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import utils.ServletUtils;
import utils.SessionUtils;

import java.io.IOException;
import java.io.PrintWriter;


@WebServlet(name = "Get Expected Value Servlet" , urlPatterns = "/getExpectedValue")
public class GetExpectedValueServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        PrintWriter out = resp.getWriter();

        String username = SessionUtils.getUsername(req);
        String sheetName = SessionUtils.getSheetName(req);

        Engine engine = ServletUtils.getEngine(getServletContext());


        try {
            JsonObject jsonRequest = Constants.GSON_INSTANCE.fromJson(req.getReader(), JsonObject.class);
            int row = jsonRequest.get("row").getAsInt();
            int column = jsonRequest.get("column").getAsInt();
            String value = jsonRequest.get("value").getAsString();

            Coordinate coordinate = CoordinateFactory.createCoordinate(row, column);
            SpreadsheetDTO sheetDTO = engine.getExpectedValue(username,coordinate, value,sheetName);

            String jsonSheet = Constants.GSON_INSTANCE.toJson(sheetDTO);
            out.println(jsonSheet);
            out.flush();
        } catch (Exception e) {
            ServletUtils.sendErrorResponse(resp, e.getMessage());
        }
    }

}
