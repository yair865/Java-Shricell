package servlets.spreadsheet;

import com.google.gson.Gson;
import dto.dtoPackage.SpreadsheetDTO;
import engine.engineimpl.Engine;
import engine.sheetimpl.api.Spreadsheet;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import utils.ServletUtils;
import utils.SessionUtils;

import java.io.IOException;

@WebServlet(name = "Get Latest Version Servlet" , urlPatterns = "/latestVersion")
public class GetLatestVersionServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request , HttpServletResponse response) throws IOException{
        Engine engine = ServletUtils.getEngine(getServletContext());
        Gson gson = new Gson();
        String sheetName = SessionUtils.getSheetName(request);
        String userName = SessionUtils.getUsername(request);

        try {
            SpreadsheetDTO spreadsheet = engine.getLatestVersion(sheetName, userName);

            if (spreadsheet == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Spreadsheet not found");
                return;
            }

            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            String jsonResponse = gson.toJson(spreadsheet);

            response.getWriter().write(jsonResponse);
        }catch (Exception e) {
            e.printStackTrace();
            ServletUtils.sendErrorResponse(response, e.getMessage());
        }
    }
}
