package org.trams.sicbang.web.controller.anonymous;

import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.trams.sicbang.model.entity.Notice;
import org.trams.sicbang.model.form.FormNotice;
import org.trams.sicbang.web.controller.AbstractController;

/**
 * Created by voncount on 15/04/2016.
 */
@Controller(value = "anonymousControllerNotice")
@RequestMapping(value = "/notice")
public class ControllerNotice extends AbstractController {

    final String BASE_URL = "/notice/";
    final String BASE_TEMPLATE = "web/content/notice/";

    /**
     * Filter
     * @param map
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
    public String index(
            @ModelAttribute FormNotice form,
            ModelMap map) {
        Page<Notice> notices = serviceNotice.filter(form);
        map.put("items", notices);
        return BASE_TEMPLATE + "event";
    }


}
