package servlets.spreadsheet;

import engine.engineimpl.Engine;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import utils.ServletUtils;
import utils.SessionUtils;

import java.io.IOException;

@WebServlet(name = "Has New Version Servlet", urlPatterns = "/hasNewVersion")
public class HasNewVersionServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Engine engine = ServletUtils.getEngine(request.getServletContext());
        String sheetName = SessionUtils.getSheetName(request);
        String userName = SessionUtils.getUsername(request);

        String currentVersionParam = request.getParameter("version");
        int currentVersion = currentVersionParam != null ? Integer.parseInt(currentVersionParam) : -1;

        if (currentVersion == -1) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing or invalid version");
            return;
        }

        // Get the latest version
        int latestVersion = engine.getLatestVersionNumber(sheetName, userName);

        // Return the latest version number directly
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(String.valueOf(latestVersion)); // Send only the latest version
    }
}
