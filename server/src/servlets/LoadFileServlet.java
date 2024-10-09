package servlets;

import engine.engineimpl.Engine;
import engine.engineimpl.EngineImpl;
import engine.sheetmanager.SheetManager;
import engine.sheetmanager.SheetManagerImpl;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet(name = "Load File Servlet", urlPatterns = "/load")
public class LoadFileServlet extends HttpServlet {

    private Engine engine;

    public LoadFileServlet() {
         engine = EngineImpl.getInstance();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String sheetName = request.getParameter("sheetName");
        String filePath = request.getParameter("filePath");

        if (sheetName == null || filePath == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing sheet name or file path.");
            return;
        }

        try {
            // Create a new SheetManager instance for the new sheet
            SheetManager sheetManager = new SheetManagerImpl();
            engine.addSheet(sheetName, sheetManager, filePath);
            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().write("Sheet '" + sheetName + "' loaded successfully from '" + filePath + "'.");

        } catch (IllegalArgumentException e) {
            // Handle specific exceptions (like sheet already exists or loading issues)
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
        } catch (Exception e) {
            // Handle general exceptions
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Failed to load the sheet: " + e.getMessage());
        }
    }
}
