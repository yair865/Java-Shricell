package servlets.spreadsheet;

import com.google.gson.Gson;
import constant.Constants;
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

@WebServlet(name = "Remove Range Servlet", urlPatterns = "/removeRange")
public class RemoveRangeServlet extends HttpServlet {

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws IOException {
        int clientVersion;
        Engine engine = ServletUtils.getEngine(request.getServletContext());
        String sheetName = SessionUtils.getSheetName(request);
        String userName = SessionUtils.getUsername(request);


        String selectedRange = request.getParameter("range");

        if (selectedRange == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing selected range parameter");
            return;
        }

        try {
            clientVersion = Integer.parseInt(request.getParameter("clientVersion"));
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid client version.");
            return;
        }

        try {
            engine.removeRangeFromSheet(selectedRange, userName, sheetName, clientVersion);
            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().write(Constants.GSON_INSTANCE.toJson("Range removed successfully"));
        }catch (OutdatedVersionException e){
            sendErrorResponse(response,e.getMessage());
        } catch (Exception e) {
            sendErrorResponse(response,e.getMessage());
        }
    }
}
