package org.trams.sicbang.web.controller.anonymous;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.trams.sicbang.web.controller.AbstractController;

import java.io.IOException;

/**
 * Created by voncount on 4/6/16.
 */
@Controller(value = "anonymousControllerHome")
public class ControllerHome extends AbstractController {

    final String BASE_TEMPLATE = "web/content/";

    @RequestMapping(value = "/", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
    public String index() throws IOException {
        return "web/index";
    }


}
