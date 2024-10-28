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

@WebServlet(name = "Set Background Color Servlet", urlPatterns = "/backgroundColor")
public class BackGroundColorServlet extends HttpServlet {

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws IOException {
        int clientVersion;
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
            clientVersion = Integer.parseInt(request.getParameter("clientVersion"));
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid client version.");
            return;
        }

        try {
            engine.setSingleCellBackGroundColor(cellId, color, sheetName, userName, clientVersion);
            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().write(gson.toJson("Background color updated successfully"));
        } catch (OutdatedVersionException e){
            sendErrorResponse(response,e.getMessage());
        } catch (Exception e) {
            sendErrorResponse(response,e.getMessage());
        }
    }
}
