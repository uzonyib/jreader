package jreader.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import jreader.services.UserAdminService;

@Controller
@RequestMapping(value = "/admin")
public class AdminController {

    private final UserAdminService userAdminService;

    private final int pageSize;

    @Autowired
    public AdminController(final UserAdminService userAdminService, @Value("${admin.users.pageSize}") final int pageSize) {
        this.userAdminService = userAdminService;
        this.pageSize = pageSize;
    }

    @RequestMapping(value = "/users", method = RequestMethod.GET)
    public ModelAndView getUsersPage(@RequestParam(required = false) final Integer page) {
        final int pageIndex = page == null ? 0 : page.intValue();
        final int offset = pageIndex * pageSize;
        final ModelAndView modelAndView = new ModelAndView("admin/users");
        modelAndView.addObject("users", userAdminService.list(offset, pageSize));
        return modelAndView;
    }

    @RequestMapping(value = "/users", method = RequestMethod.POST)
    public String updateRole(@RequestParam final String username, @RequestParam final String role) {
        userAdminService.updateRole(username, role);
        return "redirect:/admin/users";
    }

}
