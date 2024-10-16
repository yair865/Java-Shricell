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
import utils.SessionUtils;

import java.io.IOException;
import java.io.InputStream;

@WebServlet(name = "Load File Servlet", urlPatterns = "/load")
@MultipartConfig
public class LoadFileServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

        Engine engine = ServletUtils.getEngine(getServletContext());
        Part filePart = request.getPart("file");

        if (filePart == null) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("Missing file.");
            return;
        }

        String usernameFromSession = SessionUtils.getUsername(request);

        try (InputStream fileContent = filePart.getInputStream()) {
            engine.addSheet(fileContent, usernameFromSession);
            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().write("File loaded successfully.");
        } catch (IllegalArgumentException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write(e.getMessage());
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("Failed to load the sheet: " + e.getMessage());
        }
    }
}
