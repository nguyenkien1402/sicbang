package org.trams.sicbang.web.controller.member;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.trams.sicbang.model.dto.BaseFormSearch;
import org.trams.sicbang.model.entity.ReportInformation;
import org.trams.sicbang.model.entity.User;
import org.trams.sicbang.model.enumerate.UserType;
import org.trams.sicbang.model.exception.FormError;
import org.trams.sicbang.model.form.FormReport;
import org.trams.sicbang.model.form.FormUser;
import org.trams.sicbang.web.controller.AbstractController;

/**
 * Created by voncount on 15/04/2016.
 */
@Controller(value = "memberControllerBroker")
@RequestMapping(value = "/member/broker")
public class ControllerBroker extends AbstractController {

    final String BASE_URL = "/member/broker/";
    final String BASE_TEMPLATE = "web/content/broker/";

    /**
     * Filter
     * @param map
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
    public String index(
            @ModelAttribute FormUser form,
            @RequestParam(value = "type-broker", required = false) String typeBroker,
            @RequestParam(value = "type-trusted-broker", required = false) String typeTrustedBroker,
            ModelMap map) {

        // authorized request
        form.setRole("MEMBER");
        form.setType(UserType.BROKER.name() + "," + UserType.NON_BROKER.name());
//        if (typeBroker != null) {
//            form.setType(UserType.BROKER.name());
//        }
//        if (typeTrustedBroker != null) {
//            form.setType(UserType.TRUSTED_BROKER.name());
//        }
        Page<User> users = serviceUser.filter(form);

        map.put("items", users);
        return BASE_TEMPLATE + "list";
    }

    /**
     * Detail
     * @param map
     * @return
     */
    @RequestMapping(value = "/{userId}", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
    public String detail(
            @ModelAttribute FormUser form,
            ModelMap map) {

        User user = serviceUser.findOne(form);
        FormReport formReport = new FormReport();
        formReport.setUserId(form.getUserId());
        Page<ReportInformation> reports = serviceReport.filter(formReport);

        map.put("user", user);
        map.put("items", reports);
        return BASE_TEMPLATE + "detail";
    }

    /**
     * Update
     * @param map
     * @return
     */
    @RequestMapping(value = "/{userId}", method = RequestMethod.POST, produces = MediaType.TEXT_HTML_VALUE)
    @ResponseBody
    public ResponseEntity update(
            @ModelAttribute FormUser form,
            ModelMap map) {
        serviceUser.update(form);
        return new ResponseEntity(HttpStatus.OK);
    }

    /**
     * Delete
     * @param form
     * @return
     */
    @RequestMapping(value = "/{userId}", method = RequestMethod.DELETE, produces = MediaType.TEXT_HTML_VALUE)
    public ResponseEntity delete(
            @ModelAttribute FormUser form) {
        serviceUser.delete(form);
        return new ResponseEntity(HttpStatus.OK);
    }

    /**
     * Filter
     * @param map
     * @return
     */
    @RequestMapping(value = "/approved", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
    public String approvedList(
            @ModelAttribute FormUser form,
            ModelMap map) {

        // authorized request
        form.setRole("MEMBER");
        form.setType("TRUSTED_BROKER");
        Page<User> users = serviceUser.filter(form);

        map.put("items", users);
        return BASE_TEMPLATE + "list";
    }

    /**
     * Create
     * @param form
     * @return
     */
    @RequestMapping(value = "/create", method = {RequestMethod.GET, RequestMethod.POST})
    public Object create(
            @ModelAttribute BaseFormSearch form
    ) {
        switch (getRequestMethod()) {
            case POST:
                FormError error = null;
                if (error != null) {
                    return new ResponseEntity(error, HttpStatus.BAD_REQUEST);
                }
                // create service
                return new ResponseEntity(HttpStatus.OK);
            case GET:
            default:
                return BASE_TEMPLATE + "create";
        }
    }

}
