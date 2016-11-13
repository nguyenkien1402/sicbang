package org.trams.sicbang.web.controller.member;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.trams.sicbang.model.entity.User;
import org.trams.sicbang.model.form.FormEstate;
import org.trams.sicbang.web.controller.AbstractController;

/**
 * Created by DinhTruong on 9/8/2016.
 */
@Controller(value = "anonymousControllerPolicy")
public class ControllerPolicy extends AbstractController {

    final String BASE_URL = "/policy/";
    final String BASE_TEMPLATE = "web/content/policy/";


    @RequestMapping(value="/privacy-policy",method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
    public String privacy(ModelMap map) {

        return BASE_TEMPLATE + "privacy";
    }
    @RequestMapping(value="/use-policy",method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
    public String use(ModelMap map) {

        return BASE_TEMPLATE + "use";
    }

    @RequestMapping(value="/lbs-policy",method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
    public String lbs(ModelMap map) {

        return BASE_TEMPLATE + "lbs";
    }
    @RequestMapping(value="/paid",method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
    public String paid() {

        return BASE_TEMPLATE + "paid";
    }


}
