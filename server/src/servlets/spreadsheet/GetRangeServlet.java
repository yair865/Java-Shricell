package servlets.spreadsheet;

import com.google.gson.Gson;
import engine.engineimpl.Engine;
import engine.sheetimpl.cellimpl.coordinate.Coordinate;
import engine.sheetmanager.SheetManager;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import utils.ServletUtils;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet(name = "Get Range Servlet" , urlPatterns = "/getRange")
public class GetRangeServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        Engine engine = ServletUtils.getEngine(req.getServletContext());
        String range = req.getParameter("rangeName");

        if (range == null) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        SheetManager sheetManager = engine.getCurrentSheet();
        List<Coordinate> rangeCells = sheetManager.getRangeByName(range);
        resp.setContentType("application/json");
        PrintWriter out = resp.getWriter();
        out.print(new Gson().toJson(rangeCells));
        out.flush();
    }
}
