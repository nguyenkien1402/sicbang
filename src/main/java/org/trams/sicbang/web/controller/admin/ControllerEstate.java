package org.trams.sicbang.web.controller.admin;

import org.apache.commons.validator.Form;
import org.apache.log4j.Logger;
import org.springframework.core.env.SystemEnvironmentPropertySource;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.trams.sicbang.common.utils.ConvertUtils;
import org.trams.sicbang.model.entity.Attachment;
import org.trams.sicbang.model.entity.Estate;
import org.trams.sicbang.model.exception.FormError;
import org.trams.sicbang.model.form.FormEstate;
import org.trams.sicbang.web.controller.AbstractController;

import java.util.Collection;
import java.util.Optional;
import java.util.concurrent.SynchronousQueue;

/**
 * Created by voncount on 15/04/2016.
 */
@Controller(value = "adminControllerEstate")
@RequestMapping(value = "/admin/estate")
public class ControllerEstate extends AbstractController {

    final String BASE_URL = "/admin/estate/";
    final String BASE_TEMPLATE = "admin/content/estate/";
    private static Logger logger = Logger.getLogger(ControllerEstate.class);

    @RequestMapping(method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
    public String index(
            @ModelAttribute FormEstate form,
            @RequestParam(value = "pageIndex", defaultValue = "0") String pageIndex,
            ModelMap map) {
        Optional<Integer> _pageIndex = ConvertUtils.toIntNumber(pageIndex);

        FormEstate formEstate = new FormEstate();
        formEstate.setPageIndex(_pageIndex.isPresent() ? _pageIndex.get() : 0);
        Page<Estate> estates = serviceEstate.filter(formEstate);
        System.out.println("estate form");
        map.put("items", estates);
        return BASE_TEMPLATE + "list";
    }

    /**
     * Filter
     *
     * @param map
     * @return
     */
    @RequestMapping(method = RequestMethod.PUT, produces = MediaType.TEXT_HTML_VALUE)
    public String detailAjax(
            @ModelAttribute FormEstate form,
            @RequestParam(value = "pageIndex", defaultValue = "0") String pageIndex,
            @RequestParam(value = "dataType", defaultValue = "tab-all-estates") String dataType,
            ModelMap map) {

//        logger.info("datatype datatype : "+dataType);
        Optional<Integer> _pageIndex = ConvertUtils.toIntNumber(pageIndex);
        System.out.println("estate ajax");
        switch (dataType) {
            case "tab-all-estates": {
                FormEstate formEstate = new FormEstate();
                formEstate.setPageIndex(_pageIndex.isPresent() ? _pageIndex.get() : 0);
                Page<Estate> estates = serviceEstate.filter(formEstate);
                map.put("items", estates);
                return BASE_TEMPLATE + "ajax/tab-all-estates";
            }
            case "tab-start-list": {
                FormEstate formEstate = new FormEstate();
                formEstate.setPageIndex(_pageIndex.isPresent() ? _pageIndex.get() : 0);
                formEstate.setEstateType("STARTUP");
//                form.setSubwayStation(subway);
                Page<Estate> estates = serviceEstate.filter(formEstate);
                map.put("items", estates);
                System.out.println("startup estate: "+estates.getSize());
                return BASE_TEMPLATE + "ajax/tab-start-list";
            }
            case "tab-vacant-list": {
                System.out.println("vacant estate");
                FormEstate formEstate = new FormEstate();
                formEstate.setPageIndex(_pageIndex.isPresent() ? _pageIndex.get() : 0);
                formEstate.setEstateType("VACANT");
//                form.setSubwayStation(subway);
                Page<Estate> estates = serviceEstate.filter(formEstate);

                map.put("items", estates);
                return BASE_TEMPLATE + "ajax/tab-vacant-list";
            }
            default:
                return BASE_TEMPLATE + "ajax/tab-all-estate";
        }
    }

    @RequestMapping(value = "/detail/{estateId}", method = RequestMethod.PUT, produces = MediaType.TEXT_HTML_VALUE)
    public String detailEstateAjax(
            @PathVariable(value = "estateId") String estateId,
            @ModelAttribute FormEstate formEstate,
            ModelMap map) {
        System.out.println("estate id: "+estateId);
        FormEstate estateForm = new FormEstate();
        estateForm.setEstateId(estateId);
        Estate estate = serviceEstate.findOne(estateForm);
        Collection<Attachment> listAttach = estate.getAttachments();
        System.out.println("list attach: " +listAttach.size());
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

    /**
     * Detail
     *
     * @param id
     * @param map
     * @return
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
    public String detail(
            @PathVariable(value = "id") String id,
            @ModelAttribute FormEstate formEstate,
            ModelMap map) {
        System.out.println("estate id: "+id);
        FormEstate estateForm = new FormEstate();
        estateForm.setEstateId(id);
        Estate estate = serviceEstate.findOne(estateForm);
        Collection<Attachment> listAttach = estate.getAttachments();
        System.out.println("list attach: " +listAttach.size());
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

    /**
     * Update
     *
     * @param estateId
     * @param map
     * @return
     */
    @RequestMapping(value = "/update/{estateId}", method = RequestMethod.POST, produces = MediaType.TEXT_HTML_VALUE)
    public String update(
            @PathVariable(value = "estateId") String estateId,
            @ModelAttribute FormEstate formEstate,
            ModelMap map) {
        FormEstate formSearch = new FormEstate();
        formSearch.setEstateId(estateId);
        Estate estate = serviceEstate.findOne(formSearch);
        serviceEstate.updateEstate(formEstate,estate);
//        Collection<Attachment> listAttach = estate.getAttachments();
//        System.out.println("list attach type: " +listAttach.size());
//        map.put("attachments", listAttach);
        map.put("estate",estate);
//        map.put("sizeattach", listAttach.size());
        return "redirect:/admin/estate";
    }


//    /**
//     *
//     * @param form
//     * @return
//     */
//    @RequestMapping(value = "/{boardId}", method = RequestMethod.POST)
//    @ResponseBody
//    public ResponseEntity update(
//            @ModelAttribute FormBoard form) {
//        serviceBoard.update(form);
//        return new ResponseEntity(HttpStatus.OK);
//    }

    /**
     * Delete
     *
     * @param id
     * @param map
     * @return
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = MediaType.TEXT_HTML_VALUE)
    public String delete(
            @PathVariable(value = "id") String id,
            ModelMap map) {
        FormEstate form = new FormEstate();
        form.setEstateId(id);
        serviceEstate.delete(form);
        return "redirect:" + BASE_URL + id;
    }

    /**
     * Create
     *
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

    /*
   * content_repair
   * @
   * */
    @RequestMapping(value = "/repair/{estateId}", method = RequestMethod.PUT, produces = MediaType.TEXT_HTML_VALUE)
    public String repairBrokerAjax(
            @PathVariable(value = "estateId") String estateId,
//            @RequestParam(value = "dataType", defaultValue = "tab-estates-registered") String dataType,
            ModelMap map) {
        System.out.println("Go estate repair");
        FormEstate estateForm = new FormEstate();
        estateForm.setEstateId(estateId);
        Estate estate = serviceEstate.findOne(estateForm);
        Collection<Attachment> listAttach = estate.getAttachments();
        System.out.println("list attach: " +listAttach.size());
        map.put("attachments", listAttach);
        map.put("estate",estate);
        System.out.println("estate id: " + estateId);
        map.put("sizeattach", listAttach.size());
        System.out.println("go for content repari");
        return BASE_TEMPLATE +"ajax/content_repair_2";

    }


    /*
      * chage type of state
      * @param: estateId
      * @param: type
       */
    @RequestMapping(value = "/change/{estateId}/{type}", method = RequestMethod.PUT, produces = MediaType.TEXT_HTML_VALUE)
    public String changeEstateType(
            @PathVariable(value = "estateId") String estateId,
            @PathVariable(value = "type") String type,
            @ModelAttribute FormEstate form,
//            @RequestParam(value = "dataType", defaultValue = "tab-estates-registered") String dataType,
            ModelMap map) {
        System.out.println("======================================");
        System.out.println("type:" +type);
        System.out.println("name: "+form.getName());
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



}
