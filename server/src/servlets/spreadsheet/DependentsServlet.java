package servlets.spreadsheet;

import com.google.gson.Gson;
import dto.dtoPackage.SpreadsheetDTO;
import engine.engineimpl.Engine;
import engine.sheetimpl.cellimpl.coordinate.Coordinate;
import engine.sheetimpl.cellimpl.coordinate.CoordinateFactory;
import engine.sheetmanager.SheetManager;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import utils.ServletUtils;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet(name = "Get Dependents Servlet", urlPatterns = "/dependents")
public class DependentsServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Engine engine = ServletUtils.getEngine(request.getServletContext());

        String cellId = request.getParameter("cellId");
        if (cellId == null || cellId.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        List<Coordinate> dependents = getDependentsFromEngine(cellId , engine);
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        out.print(new Gson().toJson(dependents));
        out.flush();
    }

    private List<Coordinate> getDependentsFromEngine(String cellId , Engine engine) {
        SheetManager sheetManager = engine.getCurrentSheet();
        SpreadsheetDTO spreadsheetDTO = sheetManager.getSpreadsheetState();
        return spreadsheetDTO.dependenciesAdjacencyList().get(CoordinateFactory.createCoordinate(cellId));
    }
}