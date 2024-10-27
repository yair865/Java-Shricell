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

@WebServlet(name = "Set Background Color Servlet", urlPatterns = "/backgroundColor")
public class BackGroundColorServlet extends HttpServlet {

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Engine engine = ServletUtils.getEngine(request.getServletContext());
        String sheetName = SessionUtils.getSheetName(request);
        String userName = SessionUtils.getUsername(request);

        Gson gson = new Gson();

        String cellId = request.getParameter("cellId");
        String color = request.getParameter("color");


        if (cellId == null || color == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing parameters");
            return;
        }

        try {
            engine.setSingleCellBackGroundColor(cellId, color,sheetName,userName);
            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().write(gson.toJson("Background color updated successfully"));
        } catch (Exception e) {
            // Handle exceptions (e.g., cell not found, invalid color)
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Failed to update background color: " + e.getMessage());
        }
    }
}
