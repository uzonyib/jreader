package jreader.web.controller;

import java.io.IOException;
import java.security.Principal;

import javax.servlet.http.HttpServletResponse;

import jreader.services.UserService;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class PageController {
	
	private UserService userService;
	private com.google.appengine.api.users.UserService googleUserService;
	
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public void getMainPage(HttpServletResponse response) throws IOException {
		response.sendRedirect("reader");
	}
	
	@RequestMapping(value = "/reader", method = RequestMethod.GET)
	public ModelAndView getHomePage(HttpServletResponse response, Principal principal) throws IOException {
		userService.register(principal.getName());
		ModelAndView modelAndView = new ModelAndView("reader");
		modelAndView.addObject("logoutUrl", googleUserService.createLogoutURL("/"));
		return modelAndView;
	}

	public UserService getUserService() {
		return userService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	public com.google.appengine.api.users.UserService getGoogleUserService() {
		return googleUserService;
	}

	public void setGoogleUserService(
			com.google.appengine.api.users.UserService googleUserService) {
		this.googleUserService = googleUserService;
	}
	
}
