package servlets.spreadsheet;

import com.google.gson.Gson;
import constant.Constants;
import engine.engineimpl.Engine;
import engine.exception.OutdatedVersionException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import utils.ServletUtils;
import utils.SessionUtils;

import java.io.IOException;

import static utils.ServletUtils.sendErrorResponse;

@WebServlet(name = "Set Text Color Servlet", urlPatterns = "/textColor")
public class TextColorServlet extends HttpServlet {

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws IOException {
        int clientVersion;
        Engine engine = ServletUtils.getEngine(request.getServletContext());
        String sheetName = SessionUtils.getSheetName(request);
        String userName = SessionUtils.getUsername(request);


        String cellId = request.getParameter("cellId");
        String color = request.getParameter("color");

        try {
            clientVersion = Integer.parseInt(request.getParameter("clientVersion"));
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid client version.");
            return;
        }

        if (cellId == null || color == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing parameters");
            return;
        }

        try {
            engine.setSingleCellTextColor(cellId, color, userName, sheetName, clientVersion);
            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().write(Constants.GSON_INSTANCE.toJson("Text color updated successfully"));
        }catch (OutdatedVersionException e){
            sendErrorResponse(response,e.getMessage());
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Failed to update text color: " + e.getMessage());
        }
    }
}
