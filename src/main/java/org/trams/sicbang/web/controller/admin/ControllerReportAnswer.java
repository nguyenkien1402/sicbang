package org.trams.sicbang.web.controller.admin;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.trams.sicbang.model.entity.ReportAnswer;
import org.trams.sicbang.model.exception.FormError;
import org.trams.sicbang.model.form.FormReportAnswer;
import org.trams.sicbang.web.controller.AbstractController;

/**
 * Created by voncount on 15/04/2016.
 */
@Controller(value = "adminControllerReportAnswer")
@RequestMapping(value = "/admin/answer")
public class ControllerReportAnswer extends AbstractController {

    final String BASE_URL = "/admin/answer/";
    final String BASE_TEMPLATE = "admin/content/answer/";

    /**
     * Filter
     * @param map
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
    public String index(
            @ModelAttribute FormReportAnswer form,
            ModelMap map) {
        Page<ReportAnswer> reportAnswers = serviceReportAnswer.filter(form);
        map.put("items", reportAnswers);
        return BASE_TEMPLATE + "list";
    }

    /**
     * Detail
     * @param form
     * @param map
     * @return
     */
    @RequestMapping(value = "/{reportAnswerId}", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
    public String detail(
            @ModelAttribute FormReportAnswer form,
            ModelMap map) {
        ReportAnswer reportAnswer = serviceReportAnswer.findOne(form);
        map.put("item", reportAnswer);
        return BASE_TEMPLATE + "detail";
    }

    /**
     * Detail
     * @param form
     * @param map
     * @return
     */
    @RequestMapping(value = "/edit/{reportAnswerId}", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
    public String detailEdit(
            @ModelAttribute FormReportAnswer form,
            ModelMap map) {
        ReportAnswer reportAnswer = serviceReportAnswer.findOne(form);
        map.put("item", reportAnswer);
        return BASE_TEMPLATE + "update";
    }

    /**
     * Detail
     * @param form
     * @param map
     * @return
     */
    @RequestMapping(value = "/edit/{reportAnswerId}", method = RequestMethod.POST, produces = MediaType.TEXT_HTML_VALUE)
    public ResponseEntity detailUpdate(
            @ModelAttribute FormReportAnswer form,
            ModelMap map) {
        serviceReportAnswer.update(form);
        return new ResponseEntity(HttpStatus.OK);
    }

    /**
     * Update
     * @param form
     * @return
     */
    @RequestMapping(value = "/{reportAnswerId}", method = RequestMethod.POST, produces = MediaType.TEXT_HTML_VALUE)
    public ResponseEntity update(
            @ModelAttribute FormReportAnswer form) {
        serviceReportAnswer.update(form);
        return new ResponseEntity(HttpStatus.OK);

    }

    /**
     * Delete
     * @param form
     * @return
     */
    @RequestMapping(value = "/{reportAnswerId}", method = RequestMethod.DELETE)
    @ResponseBody
    public ResponseEntity delete(@ModelAttribute FormReportAnswer form) {
        serviceReportAnswer.delete(form);
        return new ResponseEntity(HttpStatus.OK);
    }

    /**
     * Create
     * @param form
     * @return
     */
    @RequestMapping(value = "/create", method = {RequestMethod.GET, RequestMethod.POST})
    public Object create(
            @ModelAttribute FormReportAnswer form
    ) {
        switch (getRequestMethod()) {
            case POST:
                FormError error = validationReport.validateCreateReportAnswer(form);
                if (error != null) {
                    return new ResponseEntity(error, HttpStatus.BAD_REQUEST);
                }
                serviceReportAnswer.create(form);
                return new ResponseEntity(HttpStatus.OK);
            case GET:
            default:
                return BASE_TEMPLATE + "create";
        }
    }

}
