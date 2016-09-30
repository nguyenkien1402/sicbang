package org.trams.sicbang.web.controller.member;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.trams.sicbang.model.entity.ReportInformation;
import org.trams.sicbang.model.exception.FormError;
import org.trams.sicbang.model.form.FormReport;
import org.trams.sicbang.model.form.FormUser;
import org.trams.sicbang.validation.ValidationReport;
import org.trams.sicbang.web.controller.AbstractController;

/**
 * Created by voncount on 15/04/2016.
 */
@Controller(value = "memberControllerReport")
@RequestMapping(value = "/member/report")
public class ControllerReport extends AbstractController {

    final String BASE_URL = "/member/report/";
    final String BASE_TEMPLATE = "web/content/report/";

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
     * Detail
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
     * Delete
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
    @RequestMapping(value = "/create", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public ResponseEntity create(@ModelAttribute FormReport form ) {
        FormError error = validationReport.validateCreateReport(form);
        if(error != null){
            return new ResponseEntity(error, HttpStatus.OK);
        }
        serviceReport.create(form);
        return new ResponseEntity("{\"errors\":{}}",HttpStatus.OK);
    }

}
