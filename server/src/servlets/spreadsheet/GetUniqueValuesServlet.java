package servlets.spreadsheet;

import com.google.gson.Gson;
import engine.engineimpl.Engine;
import engine.sheetmanager.SheetManager;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import utils.ServletUtils;
import utils.SessionUtils;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet(name = "Get Unique Values Servlet" , urlPatterns = "/getUniqueValues")
public class GetUniqueValuesServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Engine engine = ServletUtils.getEngine(request.getServletContext());
        String sheetName = SessionUtils.getSheetName(request);
        String userName = SessionUtils.getUsername(request);
        Gson gson = new Gson();

        String columnParam = request.getParameter("column");

        if (columnParam == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing column parameter");
            return;
        }

        char column = columnParam.charAt(0);

        try {
            List<String> uniqueValues = engine.getUniqueValuesFromColumn(column , userName , sheetName);
            response.setContentType("application/json");
            PrintWriter out = response.getWriter();
            out.print(gson.toJson(uniqueValues));
            out.flush();
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error retrieving values from column: " + e.getMessage());
        }
    }
}