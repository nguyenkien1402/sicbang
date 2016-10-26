package org.trams.sicbang.web.controller.member;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.trams.sicbang.model.dto.BaseFormSearch;
import org.trams.sicbang.model.entity.ReportInformation;
import org.trams.sicbang.model.entity.User;
import org.trams.sicbang.model.exception.FormError;
import org.trams.sicbang.model.form.FormPassword;
import org.trams.sicbang.model.form.FormReport;
import org.trams.sicbang.model.form.FormUser;
import org.trams.sicbang.model.form.FormWithdraw;
import org.trams.sicbang.service.implement.ServiceAuthorized;
import org.trams.sicbang.validation.ValidationUser;
import org.trams.sicbang.web.controller.AbstractController;

/**
 * Created by voncount on 15/04/2016.
 */
@Controller(value = "memberControllerUser")
@RequestMapping(value = "/member/user")
public class ControllerUser extends AbstractController {

    final String BASE_URL = "/member/user/";
    final String BASE_TEMPLATE = "web/content/setting/";

    /**
     * Filter
     * @param map
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
    public String index(ModelMap map) {
        User user = getUserSession();
        map.put("user",user);
        return BASE_TEMPLATE + "setting";
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
     * @param form
     * @return
     */
    @RequestMapping(value="/update",method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
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
     * Reset password
     * @param form
     * @return
     */
    @RequestMapping(value = "/update/password", method = RequestMethod.POST, produces = MediaType.TEXT_PLAIN_VALUE)
    @ResponseBody
    public String resetPassword(
            @ModelAttribute FormPassword form,@ModelAttribute String email) {
        FormError error = validationUser.validateResetPassword(form);

        if (error != null) {
            return "FAIL";
        }
        try {
            serviceUser.resetPassword(form);
        }catch (Exception e){
            return "PASSWORD_INCORRECT";
        }
        return "SUCCESS";
    }

    /**
     * Withdraw user
     * @param form
     * @return
     */
    @RequestMapping(value = "/update/withdraw", method = RequestMethod.POST, produces = MediaType.TEXT_PLAIN_VALUE)
    @ResponseBody
    public String withdraw(
            @ModelAttribute FormWithdraw form) {
        FormError error = validationUser.validateWithdraw(form);
        if (error != null) {
            return "FAIL";
        }
        serviceUser.withdraw(form);
        return "SUCCESS";
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
            @ModelAttribute BaseFormSearch form
    ) {
        switch (getRequestMethod()) {
            case POST:
                // TODO validate form
                FormError error = null;
                if (error != null) {
                    return new ResponseEntity(error, HttpStatus.BAD_REQUEST);
                }
                // TODO call create service
                return new ResponseEntity(HttpStatus.OK);
            case GET:
            default:
                return BASE_TEMPLATE + "create";
        }
    }

}
