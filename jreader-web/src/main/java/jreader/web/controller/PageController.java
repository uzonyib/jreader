package jreader.web.controller;

import java.io.IOException;
import java.security.Principal;

import javax.servlet.http.HttpServletResponse;

import jreader.service.UserService;
import jreader.web.form.Login;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class PageController {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private com.google.appengine.api.users.UserService googleUserService;
	
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public void getMainPage(HttpServletResponse response) throws IOException {
		response.sendRedirect("reader");
	}

	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public ModelAndView getLoginPage() {
		return new ModelAndView("login", "command", new Login());
	}
	
	@RequestMapping(value = "/reader", method = RequestMethod.GET)
	public ModelAndView getHomePage(HttpServletResponse response, Principal principal) throws IOException {
		userService.register(principal.getName());
		ModelAndView modelAndView = new ModelAndView("reader");
		modelAndView.addObject("logoutUrl", googleUserService.createLogoutURL("/"));
		return modelAndView;
	}
	
}
