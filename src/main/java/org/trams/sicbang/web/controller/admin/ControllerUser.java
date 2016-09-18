package org.trams.sicbang.web.controller.admin;

import com.google.common.base.Strings;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.trams.sicbang.model.entity.ReportInformation;
import org.trams.sicbang.model.entity.User;
import org.trams.sicbang.model.enumerate.MessageResponse;
import org.trams.sicbang.model.exception.FormError;
import org.trams.sicbang.model.form.FormPassword;
import org.trams.sicbang.model.form.FormReport;
import org.trams.sicbang.model.form.FormUser;
import org.trams.sicbang.web.controller.AbstractController;

import java.security.Principal;

/**
 * Created by voncount on 15/04/2016.
 */
@Controller(value = "adminControllerUser")
@RequestMapping(value = "/admin/user")
public class ControllerUser extends AbstractController {

    final String BASE_URL = "/admin/user/";
    final String BASE_TEMPLATE = "admin/content/user/";

    /**
     * Filter
     * @param map
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
    public String index(
            @ModelAttribute FormUser form,
            ModelMap map, Principal principal) {
        // authorized request
        form.setRole("MEMBER");
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
     *
     * @param form
     * @return
     */
    @RequestMapping(value = "/{userId}", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public ResponseEntity update(
            @ModelAttribute FormUser form) {
        FormError error = validationUser.validateUpdateSettings(form);
        if (error != null) {
            return new ResponseEntity(error, HttpStatus.BAD_REQUEST);
        }
        serviceUser.update(form);
        return new ResponseEntity(HttpStatus.OK);
    }

    /**
     *
     * @param form
     * @return
     */
    @RequestMapping(value = "/{userId}/password", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public ResponseEntity resetPassword(
            @ModelAttribute FormPassword form) {
        FormError error = validationUser.validateResetPassword(form);
        if (error != null) {
            System.out.println(error.getErrors().get("passwordInvalid"));
            System.out.println("confirm: "+error.getErrors().get("passwordConfirm"));
            if(!Strings.isNullOrEmpty(error.getErrors().get("passwordInvalid"))){
                if(error.getErrors().get("passwordInvalid").equals(MessageResponse.EXCEPTION_FIELD_INVALID)) {
                    return new ResponseEntity(error, HttpStatus.BAD_REQUEST);
                }
            }
            if(!Strings.isNullOrEmpty(error.getErrors().get("passwordConfirm")) && error.getErrors().get("passwordConfirm").equals(MessageResponse.EXCEPTION_PASSWORD_NOT_CONFIRM)) {
                return new ResponseEntity(error, HttpStatus.BAD_REQUEST);
            }
            return new ResponseEntity(error, HttpStatus.BAD_REQUEST);
        }
        serviceUser.resetPassword(form);
        return new ResponseEntity(HttpStatus.OK);
    }

    /**
     * Delete
     * @param form
     * @return
     */
    @RequestMapping(value = "/{userId}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public ResponseEntity delete(@ModelAttribute FormUser form) {
        serviceUser.delete(form);
        return new ResponseEntity(HttpStatus.OK);
    }

    /**
     * Create
     * @param form
     * @return
     */
    @RequestMapping(value = "/create", method = {RequestMethod.GET, RequestMethod.POST})
    public Object create(
            @ModelAttribute FormUser form
    ) {
        switch (getRequestMethod()) {
            case POST:
                FormError error = validationUser.validateCreate(form);
                if (error != null) {
                    return new ResponseEntity(error, HttpStatus.BAD_REQUEST);
                }
                serviceUser.create(form);
                return new ResponseEntity(HttpStatus.OK);
            case GET:
            default:
                return BASE_TEMPLATE + "create";
        }
    }

}
