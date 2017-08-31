package jreader.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

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
    public RedirectView mainPage() {
        return new RedirectView("/reader");
    }

    @GetMapping("/reader")
    public ModelAndView homePage() {
        final ModelAndView modelAndView = new ModelAndView("reader");
        modelAndView.addObject("logoutUrl", googleUserService.createLogoutURL("/"));
        modelAndView.addObject("appVersion", appVersion);
        return modelAndView;
    }

    @GetMapping("/forbidden")
    public ModelAndView forbiddenPage() {
        return new ModelAndView("forbidden");
    }

}
