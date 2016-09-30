package org.trams.sicbang.web.controller.anonymous;

import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.trams.sicbang.model.entity.User;
import org.trams.sicbang.web.controller.AbstractController;

import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Created by voncount on 4/6/16.
 */
@Controller(value = "anonymousControllerHome")
public class ControllerHome extends AbstractController {

    final String BASE_TEMPLATE = "web/content/";

    @RequestMapping(value = "/", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
    public String index() throws IOException {

        Authentication auth = serviceAuthorized.isAuthenticated();
        if(auth != null) {
            UserDetails userDetails = (UserDetails) auth.getPrincipal();
            User user = serviceUser.findUserByEmail(userDetails.getUsername());
            HttpSession session = httpRequest.getSession();
            System.out.println("User Type: " + user.getType().name());
            session.setAttribute("type", user.getType().name());
        }
        return "web/index";
    }


}
