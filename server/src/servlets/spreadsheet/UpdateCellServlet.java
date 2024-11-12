package servlets.spreadsheet;

import com.google.gson.Gson;
import constant.Constants;
import dto.dtoPackage.CellDTO;
import engine.engineimpl.Engine;
import engine.exception.OutdatedVersionException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import utils.ServletUtils;
import utils.SessionUtils;

import java.io.IOException;
import java.util.List;

import static utils.ServletUtils.sendErrorResponse;

@WebServlet(name = "Update Cell Servlet", urlPatterns = "/updateCell")
public class UpdateCellServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        int clientVersion;
        Engine engine = ServletUtils.getEngine(request.getServletContext());
        String sheetName = SessionUtils.getSheetName(request);
        String userName = SessionUtils.getUsername(request);

        String cellId = request.getParameter("cellId");
        String newValue = request.getParameter("newValue");

        try {
            clientVersion = Integer.parseInt(request.getParameter("clientVersion"));
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid client version.");
            return;
        }

        if (cellId == null || cellId.isEmpty()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing or invalid parameters.");
            return;
        }

        try {
            List<CellDTO> cellsThatHaveChanged = engine.updateCell(cellId, newValue, userName, sheetName, clientVersion);

            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");

            String jsonResponse = Constants.GSON_INSTANCE.toJson(cellsThatHaveChanged);
            response.getWriter().write(jsonResponse);
        }catch (OutdatedVersionException e) {
            sendErrorResponse(response,e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            sendErrorResponse(response,e.getMessage());
        }
    }
}
