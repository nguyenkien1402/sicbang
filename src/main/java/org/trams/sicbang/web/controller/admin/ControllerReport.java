package org.trams.sicbang.web.controller.admin;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.trams.sicbang.model.entity.Attachment;
import org.trams.sicbang.model.entity.Estate;
import org.trams.sicbang.model.entity.ReportInformation;
import org.trams.sicbang.model.entity.User;
import org.trams.sicbang.model.exception.FormError;
import org.trams.sicbang.model.form.FormEstate;
import org.trams.sicbang.model.form.FormReport;
import org.trams.sicbang.web.controller.AbstractController;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by voncount on 15/04/2016.
 */
@Controller(value = "adminControllerReport")
@RequestMapping(value = "/admin/report")
public class ControllerReport extends AbstractController {

    final String BASE_URL = "/admin/report/";
    final String BASE_TEMPLATE = "admin/content/report/";

    /**
     * Filter
     * @param map
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
    public String index(
            @ModelAttribute FormReport form,
            ModelMap map) {
        Page<ReportInformation> reportInformations = serviceReport.filter(form);
        map.put("items", reportInformations);
        return BASE_TEMPLATE + "list";
    }

    /**
     *
     * @param form
     * @param map
     * @return
     */
    @RequestMapping(value = "/{reportId}", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
    public String detail(
            @ModelAttribute FormReport form,
            ModelMap map) {
        ReportInformation reportInformation = serviceReport.findOne(form);
        map.put("item", reportInformation);
        return BASE_TEMPLATE + "detail";
    }

    /**
     * Update
     * @param id
     * @param map
     * @return
     */
    @RequestMapping(value = "/{reportId}", method = RequestMethod.POST, produces = MediaType.TEXT_HTML_VALUE)
    public String update(
            @PathVariable(value = "id") String id,
            ModelMap map) {
        return "redirect:" + BASE_URL;
    }

    /**
     *
     * @param form
     * @return
     */
    @RequestMapping(value = "/{reportId}", method = RequestMethod.DELETE)
    @ResponseBody
    public ResponseEntity delete(@ModelAttribute FormReport form) {
        serviceReport.delete(form);
        return new ResponseEntity(HttpStatus.OK);
    }

    /**
     * Create
     * @param form
     * @return
     */
    @RequestMapping(value = "/create", method = {RequestMethod.GET, RequestMethod.POST})
    public Object create(
            @ModelAttribute FormReport form
    ) {
        switch (getRequestMethod()) {
            case POST:
                FormError error = validationReport.validateCreateReport(form);
                if (error != null) {
                    return new ResponseEntity(error, HttpStatus.BAD_REQUEST);
                }
                serviceReport.create(form);
                return new ResponseEntity(HttpStatus.OK);
            case GET:
            default:
                return BASE_TEMPLATE + "create";
        }
    }

    @RequestMapping(value = "/estate/{estateId}", method = RequestMethod.GET)
    public String detailEstate(
            @PathVariable(value = "estateId") String estateId,
            ModelMap map
    ){
        FormEstate estateForm = new FormEstate();
        estateForm.setEstateId(estateId);
        estateForm.setIsApproved("1");
        Estate estate = serviceEstate.findOne(estateForm);
        User user = estate.getUser();
        Collection<Attachment> listAttach = new ArrayList<Attachment>();
        listAttach = estate.getAttachments();
        map.put("attachments", listAttach);
        map.put("estate",estate);
        map.put("sizeattach", listAttach.size());
        map.put("user",user);

        if(estate.getEstateType().equals("STARTUP")){
            System.out.println("go for startup");
            return BASE_TEMPLATE +"detail_startup";
        }else{
            System.out.println("go for vacant");
            return BASE_TEMPLATE +"detail_vacant";
        }
    }

    @RequestMapping(value = "/estate/repair/{estateId}", method = RequestMethod.GET)
    public String repairEstate(
            @PathVariable(value = "estateId") String estateId,
//            @RequestParam(value = "dataType", defaultValue = "tab-estates-registered") String dataType,
            ModelMap map) {
        System.out.println("Go estate repair");
        FormEstate estateForm = new FormEstate();
        estateForm.setEstateId(estateId);
        estateForm.setIsApproved("1");
        Estate estate = serviceEstate.findOne(estateForm);
        Collection<Attachment> listAttach = estate.getAttachments();
        System.out.println("list attach: " +listAttach.size());
        map.put("attachments", listAttach);
        map.put("estate",estate);
        System.out.println("estate id: " + estateId);
        map.put("sizeattach", listAttach.size());
        System.out.println("go for content repair");
        return BASE_TEMPLATE +"detail_repair";

    }

    @RequestMapping(value = "/eatate/change/{estateId}/{type}", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
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
        estateForm.setIsApproved("1");
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
        return "redirect:/admin/report/estate/repair/"+estateId;

    }
}
