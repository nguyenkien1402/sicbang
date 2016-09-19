package org.trams.sicbang.web.controller.admin;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.trams.sicbang.model.entity.Notice;
import org.trams.sicbang.model.exception.FormError;
import org.trams.sicbang.model.form.FormNotice;
import org.trams.sicbang.web.controller.AbstractController;

/**
 * Created by voncount on 15/04/2016.
 */
@Controller(value = "adminControllerNotice")
@RequestMapping(value = "/admin/notice")
public class ControllerNotice extends AbstractController {

    final String BASE_URL = "/admin/notice/";
    final String BASE_TEMPLATE = "admin/content/notice/";

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
        return BASE_TEMPLATE + "list";
    }

    /**
     *
     * @param form
     * @param map
     * @return
     */
    @RequestMapping(value = "/{noticeId}", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
    public String detail(
            @ModelAttribute FormNotice form,
            ModelMap map) {
        Notice notice = serviceNotice.findOne(form);
        map.put("notice", notice);
        return BASE_TEMPLATE + "update";
    }

    /**
     *
     * @param form
     * @return
     */
    @RequestMapping(value = "/{noticeId}", method = RequestMethod.POST, produces = MediaType.TEXT_HTML_VALUE)
    public ResponseEntity update(
            @ModelAttribute FormNotice form) {
        serviceNotice.update(form);
        return new ResponseEntity(HttpStatus.OK);
    }

    /**
     *
     * @param form
     * @param map
     * @return
     */
    @RequestMapping(value = "/{noticeId}", method = RequestMethod.DELETE, produces = MediaType.TEXT_HTML_VALUE)
    public ResponseEntity delete(
            @ModelAttribute FormNotice form,
            ModelMap map) {
        serviceNotice.delete(form);
        return new ResponseEntity(HttpStatus.OK);
    }

    /**
     * Create
     * @param form
     * @return
     */
    @RequestMapping(value = "/create", method = {RequestMethod.GET, RequestMethod.POST})
    public Object create(
            @ModelAttribute FormNotice form
    ) {
        switch (getRequestMethod()) {
            case POST:
                FormError error = validationNotice.validateCreate(form);
                if (error != null) {
                    return new ResponseEntity(error, HttpStatus.BAD_REQUEST);
                }
                // create service
                serviceNotice.create(form);
                return new ResponseEntity(HttpStatus.OK);
            case GET:
            default:
                return BASE_TEMPLATE + "create";
        }
    }

}
