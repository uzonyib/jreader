package jreader.web.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import com.google.appengine.api.users.UserService;

@Controller
public class PageController {

    private final UserService googleUserService;

    private final String appVersion;

    @Autowired
    public PageController(final UserService googleUserService, @Value("${version}") final String appVersion) {
        this.googleUserService = googleUserService;
        this.appVersion = appVersion;
    }

    @GetMapping("/")
    public void getMainPage(final HttpServletResponse response) throws IOException {
        response.sendRedirect("/reader");
    }

    @GetMapping("/reader")
    public ModelAndView getHomePage() {
        final ModelAndView modelAndView = new ModelAndView("reader");
        modelAndView.addObject("logoutUrl", googleUserService.createLogoutURL("/"));
        modelAndView.addObject("appVersion", appVersion);
        return modelAndView;
    }

    @GetMapping("/forbidden")
    public ModelAndView getForbiddenPage() {
        return new ModelAndView("forbidden");
    }

}
