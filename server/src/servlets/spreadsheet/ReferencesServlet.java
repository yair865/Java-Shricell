package servlets.spreadsheet;

import constant.Constants;
import dto.dtoPackage.SpreadsheetDTO;
import engine.engineimpl.Engine;
import dto.dtoPackage.coordinate.Coordinate;
import dto.dtoPackage.coordinate.CoordinateFactory;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import utils.ServletUtils;
import utils.SessionUtils;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet(name = "Get References Servlet", urlPatterns = "/references")
public class ReferencesServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Engine engine = ServletUtils.getEngine(request.getServletContext());
        String sheetName = SessionUtils.getSheetName(request);
        String userName = SessionUtils.getUsername(request);

        String cellId = request.getParameter("cellId");
        if (cellId == null || cellId.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        List<Coordinate> references = getReferencesFromEngine(cellId , engine , sheetName , userName); // Your method to fetch references
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        out.print(Constants.GSON_INSTANCE.toJson(references));
        out.flush();
    }

    private List<Coordinate> getReferencesFromEngine(String cellId , Engine engine , String sheetName , String userName) {
        SpreadsheetDTO spreadsheetDTO = engine.getSpreadsheetState(cellId, sheetName , userName);

        return spreadsheetDTO.referencesAdjacencyList().get(CoordinateFactory.createCoordinate(cellId));
    }
}