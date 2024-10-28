package servlets.spreadsheet;

import com.google.gson.Gson;
import engine.engineimpl.Engine;
import engine.exception.OutdatedVersionException;
import engine.sheetmanager.SheetManager;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import utils.ServletUtils;
import utils.SessionUtils;

import java.io.IOException;

import static utils.ServletUtils.sendErrorResponse;

@WebServlet(name = "Add Range Servlet", urlPatterns = "/addRange")
public class AddRangeServlet extends HttpServlet {

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws IOException {
        int clientVersion;
        Engine engine = ServletUtils.getEngine(request.getServletContext());
        String sheetName = SessionUtils.getSheetName(request);
        String userNameFromSession = SessionUtils.getUsername(request);
        Gson gson = new Gson();

        String rangeName = request.getParameter("rangeName");
        String coordinates = request.getParameter("coordinates");

        if (rangeName == null || coordinates == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing range name or coordinates");
            return;
        }

        try {
            clientVersion = Integer.parseInt(request.getParameter("clientVersion"));
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid client version.");
            return;
        }

        try {
            engine.addRangeToSheet(rangeName, coordinates, sheetName, userNameFromSession,clientVersion);
            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().write(gson.toJson("Range added successfully"));
        }catch (OutdatedVersionException e) {
            sendErrorResponse(response,e.getMessage());
        } catch (Exception e) {
            sendErrorResponse(response,e.getMessage());
        }
    }
}
