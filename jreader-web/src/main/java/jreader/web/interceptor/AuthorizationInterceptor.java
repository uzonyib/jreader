package jreader.web.interceptor;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import jreader.domain.Role;
import jreader.services.UserService;

public class AuthorizationInterceptor extends HandlerInterceptorAdapter {

    private UserService userService;
    private com.google.appengine.api.users.UserService googleUserService;
    private List<Role> authorizedRoles;

    public AuthorizationInterceptor(final UserService userService, final com.google.appengine.api.users.UserService googleUserService,
            final List<Role> authorizedRoles) {
        this.userService = userService;
        this.googleUserService = googleUserService;
        this.authorizedRoles = authorizedRoles;
    }

    @Override
    public boolean preHandle(final HttpServletRequest request, final HttpServletResponse response, final Object handler) throws Exception {
        if (googleUserService.isUserLoggedIn()) {
            final String username = googleUserService.getCurrentUser().getEmail();
            if (!userService.isRegistered(username)) {
                userService.register(username, googleUserService.isUserAdmin() ? Role.ADMIN : Role.UNAUTHORIZED);
            }
    
            final Role role = userService.getRole(username);
            if (authorizedRoles.contains(role)) {
                return true;
            }
        }
        
        response.sendRedirect("/forbidden");
        return false;
    }

}
