package jreader.web.controller.appengine;

import javax.servlet.http.HttpServletRequest;

import com.google.appengine.api.users.UserService;

abstract class AppengineController {
    
    private UserService userService;

    public AppengineController(final UserService userService) {
        this.userService = userService;
    }
    
    protected boolean isAppengineCronRequest(final HttpServletRequest request) {
        return request.getHeader("X-AppEngine-Cron") != null;
    }
    
    protected boolean isAppengineTaskRequest(final HttpServletRequest request) {
        return request.getHeader("X-AppEngine-TaskName") != null;
    }
    
    protected boolean isUserAuthorized() {
        return userService.isUserLoggedIn() && userService.isUserAdmin();
    }

}
