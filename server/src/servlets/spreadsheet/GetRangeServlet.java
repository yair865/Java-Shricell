package servlets.spreadsheet;

import constant.Constants;
import engine.engineimpl.Engine;
import dto.dtoPackage.coordinate.Coordinate;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import utils.ServletUtils;
import utils.SessionUtils;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet(name = "Get Range Servlet" , urlPatterns = "/getRange")
public class GetRangeServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Engine engine = ServletUtils.getEngine(req.getServletContext());
        String sheetName = SessionUtils.getSheetName(req);
        String userName = SessionUtils.getUsername(req);
        String range = req.getParameter("rangeName");

        if (range == null) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        List<Coordinate> rangeCells = engine.getRangeByName(range , sheetName , userName);
        resp.setContentType("application/json");
        PrintWriter out = resp.getWriter();
        out.print(Constants.GSON_INSTANCE.toJson(rangeCells));
        out.flush();
    }
}
