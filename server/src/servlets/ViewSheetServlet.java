package servlets;

import com.google.gson.Gson;
import dto.dtoPackage.SpreadsheetDTO;
import engine.engineimpl.Engine;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import utils.ServletUtils;

import java.io.IOException;

@WebServlet(name = "View Sheet" , urlPatterns = "/view")
public class ViewSheetServlet  extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

        Engine engine = ServletUtils.getEngine(getServletContext());
        Gson gson = new Gson();

        String sheetName = request.getParameter("sheetName");

        if (sheetName == null || sheetName.isEmpty()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Sheet name is required");
            return;
        }

        try{
            SpreadsheetDTO spreadsheetDTO = engine.getSheet(sheetName).getSpreadsheetState();

            if (spreadsheetDTO == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Spreadsheet not found");
                return;
            }

            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            String jsonResponse = gson.toJson(spreadsheetDTO);
            response.getWriter().write(jsonResponse);
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "An error occurred while processing the request");
        }
    }
}
