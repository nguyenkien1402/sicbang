package org.trams.sicbang.web.controller.admin;

import com.google.common.base.Strings;
import org.codehaus.groovy.runtime.powerassert.SourceText;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.trams.sicbang.common.utils.ConvertUtils;
import org.trams.sicbang.model.dto.BaseFormSearch;
import org.trams.sicbang.model.entity.*;
import org.trams.sicbang.model.enumerate.UserType;
import org.trams.sicbang.model.exception.FormError;
import org.trams.sicbang.model.form.FormEstate;
import org.trams.sicbang.model.form.FormReport;
import org.trams.sicbang.model.form.FormUser;
import org.trams.sicbang.service.implement.ServiceUser;
import org.trams.sicbang.web.controller.AbstractController;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * Created by voncount on 15/04/2016.
 */
@Controller(value = "adminControllerBroker")
@RequestMapping(value = "/admin/broker")
public class ControllerBroker extends AbstractController {

    final String BASE_URL = "/admin/broker/";
    final String BASE_TEMPLATE = "admin/content/broker/";

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
        System.out.println("go to broker");
        form.setRole("MEMBER");
        form.setType(UserType.BROKER.name() + "," + UserType.TRUSTED_BROKER.name());
        if (typeBroker != null) {
            form.setType(UserType.BROKER.name());
        }
        if (typeTrustedBroker != null) {
            form.setType(UserType.TRUSTED_BROKER.name());
        }
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
    public String detailBroker(
            @ModelAttribute FormUser form,
            ModelMap map) {
        User user = serviceUser.findOne(form);
        map.put("user", user);
        return BASE_TEMPLATE + "detail_list";
    }

    @RequestMapping(value = "/detail/{estateId}", method = RequestMethod.PUT, produces = MediaType.TEXT_HTML_VALUE)
    public String detailBrokerAjax(
            @PathVariable(value = "estateId") String estateId,
            @RequestParam(value = "dataType", defaultValue = "tab-estates-registered") String dataType,
            @ModelAttribute FormEstate formEstate,
            ModelMap map) {
        FormEstate estateForm = new FormEstate();
        estateForm.setEstateId(estateId);
        Estate estate = serviceEstate.findOne(estateForm);
        Collection<Attachment> listAttach = new ArrayList<Attachment>();
        listAttach = estate.getAttachments();
        System.out.println("list attach: " +listAttach.size());
        System.out.println("id: "+formEstate.getEstateId());
        map.put("attachments", listAttach);
        map.put("estate",estate);
        map.put("sizeattach", listAttach.size());

        if(estate.getEstateType().equals("STARTUP")){
            System.out.println("go for startup");
            return BASE_TEMPLATE +"ajax/detail-startup";
        }else{
            System.out.println("go for vacant");
            return BASE_TEMPLATE +"ajax/detail-vacant";
        }
    }

    /*
    * content_repair
    * @
    * */
    @RequestMapping(value = "/repair/{estateId}", method = RequestMethod.PUT, produces = MediaType.TEXT_HTML_VALUE)
    public String repairBrokerAjax(
            @PathVariable(value = "estateId") String estateId,
//            @RequestParam(value = "dataType", defaultValue = "tab-estates-registered") String dataType,
            ModelMap map) {
        FormEstate estateForm = new FormEstate();
        estateForm.setEstateId(estateId);
        Estate estate = serviceEstate.findOne(estateForm);
        Collection<Attachment> listAttach = estate.getAttachments();
        System.out.println("list attach: " +listAttach.size());
        map.put("attachments", listAttach);
        map.put("estate",estate);
        System.out.println("estate id: " + estateId);
        map.put("sizeattach", listAttach.size());

        System.out.println("go for content repair");
        return BASE_TEMPLATE +"ajax/content_repair_2";

    }


    /*
    * chage type of state
    * @param: estateId
    * @param: type
     */
    @RequestMapping(value = "/estate/change/{estateId}/{type}", method = RequestMethod.PUT, produces = MediaType.TEXT_HTML_VALUE)
    public String changeEstateType(
            @PathVariable(value = "estateId") String estateId,
            @PathVariable(value = "type") String type,
//            @RequestParam(value = "dataType", defaultValue = "tab-estates-registered") String dataType,
            ModelMap map) {
        System.out.println("======================================");
        System.out.println("type:" +type);
        FormEstate estateForm = new FormEstate();
        estateForm.setEstateId(estateId);
        Estate estate = new Estate();
        serviceEstate.updateEstateType(estateForm,type);
        estate = serviceEstate.findOne(estateForm);
        Collection<Attachment> listAttach = estate.getAttachments();
        System.out.println("list attach type: " +listAttach.size());
        map.put("attachments", listAttach);
        map.put("estate",estate);
        System.out.println("estate id type: " + estateId);
        map.put("sizeattach", listAttach.size());
        System.out.println("========================================");
        System.out.println("go for content repari");
        return BASE_TEMPLATE +"ajax/content_repair_2";

    }




