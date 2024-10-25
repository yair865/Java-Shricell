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

@WebServlet(name = "Remove Range Servlet", urlPatterns = "/removeRange")
public class RemoveRangeServlet extends HttpServlet {

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Engine engine = ServletUtils.getEngine(request.getServletContext());
        Gson gson = new Gson();

        String selectedRange = request.getParameter("range");

        if (selectedRange == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing selected range parameter");
            return;
        }

        try {
            SheetManager sheetManager = engine.getCurrentSheet();
            sheetManager.removeRangeFromSheet(selectedRange);
            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().write(gson.toJson("Range removed successfully"));
        } catch (Exception e) {
            // Handle exceptions, such as range not found or invalid range
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Failed to remove range: " + e.getMessage());
        }
    }
}
