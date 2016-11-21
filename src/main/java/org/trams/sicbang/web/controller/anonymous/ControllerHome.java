package org.trams.sicbang.web.controller.anonymous;

import org.springframework.boot.actuate.health.Status;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.trams.sicbang.model.dto.Response;
import org.trams.sicbang.model.entity.Board;
import org.trams.sicbang.model.entity.Notice;
import org.trams.sicbang.model.entity.Slide;
import org.trams.sicbang.model.entity.User;
import org.trams.sicbang.model.form.FormBoard;
import org.trams.sicbang.model.form.FormNotice;
import org.trams.sicbang.model.form.FormSlide;
import org.trams.sicbang.web.controller.AbstractController;

import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Created by voncount on 4/6/16.
 */
@Controller(value = "anonymousControllerHome")
public class ControllerHome extends AbstractController {

    final String BASE_TEMPLATE = "web/content/";


    /**
     * Redirect to home
     * @param map
     * @return
     */
    @RequestMapping(value = "/", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
    public String index(ModelMap map, FormBoard formBoard, FormNotice formNotice) throws IOException {
        formBoard.setPageIndex(0);
        formBoard.setPageSize(5);
        Page<Board> boards = serviceBoard.filter(formBoard);
        map.put("boards",boards);
        formNotice.setPageSize(5);
        formNotice.setPageIndex(0);
        Page<Notice> notices = serviceNotice.filter(formNotice);
        map.put("notices",notices);
        return "web/index";
    }

    @RequestMapping(value = "/getAllSlide", method = RequestMethod.GET)
    public ResponseEntity slide(FormSlide formSlide) throws IOException {
        formSlide.setType("WEB");
        formSlide.setIsDelete(0);
        formSlide.setPageIndex(0);
        Page<Slide> slides = serviceSlide.filter(formSlide);
        return new ResponseEntity(slides, HttpStatus.OK);
    }

}