    /**
     * Detail
     * @param map
     * @return
     */
    @RequestMapping(value = "/{userId}", method = RequestMethod.PUT, produces = MediaType.TEXT_HTML_VALUE)
    public String detailAjax(
            @PathVariable(value = "userId") String userId,
            @RequestParam(value = "pageIndex", defaultValue = "0") String pageIndex,
            @RequestParam(value = "dataType", defaultValue = "tab-estates-registered") String dataType,
            ModelMap map) {

        Optional<Integer> _pageIndex = ConvertUtils.toIntNumber(pageIndex);
        FormUser formUser = new FormUser();
        formUser.setUserId(userId);
        User user = serviceUser.findOne(formUser);
        map.put("user", user);
        switch (dataType) {
            case "tab-reports": {
                FormReport formReport = new FormReport();
                formReport.setUserId(userId);
                formReport.setPageIndex(_pageIndex.isPresent() ? _pageIndex.get() : 0);
                Page<ReportInformation> reports = serviceReport.filter(formReport);
                map.put("reports", reports);

                return BASE_TEMPLATE + "ajax/tab-reports";
            }
            case "tab-advertised-estate": {
                System.out.println("call advertised");
                FormEstate formEstate = new FormEstate();
                formEstate.setUserId(userId);
                formEstate.setPageIndex(_pageIndex.isPresent() ? _pageIndex.get() : 0);
                formEstate.setIsAdvertised(Boolean.TRUE.toString());
                Page<Estate> estates = serviceEstate.filter(formEstate);
                map.put("items", estates);
                return BASE_TEMPLATE + "ajax/tab-advertised-estate";
            }
            case "tab-estates-registered": {
                FormEstate formEstate = new FormEstate();
                formEstate.setUserId(userId);
                formEstate.setPageIndex(_pageIndex.isPresent() ? _pageIndex.get() : 0);
                Page<Estate> estates = serviceEstate.filter(formEstate);
                map.put("items", estates);
            }
            default:
                System.out.println("tab estate");
                return BASE_TEMPLATE + "ajax/tab-estates-registered";
        }

    }

    /*
    * Delete
    * @Param form
    * @return
    */

    @RequestMapping(value = "/estate/{estateId}", method = RequestMethod.DELETE, produces = MediaType.TEXT_HTML_VALUE)
    public ResponseEntity deleteEstate(@ModelAttribute FormEstate form){
        System.out.println("delete estate.");
        System.out.println("estate id: "+form.getEstateId());
        serviceEstate.delete(form);
        return new ResponseEntity(HttpStatus.OK);
    }

    @RequestMapping(value = "/update/{estateId}", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public ResponseEntity update(
            @PathVariable(value = "estateId") String estateId,
            @ModelAttribute FormEstate formEstate,
            ModelMap map) {
        System.out.println("==============================");
        System.out.println("Go for update estate: "+formEstate.getEstateId());
//        String estateId = formEstate.getEstateId();
        FormEstate formSearch = new FormEstate();
        formSearch.setEstateId(estateId);
        Estate estate = serviceEstate.findOne(formSearch);
        if(!Strings.isNullOrEmpty(formEstate.getDetail())) {
            formEstate.setDetail(formEstate.getDetail().trim());
        }
//        List<MultipartFile> multipartFile = null;
//        if(formEstate.getAttachmentFiles() != null){
//            multipartFile = formEstate.getAttachmentFiles();
//            System.out.println("multipart: "+multipartFile.get(0));
//            System.out.println("file: "+multipartFile.get(0).getOriginalFilename()+" - size: "+multipartFile.get(0).getSize());
//        }

        serviceEstate.updateEstate(formEstate,estate);
//        Collection<Attachment> listAttach = estate.getAttachments();
//        map.put("attachments", listAttach);

        map.put("estate",estate);
//        map.put("sizeattach", listAttach.size());
        System.out.println("==============================");
//        return BASE_TEMPLATE +"ajax/content_repair_2";
//        return "redirect:/admin/broker";
        return new ResponseEntity(HttpStatus.OK);
    }

//    /**
//     * Update
//     * @param form
//     * @return
//     */
//    @RequestMapping(value = "/{userId}", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
//    @ResponseBody
//    public ResponseEntity update(
//            @ModelAttribute FormUser form) {
//        serviceUser.update(form);
//        return new ResponseEntity(HttpStatus.OK);
//    }

    @RequestMapping(value = "/update/type", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public ResponseEntity updateTypeUser(@RequestParam("userId") int userId, @RequestParam("userType") String type, @ModelAttribute FormUser form) {
        form.setType(type);
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
        System.out.println("go for approved");
        form.setRole("MEMBER");
        form.setType("TRUSTED_BROKER");
        Page<User> users = serviceUser.filter(form);

        map.put("items", users);
        return BASE_TEMPLATE + "approved";
    }

    /**
     * Detail
     * @param map
     * @return
     */
    @RequestMapping(value = "/approved/{userId}", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
    public String approvedDetail(
            @ModelAttribute FormUser form,
            ModelMap map) {

        User user = serviceUser.findOne(form);
        map.put("user", user);
        return BASE_TEMPLATE + "approved-detail";
    }

    /**
     * Update: approved this broker
     * @return
     */
    @RequestMapping(value = "/approved/{userId}", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity approvedUpdate(
            @ModelAttribute FormUser form) {
        form.setType(UserType.TRUSTED_BROKER.name());
        serviceUser.update(form);
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


    /**
     * Update
     *
     * @param estateId
     * @param map
     * @return
     */


    /**
     * Update
     *
     * @param map
     * @return
     */
    @RequestMapping(value = "/upload/image", method = RequestMethod.POST, produces = MediaType.TEXT_HTML_VALUE)
    public void uploadImage(
//            @PathVariable(value = "estateId") String estateId,
            @RequestParam(value = "id", required = false) String id,
            @ModelAttribute FormEstate formEstate,
            ModelMap map) {
        System.out.println("==============================");
        System.out.println("Go for upload image");
        System.out.println("ID image: " + id);
        System.out.println("==============================");
    }

}
