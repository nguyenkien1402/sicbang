package org.trams.sicbang.web.controller.admin;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.trams.sicbang.model.entity.Board;
import org.trams.sicbang.model.exception.FormError;
import org.trams.sicbang.model.form.FormBoard;
import org.trams.sicbang.web.controller.AbstractController;

/**
 * Created by voncount on 15/04/2016.
 */
@Controller(value = "adminControllerBoard")
@RequestMapping(value = "/admin/board")
public class ControllerBoard extends AbstractController {

    final String BASE_URL = "/admin/board/";
    final String BASE_TEMPLATE = "admin/content/board/";

    /**
     *
     * @param form
     * @param map
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
    public String index(
            @ModelAttribute FormBoard form,
            ModelMap map) {
        Page<Board> boards = serviceBoard.filter(form);
        map.put("items", boards);
        return BASE_TEMPLATE + "list";
    }

    /**
     *
     * @param form
     * @param map
     * @return
     */
    @RequestMapping(value = "/{boardId}", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
    public String detail(
            @ModelAttribute FormBoard form,
            ModelMap map) {
        Board board = serviceBoard.findOne(form);
        map.put("board", board);
        return BASE_TEMPLATE + "update";
    }

    /**
     *
     * @param form
     * @return
     */
    @RequestMapping(value = "/{boardId}", method = RequestMethod.POST)
    @ResponseBody
    public String update(
            @ModelAttribute FormBoard form) {
        serviceBoard.update(form);
        return "redirect:/admin/board";
    }

    /**
     *
     * @param form
     * @return
     */
    @RequestMapping(value = "/{boardId}", method = RequestMethod.DELETE, produces = MediaType.TEXT_HTML_VALUE)
    public ResponseEntity delete(
            @ModelAttribute FormBoard form) {
        serviceBoard.delete(form);
        return new ResponseEntity(HttpStatus.OK);
    }

    /**
     *
     * @param form
     * @return
     */
    @RequestMapping(value = "/create", method = {RequestMethod.GET, RequestMethod.POST})
    public Object create(
            @ModelAttribute FormBoard form
    ) {
        switch (getRequestMethod()) {
            case POST:
                FormError error = validationBoard.validateCreate(form);
                if (error != null) {
                    return new ResponseEntity(error, HttpStatus.BAD_REQUEST);
                }
                serviceBoard.create(form);
                return new ResponseEntity(HttpStatus.OK);
            case GET:
            default:
                return BASE_TEMPLATE + "create";
        }
    }

}
