package org.trams.sicbang.web.controller.anonymous;

import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.trams.sicbang.model.exception.FormError;
import org.trams.sicbang.model.form.FormUser;
import org.trams.sicbang.web.controller.AbstractController;

import java.io.IOException;

/**
 * Created by DinhTruong on 9/27/2016.
 */
@Controller(value = "anonymousControllerRegistration")
public class ControllerRegistration extends AbstractController{

    final String BASE_TEMPLATE = "web/content/";

    @RequestMapping(value = "/broker-join", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
    public String brokerJoin() throws IOException {
        return BASE_TEMPLATE + "register/broker-join";
    }


    @RequestMapping(value = "/broker-join-waiting", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
    public String brokerJoinWaiting() throws IOException {
        return BASE_TEMPLATE + "register/broker-join-waiting";
    }

    @RequestMapping(value = "/member-join", method = RequestMethod.POST, produces = MediaType.TEXT_PLAIN_VALUE)
    @ResponseBody
    public String memberJoin(@ModelAttribute FormUser formUser) throws IOException {
        formUser.setType("MEMBER");
        formUser.setRole("MEMBER");
        formUser.setStatus("ACTIVE");
        FormError error = validationUser.validateCreate(formUser);
        if(error != null){
            return error.getErrors().get("email");
        }
        try{
        serviceUser.create(formUser);
        }catch(Exception e){
            e.printStackTrace();
        }
        return "SUCCESS";
    }

    @RequestMapping(value = "/create-broker", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public ResponseEntity create(
            @ModelAttribute FormUser form) {
        form.setStatus("INACTIVE");
        form.setType("BROKER");
        form.setRole("MEMBER");
        FormError error = validationUser.validateJoin(form);
        if (error != null) {
            return new ResponseEntity(error, HttpStatus.OK);
        }
        serviceUser.create(form);
        return new ResponseEntity("{\"errors\":{}}",HttpStatus.OK);
    }
}
