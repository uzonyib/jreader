package jreader.web.interceptor;

import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.google.appengine.api.users.UserService;

abstract class AppengineInterceptor extends HandlerInterceptorAdapter {

    private static final Logger LOG = Logger.getLogger(AppengineInterceptor.class.getName());

    private UserService userService;

    AppengineInterceptor(final UserService userService) {
        this.userService = userService;
    }

    @Override
    public boolean preHandle(final HttpServletRequest request, final HttpServletResponse response, final Object handler) {
        if (isAppengineAuthorized(request) || isUserAuthorized()) {
            return true;
        }

        response.setStatus(HttpStatus.FORBIDDEN.value());
        LOG.warning("Appengine request forbidden: " + request.getRequestURI());
        return false;
    }

    protected abstract boolean isAppengineAuthorized(HttpServletRequest request);

    protected boolean isUserAuthorized() {
        return userService.isUserLoggedIn() && userService.isUserAdmin();
    }

}
