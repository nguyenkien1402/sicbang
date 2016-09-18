package org.trams.sicbang.web.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.trams.sicbang.service.implement.ServiceAuthorized;
import org.trams.sicbang.web.controller.AbstractController;

import java.io.IOException;

/**
 * Created by voncount on 4/6/16.
 */
@Controller(value = "adminControllerHome")
@RequestMapping(value = "/admin")
public class ControllerHome extends AbstractController {

    @Autowired
    private ServiceAuthorized serviceAuthorized;

    @RequestMapping(method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
    public String index() throws IOException {
        if (serviceAuthorized.isAuthenticated() != null) {
            if (serviceAuthorized.isAdmin()) {
                return "redirect:/admin/user";
            }
            return "redirect:/";
        }
        return "admin/index";
    }

}
