package servlets.spreadsheet;

import com.google.gson.Gson;
import engine.engineimpl.Engine;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import utils.ServletUtils;

import java.io.IOException;

@WebServlet(name = "Set Text Color Servlet", urlPatterns = "/textColor")
public class TextColorServlet extends HttpServlet {

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Engine engine = ServletUtils.getEngine(request.getServletContext());
        Gson gson = new Gson();

        String cellId = request.getParameter("cellId");
        String color = request.getParameter("color");

        if (cellId == null || color == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing parameters");
            return;
        }

        try {
            engine.getCurrentSheet().setSingleCellTextColor(cellId, color);
            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().write(gson.toJson("Text color updated successfully"));
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Failed to update text color: " + e.getMessage());
        }
    }
}
