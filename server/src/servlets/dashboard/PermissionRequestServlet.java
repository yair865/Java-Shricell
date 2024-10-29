package servlets.dashboard;

import engine.engineimpl.Engine;
import dto.dtoPackage.PermissionType;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import utils.ServletUtils;
import utils.SessionUtils;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "Request Permission", urlPatterns = "/requestPermission")
public class PermissionRequestServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws  IOException {

        Engine engine = ServletUtils.getEngine(getServletContext());
        String usernameFromSession = SessionUtils.getUsername(request);
        String sheetName = request.getParameter("sheetName");
        String permissionTypeStr = request.getParameter("permissionType");

        if (sheetName == null || sheetName.isEmpty() || permissionTypeStr == null) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("Invalid request: missing parameters.");
            return;
        }

        PermissionType permissionType;
        try {
            permissionType = PermissionType.valueOf(permissionTypeStr);
            engine.requestPermission(usernameFromSession, permissionType, sheetName);
        } catch (IllegalArgumentException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("Invalid permission type.");
            return;
        }

        response.setStatus(HttpServletResponse.SC_OK);
        PrintWriter out = response.getWriter();
        out.write("Permission request for sheet \"" + sheetName + "\" with type \"" + permissionType + "\" processed successfully.");
        out.flush();
    }
}
