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
import org.springframework.web.multipart.MultipartFile;
import org.trams.sicbang.model.entity.*;
import org.trams.sicbang.model.exception.FormError;
import org.trams.sicbang.model.form.FormEstate;
import org.trams.sicbang.model.form.FormRecent;
import org.trams.sicbang.model.form.FormUser;
import org.trams.sicbang.model.form.FormWishlist;
import org.trams.sicbang.service.implement.ServiceAuthorized;
import org.trams.sicbang.web.controller.AbstractController;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

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
        isSession();
        User user = (User) httpRequest.getSession().getAttribute("USER_SESSION");
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
        isSession();
        User user = (User) httpRequest.getSession().getAttribute("USER_SESSION");
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
     * @param map
     * @return
     */
    @RequestMapping(value = "/sell-upload", method = RequestMethod.POST)
    public Object create(@ModelAttribute FormEstate form,ModelMap map) {
        isSession();
        User user = (User) httpRequest.getSession().getAttribute("USER_SESSION");
        form.setUserId(user.getId().toString());
        form.setAll_addr(form.getCity()+" "+form.getDistrict()+" "+form.getTown()+" "+form.getAll_addr());
        form.setIsAdvertised("0");
        form.setIsApproved("0");
        form.setBusinessZone(form.getTown()+" "+form.getDistrict()+" "+form.getCity());
        List<String> attachments = new ArrayList<>();
        for(MultipartFile multipartFile: form.getAttachmentFiles()){
            if(!multipartFile.getOriginalFilename().equals("")) {
                String file = "";
                try {
                    file = javax.xml.bind.DatatypeConverter.printBase64Binary(multipartFile.getBytes());
                    attachments.add(file);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        form.setAttachments(attachments);
        FormError error = validationEstate.validateCreate(form);
            if (error != null) {
                return new ResponseEntity(error,HttpStatus.BAD_REQUEST);
            }
            serviceEstate.create(form);
            return "redirect:/member/estate/estate-waiting";
    }

    /**
     * Redirect
     * @return
     */
    @RequestMapping(value="/estate-waiting",method = RequestMethod.GET, produces = MediaType.TEXT_PLAIN_VALUE)
    public String estateWaiting(){
        return "web/content/estate/estate-waiting";
    }


    /**
     * Add Wishlist
     * @param formWishlist
     * @return
     */
    @RequestMapping(value="/addWishList",method = RequestMethod.POST, produces = MediaType.TEXT_PLAIN_VALUE)
    @ResponseBody
    public String addWishList(@ModelAttribute FormWishlist formWishlist) {
        serviceWishlist.create(formWishlist);
        return "SUCCESS";
    }

    /**
     * remove Wishlist
     * @param formWishlist
     * @return
     */
    @RequestMapping(value="/removeWishList",method = RequestMethod.POST, produces = MediaType.TEXT_PLAIN_VALUE)
    @ResponseBody
    public String removeWishList(@ModelAttribute FormWishlist formWishlist) {
        serviceWishlist.delete(formWishlist);
        return "SUCCESS";
    }

    /**
     * Get All Wishlist
     * @param form
     * @param map
     * @return
     */
    @RequestMapping(value="/wishlist",method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
    public String wishlist(@ModelAttribute FormWishlist form, ModelMap map) throws IOException {
        isSession();
        User user = (User) httpRequest.getSession().getAttribute("USER_SESSION");
        form.setUserId(user.getId().toString());
        Page<Wishlist> wishlists = serviceWishlist.filter(form);
        map.put("wishlists",wishlists);
        return "web/content/" + "optional/wishlist";
    }
    /**
     * Redirect to Sell-upload
     * @param form
     * @return
     */
    @RequestMapping(value="/sell-upload",method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
    public String sellUpload(FormEstate form) {
        return "web/content/estate/sell-upload";
    }

    /**
     * getALlCategory
     *
     * @return
     */
    @RequestMapping(value="/getAllCategory",method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity findBusinessType(){
        List<Category> categories = serviceBusinessType.findAllCategory();
        return new ResponseEntity(categories,HttpStatus.OK);
    }
}
