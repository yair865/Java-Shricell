package utils;

import engine.usermanager.UserManager;
import jakarta.servlet.ServletContext;

public class ServletUtils {

    private static final String USER_MANAGER_ATTRIBUTE_NAME = "userManager";

    private static final Object userManagerLock = new Object();

    public static UserManager getUserManager(ServletContext servletContext) {

        synchronized (userManagerLock) {
            if (servletContext.getAttribute(USER_MANAGER_ATTRIBUTE_NAME) == null) {
                servletContext.setAttribute(USER_MANAGER_ATTRIBUTE_NAME, new UserManager());
            }
        }
        return (UserManager) servletContext.getAttribute(USER_MANAGER_ATTRIBUTE_NAME);
    }
}