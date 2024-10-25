package servlets.spreadsheet;

import com.google.gson.Gson;
import engine.engineimpl.Engine;
import engine.sheetmanager.SheetManager;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import utils.ServletUtils;

import java.io.IOException;

@WebServlet(name = "Add Range Servlet", urlPatterns = "/addRange")
public class AddRangeServlet extends HttpServlet {

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Engine engine = ServletUtils.getEngine(request.getServletContext());
        Gson gson = new Gson();

        String rangeName = request.getParameter("rangeName");
        String coordinates = request.getParameter("coordinates");

        if (rangeName == null || coordinates == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing range name or coordinates");
            return;
        }

        try {
            SheetManager sheetManager = engine.getCurrentSheet();
            sheetManager.addRangeToSheet(rangeName, coordinates);
            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().write(gson.toJson("Range added successfully"));
        } catch (Exception e) {

            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Failed to add range: " + e.getMessage());
        }
    }
}
