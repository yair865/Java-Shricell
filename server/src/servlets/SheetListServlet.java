package servlets;

import com.google.gson.Gson;
import dto.dtoPackage.SheetInfoDTO;
import engine.engineimpl.Engine;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import utils.ServletUtils;
import utils.SessionUtils;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet(name = "SheetListServlet" , urlPatterns = "/sheetList")
public class SheetListServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String usernameFromSession = SessionUtils.getUsername(request);
        response.setContentType("application/json");
        try(PrintWriter out = response.getWriter()) {
            Gson gson = new Gson();
            Engine engine = ServletUtils.getEngine(request.getServletContext());
            List<SheetInfoDTO> sheets = engine.getSheets(usernameFromSession);
            String json = gson.toJson(sheets);
            out.println(json);
            out.flush();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
