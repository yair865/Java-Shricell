package servlets.spreadsheet;

import com.google.gson.Gson;
import dto.dtoPackage.CellDTO;
import engine.engineimpl.Engine;
import engine.sheetmanager.SheetManager;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import utils.ServletUtils;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "Update Cell Servlet", urlPatterns = "/updateCell")
public class UpdateCellServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        Engine engine = ServletUtils.getEngine(request.getServletContext());
        Gson gson = new Gson();

        String cellId = request.getParameter("cellId");
        String newValue = request.getParameter("newValue");

        if (cellId == null || cellId.isEmpty()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing or invalid parameters.");
            return;
        }

        try {
            SheetManager sheetManager = engine.getCurrentSheet();
            sheetManager.updateCell(cellId, newValue);

            List<CellDTO> cellsThatHaveChanged = sheetManager.getCellsThatHaveChanged();

            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");

            String jsonResponse = gson.toJson(cellsThatHaveChanged);
            response.getWriter().write(jsonResponse);

        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "An error occurred while updating the cell.");
        }
    }
}
