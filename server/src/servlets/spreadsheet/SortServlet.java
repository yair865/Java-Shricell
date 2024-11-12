package servlets.spreadsheet;

import com.google.gson.Gson;
import constant.Constants;
import dto.dtoPackage.SpreadsheetDTO;
import engine.engineimpl.Engine;
import engine.sheetmanager.SheetManager;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import utils.ServletUtils;
import utils.SessionUtils;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@WebServlet(name = "Sort Servlet" , urlPatterns = "/sort")
public class SortServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Engine engine = ServletUtils.getEngine(request.getServletContext());
        String sheetName = SessionUtils.getSheetName(request);
        String userName = SessionUtils.getUsername(request);

        String cellsRange = request.getParameter("cellsRange");
        String selectedColumnsParam = request.getParameter("selectedColumns");

        if (cellsRange == null || selectedColumnsParam == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing parameters");
            return;
        }

        List<Character> selectedColumns = Arrays.stream(selectedColumnsParam.split(","))
                .map(c -> c.charAt(0))
                .collect(Collectors.toList());

        if (selectedColumns.isEmpty()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "No columns selected");
            return;
        }

        try {
            SpreadsheetDTO sortedSheet = engine.sort(cellsRange, selectedColumns, userName, sheetName);

            response.setContentType("application/json");
            PrintWriter out = response.getWriter();
            out.print(Constants.GSON_INSTANCE.toJson(sortedSheet));
            out.flush();
        } catch (Exception e) {
            ServletUtils.sendErrorResponse(response, e.getMessage());
        }
    }
}
