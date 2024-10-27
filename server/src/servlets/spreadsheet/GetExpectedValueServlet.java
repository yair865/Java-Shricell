package servlets.spreadsheet;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import dto.dtoPackage.SpreadsheetDTO;
import engine.engineimpl.Engine;
import engine.sheetimpl.cellimpl.coordinate.Coordinate;
import engine.sheetimpl.cellimpl.coordinate.CoordinateFactory;
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

        Engine engine = ServletUtils.getEngine(getServletContext());
        Gson gson = new Gson();

        try {
            JsonObject jsonRequest = gson.fromJson(req.getReader(), JsonObject.class);
            int row = jsonRequest.get("row").getAsInt();
            int column = jsonRequest.get("column").getAsInt();
            String value = jsonRequest.get("value").getAsString();

            Coordinate coordinate = CoordinateFactory.createCoordinate(row, column);
            SpreadsheetDTO sheetDTO = engine.getExpectedValue(username,coordinate, value);

            String jsonSheet = gson.toJson(sheetDTO);
            out.println(jsonSheet);
            out.flush();
        } catch (Exception e) {
            ServletUtils.sendErrorResponse(resp, e.getMessage());
        }
    }

}
