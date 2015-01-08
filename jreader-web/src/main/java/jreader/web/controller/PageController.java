package jreader.web.controller;

import java.io.IOException;
import java.security.Principal;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletResponse;

import jreader.services.ServiceException;
import jreader.services.UserService;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class PageController {
	
	private static final Logger LOG = Logger.getLogger(PageController.class.getName());
	
	private UserService userService;
	private com.google.appengine.api.users.UserService googleUserService;
	
	public PageController(UserService userService, com.google.appengine.api.users.UserService googleUserService) {
		this.userService = userService;
		this.googleUserService = googleUserService;
	}

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public void getMainPage(HttpServletResponse response) throws IOException {
		response.sendRedirect("reader");
	}
	
	@RequestMapping(value = "/reader", method = RequestMethod.GET)
	public ModelAndView getHomePage(HttpServletResponse response, Principal principal) throws IOException {
		try {
			userService.register(principal.getName());
			LOG.info("User registered: " + principal.getName());
		} catch(ServiceException e) {
			LOG.info("User found: " + principal.getName());
		}
		ModelAndView modelAndView = new ModelAndView("reader");
		modelAndView.addObject("logoutUrl", googleUserService.createLogoutURL("/"));
		return modelAndView;
	}
	
}
