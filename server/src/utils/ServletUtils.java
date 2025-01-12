package utils;

import com.google.gson.JsonObject;
import engine.engineimpl.Engine;
import engine.engineimpl.EngineImpl;
import engine.usermanager.UserManager;
import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class ServletUtils {

    private static final String USER_MANAGER_ATTRIBUTE_NAME = "userManager";

    private static final String ENGINE_ATTRIBUTE_NAME = "engine";

    private static final Object userManagerLock = new Object();

    private static final Object engineLock = new Object();

    public static UserManager getUserManager(ServletContext servletContext) {

        synchronized (userManagerLock) {
            if (servletContext.getAttribute(USER_MANAGER_ATTRIBUTE_NAME) == null) {
                servletContext.setAttribute(USER_MANAGER_ATTRIBUTE_NAME, new UserManager());
            }
        }
        return (UserManager) servletContext.getAttribute(USER_MANAGER_ATTRIBUTE_NAME);
    }

    public static Engine getEngine(ServletContext servletContext) {
        synchronized (engineLock) {
            if (servletContext.getAttribute(ENGINE_ATTRIBUTE_NAME) == null) {
                servletContext.setAttribute(ENGINE_ATTRIBUTE_NAME, new EngineImpl());
            }
        }
        return (Engine) servletContext.getAttribute(ENGINE_ATTRIBUTE_NAME);
    }

    public static void sendErrorResponse(HttpServletResponse response, String errorMessage) throws IOException {
        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);

        JsonObject errorResponse = new JsonObject();
        errorResponse.addProperty("error", errorMessage);

        response.getWriter().write(errorResponse.toString());
    }
}
