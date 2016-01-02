package jreader.web.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.google.appengine.api.users.UserService;

@Controller
public class PageController {

    private final UserService googleUserService;
    private final String appVersion;

    public PageController(final UserService googleUserService, final String appVersion) {
        this.googleUserService = googleUserService;
        this.appVersion = appVersion;
    }

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public void getMainPage(final HttpServletResponse response) throws IOException {
        response.sendRedirect("reader");
    }

    @RequestMapping(value = "/reader", method = RequestMethod.GET)
    public ModelAndView getHomePage() {
        final ModelAndView modelAndView = new ModelAndView("reader");
        modelAndView.addObject("logoutUrl", googleUserService.createLogoutURL("/"));
        modelAndView.addObject("appVersion", appVersion);
        return modelAndView;
    }
    
    @RequestMapping(value = "/forbidden", method = RequestMethod.GET)
    public ModelAndView getForbiddenPage() {
        return new ModelAndView("forbidden");
    }

}
