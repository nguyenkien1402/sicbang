package org.trams.sicbang.web.controller.anonymous;

import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.trams.sicbang.model.entity.Board;
import org.trams.sicbang.model.form.FormBoard;
import org.trams.sicbang.web.controller.AbstractController;

/**
 * Created by voncount on 15/04/2016.
 */
@Controller(value = "anonymousControllerBoard")
@RequestMapping(value = "/board")
public class ControllerBoard extends AbstractController {

    final String BASE_URL = "/board/";
    final String BASE_TEMPLATE = "web/content/board/";

    /**
     * Filter
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
     * Detail
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

}
