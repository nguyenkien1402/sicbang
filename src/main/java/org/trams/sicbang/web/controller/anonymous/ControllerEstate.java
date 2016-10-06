package org.trams.sicbang.web.controller.anonymous;

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
import org.trams.sicbang.model.dto.CustomUserDetail;
import org.trams.sicbang.model.entity.*;
import org.trams.sicbang.model.exception.FormError;
import org.trams.sicbang.model.form.FormEstate;
import org.trams.sicbang.model.form.FormRecent;
import org.trams.sicbang.model.form.FormWishlist;
import org.trams.sicbang.service.implement.ServiceAuthorized;
import org.trams.sicbang.web.controller.AbstractController;

import java.util.*;

/**
 * Created by voncount on 15/04/2016.
 */
@Controller(value = "anonymousControllerEstate")
@RequestMapping(value = "/estate")
public class ControllerEstate extends AbstractController {

    final String BASE_URL = "/estate/";
    final String BASE_TEMPLATE = "web/content/estate/";
    final String BASE_TEMPLATE_BROKER = "web/content/broker";

    /**
     * Filter
     * @param map
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
    public String index(ModelMap map) {

        return BASE_TEMPLATE + "map-all";
    }

    /**
     * Detail
     * @param estateId
     * @param map
     * @return
     */

    @RequestMapping(value = "/detail/{id}", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
    public String detail(
            @PathVariable(value = "id") String estateId,
            ModelMap map) {
        FormEstate estateForm = new FormEstate();
        estateForm.setEstateId(estateId);
        Authentication auth = serviceAuthorized.isAuthenticated();
        Estate estate = serviceEstate.findOne(estateForm);

        Collection<Attachment> listAttach = estate.getAttachments();
        System.out.println("list attach: " +listAttach.size());

        map.put("attachments", listAttach);
        map.put("estate",estate);
        map.put("sizeattach", listAttach.size());
        //if logged as realtor
        if(auth != null){
            UserDetails userDetails = (UserDetails) auth.getPrincipal();
            User user = serviceUser.findUserByEmail(userDetails.getUsername());
            FormWishlist formWishlist = new FormWishlist();
            formWishlist.setUserId(user.getId().toString());
            formWishlist.setEstateId(estateId);
            Wishlist wishlist = serviceWishlist.findOne(formWishlist);
            if(wishlist != null){
                System.out.println("Khác null");
               map.put("isWishList","true");
            }else{
                System.out.println("null");
                map.put("isWishList","false");
            }

            map.put("memberId",user.getId());
            FormRecent formRecent = new FormRecent();
            formRecent.setUserId(user.getId().toString());
            formRecent.setEstateId(estateId);
            Recent recent = serviceRecent.findOne(formRecent);
            System.out.println(user.getType());
            if(recent != null){
            serviceRecent.update(formRecent);
            }else{
            serviceRecent.create(formRecent);
            }
            if(user.getId() == estate.getUser().getId()){
                if(estate.getAdvertised() == true){ // if estate is premium.
                    return BASE_TEMPLATE_BROKER +"/broker-content-premium";
                }
                // estate isn't premium.
                System.out.println("go for broker-content");
                return BASE_TEMPLATE_BROKER +"/broker-content";
            }

        }
        // anonymous
        if(estate.getEstateType().equals("STARTUP")){
            System.out.println("go for startup");
            return BASE_TEMPLATE +"/detail-startup";
        }else{
            System.out.println("go for vacant");
            return BASE_TEMPLATE +"/detail-vacant";
        }

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
     * @param map
     * @return
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = MediaType.TEXT_HTML_VALUE)
    public String delete(
            @PathVariable(value = "id") String id,
            ModelMap map) {
        return "redirect:" + BASE_URL + id;
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
    /**
     * Search startup
     * @param formEstate
     * @return
     */
    @RequestMapping(value = "/search", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public ResponseEntity searchEstate(@ModelAttribute FormEstate formEstate){
        if(!formEstate.getDistrict().equals(""))
            formEstate.setCity(null);
        List<Estate> estates = serviceEstate.filterBy(0,formEstate.getCity(),formEstate.getDistrict(),formEstate.getTown(),formEstate.getEstateType(),formEstate.getSubwayStation());

        return new ResponseEntity(estates,HttpStatus.OK);
    }

    @RequestMapping(value = "/map", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public ResponseEntity estateMap(@ModelAttribute FormEstate formEstate){

        List<Estate> trusted = serviceEstate.filterEstateByType(3,1);
        List<Estate> broker = serviceEstate.filterEstateByType(3,0);
        List<Estate> member = serviceEstate.filterEstateByType(3,2);

        Map<String,List> estates = new HashMap<>();
        estates.put("trusted",trusted);
        estates.put("broker",broker);
        estates.put("member",member);
        return new ResponseEntity(estates,HttpStatus.OK);
    }
}
