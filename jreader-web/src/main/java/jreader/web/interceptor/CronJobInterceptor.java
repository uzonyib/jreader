package jreader.web.interceptor;

import javax.servlet.http.HttpServletRequest;

import com.google.appengine.api.users.UserService;

public class CronJobInterceptor extends AppengineInterceptor {

    public CronJobInterceptor(final UserService userService) {
        super(userService);
    }

    protected boolean isAppengineAuthorized(final HttpServletRequest request) {
        return request.getHeader("X-AppEngine-Cron") != null;
    }

}
