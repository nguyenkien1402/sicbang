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
import org.springframework.web.bind.annotation.*;
import org.trams.sicbang.model.entity.Estate;
import org.trams.sicbang.model.entity.Recent;
import org.trams.sicbang.model.entity.User;
import org.trams.sicbang.model.entity.Wishlist;
import org.trams.sicbang.model.exception.FormError;
import org.trams.sicbang.model.form.FormEstate;
import org.trams.sicbang.model.form.FormRecent;
import org.trams.sicbang.model.form.FormUser;
import org.trams.sicbang.model.form.FormWishlist;
import org.trams.sicbang.service.implement.ServiceAuthorized;
import org.trams.sicbang.web.controller.AbstractController;

import java.io.IOException;

/**
 * Created by voncount on 15/04/2016.
 */
@Controller(value = "memberControllerEstate")
@RequestMapping(value = "/member/estate")
public class ControllerEstate extends AbstractController {

    final String BASE_URL = "/member/estate/";
    final String BASE_TEMPLATE = "web/content/broker/";


    /**
     * Filter
     * @param map
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
    public String index(@ModelAttribute FormEstate formEstate, ModelMap map) {
        Authentication auth = serviceAuthorized.isAuthenticated();
        UserDetails userDetails = (UserDetails) auth.getPrincipal();
        User user = serviceUser.findUserByEmail(userDetails.getUsername());
        formEstate.setUserId(user.getId().toString());
        Page<Estate> estates = serviceEstate.filter(formEstate);
        map.put("estates",estates);
        return BASE_TEMPLATE + "broker-content-list";
    }

    /**
     * Detail
     * @param id
     * @param map
     * @return
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
    public String detail(
            @PathVariable(value = "id") String id,
            ModelMap map) {
        return BASE_TEMPLATE + "detail";
    }

    @RequestMapping(value="/advertised",method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
    public String getAdvertised(@ModelAttribute FormEstate formEstate, ModelMap map) {
        Authentication auth = serviceAuthorized.isAuthenticated();
        UserDetails userDetails = (UserDetails) auth.getPrincipal();
        User user = serviceUser.findUserByEmail(userDetails.getUsername());
        formEstate.setUserId(user.getId().toString());
        formEstate.setIsAdvertised("true");
        Page<Estate> estates = serviceEstate.filter(formEstate);
        map.put("estates",estates);
        map.put("advertised","true");
        return BASE_TEMPLATE + "broker-content-list";
    }
    /**
     * Update
     * @param id
     * @param map
     * @return
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.POST, produces = MediaType.TEXT_HTML_VALUE)
    public String update(
            @PathVariable(value = "id") String id,
            ModelMap map) {
        return "redirect:" + BASE_URL;
    }

    /**
     * Delete
     * @param id
     * @return
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = MediaType.TEXT_PLAIN_VALUE)
    @ResponseBody
    public String delete(
            @PathVariable(value = "id") String id
            ) {
        FormEstate formEstate = new FormEstate();
        formEstate.setEstateId(id);
        serviceEstate.delete(formEstate);
        return "success";
    }

    /**
     * Create
     * @param form
     * @return
     */
    @RequestMapping(value = "/create", method = {RequestMethod.GET, RequestMethod.POST})
    public Object create(
            @ModelAttribute FormEstate form
    ) {
        switch (getRequestMethod()) {
            case POST:
                FormError error = validationEstate.validateCreate(form);
                if (error != null) {
                    return new ResponseEntity(error, HttpStatus.BAD_REQUEST);
                }
                serviceEstate.create(form);
                return new ResponseEntity(HttpStatus.OK);
            case GET:
            default:
                return BASE_TEMPLATE + "create";
        }
    }

    @RequestMapping(value="/addWishList",method = RequestMethod.POST, produces = MediaType.TEXT_PLAIN_VALUE)
    @ResponseBody
    public String addWishList(@ModelAttribute FormWishlist formWishlist) {
        System.out.println("USER ID :"+formWishlist.getUserId());
        System.out.println("ESTATE ID:"+formWishlist.getEstateId());
        serviceWishlist.create(formWishlist);
        return "SUCCESS";
    }
    @RequestMapping(value="/removeWishList",method = RequestMethod.POST, produces = MediaType.TEXT_PLAIN_VALUE)
    @ResponseBody
    public String removeWishList(@ModelAttribute FormWishlist formWishlist) {
        serviceWishlist.delete(formWishlist);
        return "SUCCESS";
    }

    @RequestMapping(value="/wishlist",method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
    public String wishlist(@ModelAttribute FormWishlist form, ModelMap map) throws IOException {
        Authentication auth = serviceAuthorized.isAuthenticated();
        UserDetails userDetails = (UserDetails) auth.getPrincipal();
        User user = serviceUser.findUserByEmail(userDetails.getUsername());
        form.setUserId(user.getId().toString());
        Page<Wishlist> wishlists = serviceWishlist.filter(form);
        map.put("wishlists",wishlists);
        return "web/content/" + "optional/wishlist";
    }
}
