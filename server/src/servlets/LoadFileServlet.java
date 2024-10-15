package servlets;

import engine.engineimpl.Engine;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import utils.ServletUtils;

import java.io.IOException;
import java.io.InputStream;

@WebServlet(name = "Load File Servlet", urlPatterns = "/load")
@MultipartConfig
public class LoadFileServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

        Engine engine = ServletUtils.getEngine(getServletContext());

        Part filePart = request.getPart("file");
        Part userNamePart = request.getPart("userName");

        if (filePart == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing file.");
            return;
        }

        if (userNamePart == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing user name.");
            return;
        }

        String userName = new String(userNamePart.getInputStream().readAllBytes());

        try (InputStream fileContent = filePart.getInputStream()) {
            engine.addSheet(fileContent, userName);
            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().write("Sheet loaded successfully by user '" + userName + "'.");

        } catch (IllegalArgumentException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Failed to load the sheet: " + e.getMessage());
        }
    }
}
