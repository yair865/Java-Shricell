package servlets.dashboard;

import engine.engineimpl.Engine;
import dto.dtoPackage.RequestStatus;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import utils.ServletUtils;
import utils.SessionUtils;

import java.io.IOException;

@WebServlet(name = "Response Permission Request", urlPatterns = "/responsePermissionRequest")
public class PermissionResponseServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Engine engine = ServletUtils.getEngine(getServletContext());
        String usernameFromSession = SessionUtils.getUsername(request);

        String sheetName = request.getParameter("sheetName");
        String requestIdParam = request.getParameter("requestId");
        String requestStatusParam = request.getParameter("RequestStatus");

        if (isInvalid(sheetName)) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "sheetName is required");
            return;
        }

        int requestId = Integer.parseInt(requestIdParam);

        try {
            RequestStatus requestStatus = RequestStatus.valueOf(requestStatusParam.toUpperCase());

            if (requestId == -1) {
                response.setStatus(HttpServletResponse.SC_OK);
                response.getWriter().write("Permission for " + usernameFromSession + " on sheet " + sheetName + " is OWNER and cannot be changed.");
                return;
            }

            engine.updatePermissions(usernameFromSession, sheetName, requestId, requestStatus);
            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().write("Request " + requestId + " was " + requestStatus.toString().toLowerCase() + " for sheet " + sheetName);

        } catch (IllegalArgumentException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid RequestStatus value");
        }
    }

    private boolean isInvalid(String parameter) {
        return parameter == null || parameter.trim().isEmpty();
    }
}
