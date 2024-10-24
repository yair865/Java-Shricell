package servlets.spreadsheet;

import com.google.gson.Gson;
import dto.dtoPackage.SpreadsheetDTO;
import engine.engineimpl.Engine;
import engine.sheetmanager.SheetManager;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import utils.ServletUtils;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.List;

@WebServlet(name = "Filter Servlet", urlPatterns = "/filter")
public class FilterServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Engine engine = ServletUtils.getEngine(request.getServletContext());
        Gson gson = new Gson();

        String selectedColumnParam = request.getParameter("selectedColumn");
        String filterArea = request.getParameter("filterArea");
        String selectedValuesParam = request.getParameter("selectedValues");

        if (selectedColumnParam == null || filterArea == null || selectedValuesParam == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing parameters");
            return;
        }

        Character selectedColumn = selectedColumnParam.charAt(0); // Convert to Character
        List<String> selectedValues = Arrays.asList(selectedValuesParam.split(","));

        try {
            SheetManager sheetManager = engine.getCurrentSheet();
            SpreadsheetDTO filteredSheet = sheetManager.filterSheet(selectedColumn, filterArea, selectedValues);

            response.setContentType("application/json");
            PrintWriter out = response.getWriter();
            out.print(gson.toJson(filteredSheet));
            out.flush();
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error filtering the sheet: " + e.getMessage());
        }
    }
}
