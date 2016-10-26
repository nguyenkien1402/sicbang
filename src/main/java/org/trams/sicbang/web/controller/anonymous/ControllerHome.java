package org.trams.sicbang.web.controller.anonymous;

import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.trams.sicbang.model.entity.Board;
import org.trams.sicbang.model.entity.Notice;
import org.trams.sicbang.model.entity.User;
import org.trams.sicbang.model.form.FormBoard;
import org.trams.sicbang.model.form.FormNotice;
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
    public String index(ModelMap map) throws IOException {

        FormBoard formBoard = new FormBoard();
        formBoard.setPageIndex(0);
        formBoard.setPageSize(5);
        Page<Board> boards = serviceBoard.filter(formBoard);
        map.put("boards",boards);
        FormNotice formNotice = new FormNotice();
        formNotice.setPageSize(5);
        formNotice.setPageIndex(0);
        Page<Notice> notices = serviceNotice.filter(formNotice);
        map.put("notices",notices);
        return "web/index";
    }


}
