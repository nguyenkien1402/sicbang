package org.trams.sicbang.web.controller.member;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.trams.sicbang.model.entity.Attachment;
import org.trams.sicbang.model.entity.Estate;
import org.trams.sicbang.model.entity.Recent;
import org.trams.sicbang.model.entity.User;
import org.trams.sicbang.model.form.FormEstate;
import org.trams.sicbang.model.form.FormRecent;
import org.trams.sicbang.service.implement.ServiceAuthorized;
import org.trams.sicbang.web.controller.AbstractController;

import java.io.IOException;
import java.util.Collection;

/**
 * Created by DinhTruong on 9/28/2016.
 */
@Controller(value = "anonymousControllerViewHistory")
@RequestMapping(value = "/member/lately")
public class ControllerRecents extends AbstractController {

    final String BASE_TEMPLATE = "web/content/";

    /**
     * Redirect to lately
     * @param form
     * @param map
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
    public String lately(@ModelAttribute FormRecent form, ModelMap map) throws IOException {
        isSession();
        User user = (User) httpRequest.getSession().getAttribute("USER_SESSION");
        form.setUserId(user.getId().toString());
        Page<Recent> recents = serviceRecent.filter(form);
        map.put("recents",recents);
        return BASE_TEMPLATE + "optional/lately";
    }


}

