package servlets.dashboard;

import com.google.gson.Gson;
import constant.Constants;
import dto.dtoPackage.PermissionInfoDTO;
import engine.engineimpl.Engine;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import utils.ServletUtils;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet(name = "Permissions List", urlPatterns = "/permissionList")
public class PermissionListServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");

        String sheetName = request.getParameter("sheetName");

        try (PrintWriter out = response.getWriter()) {

            Engine engine = ServletUtils.getEngine(request.getServletContext());

            List<PermissionInfoDTO> permissions;
            if (sheetName != null && !sheetName.isEmpty()) {
                permissions = engine.getPermissions(sheetName);
            } else {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                out.println("{\"error\":\"Missing or invalid sheetName parameter.\"}");
                return;
            }

            String json = Constants.GSON_INSTANCE.toJson(permissions);
            out.println(json);
            out.flush();
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            try (PrintWriter out = response.getWriter()) {
                out.println("{\"error\":\"Internal server error.\"}");
                out.flush();
            }
        }
    }
}

