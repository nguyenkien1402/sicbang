package org.trams.sicbang.web.controller.admin;

import com.google.common.base.Strings;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.trams.sicbang.common.utils.CommonEmailType;
import org.trams.sicbang.model.entity.Board;
import org.trams.sicbang.model.entity.Mail;
import org.trams.sicbang.model.entity.Slide;
import org.trams.sicbang.model.entity.User;
import org.trams.sicbang.model.exception.FormError;
import org.trams.sicbang.model.form.FormBoard;
import org.trams.sicbang.model.form.FormMail;
import org.trams.sicbang.model.form.FormSlide;
import org.trams.sicbang.web.controller.AbstractController;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by voncount on 15/04/2016.
 */
@Controller(value = "adminControllerMain")
@RequestMapping(value = "/admin/main")
public class ControllerMain extends AbstractController {

    final String BASE_URL = "/admin/main/";
    final String BASE_TEMPLATE = "admin/content/main/";

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
        System.out.println("==========================");
        System.out.println("call main");
        System.out.println("==========================");
        Page<Board> boards = serviceBoard.filter(form);
        map.put("items", boards);
        FormSlide formSlide = new FormSlide();
        Page<Slide> slides = serviceSlide.filter(formSlide);
        map.put("slides",slides);
        return BASE_TEMPLATE + "main";
    }

    /**
     *
     * @param formSlide
     * @param map
     * @return
     */
    @RequestMapping(value="/popup", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
    public String popup(
            @ModelAttribute FormSlide formSlide,
            ModelMap map) {
        System.out.println("==========================");
        System.out.println("call popup");
        System.out.println("==========================");
        return BASE_TEMPLATE + "main_popup";
    }

    /**
     *
     * @param formMail
     * @param map
     * @return
     */
    @RequestMapping(value="/history", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
    public String history(
            @ModelAttribute FormMail formMail,
            ModelMap map) {
        System.out.println("==========================");
        System.out.println("call history");
        Page<Mail> emails = serviceMail.filter(formMail);
        for (int i = 0; i < emails.getNumberOfElements(); i++) {
            try {
                System.out.println("Decode 2" + emails.getContent().get(i).getMailContent());
                emails.getContent().get(i).decodeContent();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        if(emails.getNumberOfElements() != 0)
            System.out.println("Decode :"+emails.getContent().get(0).getMailContent());
        map.put("emails",emails);
        System.out.println("==========================");
        return BASE_TEMPLATE + "main_email_history";
    }

    /**
     *
     * @param formMail
     * @param map
     * @return
     */
    @RequestMapping(value="/email/{type}", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
    public String email(
            @ModelAttribute FormMail formMail,
            @PathVariable("type") String type,
            ModelMap map) {
        System.out.println("==========================");
        System.out.println("call email");
        System.out.println("type: "+type);
        List<User> listUser = new ArrayList<>();
        String email = "";
        if(type != null){
            switch (type){
                case "0":
                    map.put("type","0");
                    break;
                case "1": // mean sign up today
                    map.put("type","1");
                    listUser = serviceUser.filterBy(type);
                    email = CommonEmailType.toStringEmails(listUser);
                    break;
                case "2": // mean sign up last month
                    map.put("type","2");
                    listUser = serviceUser.filterBy(type);
                    email = CommonEmailType.toStringEmails(listUser);
                    break;
                case "3": // mean regular memeber
                    map.put("type","3");
                    listUser = serviceUser.filterBy(type);
                    email = CommonEmailType.toStringEmails(listUser);
                    break;
                case "4": // mean broker
                    map.put("type","4");
                    listUser = serviceUser.filterBy(type);
                    email = CommonEmailType.toStringEmails(listUser);
                    break;
                case "5": // mean approved broker
                    map.put("type","5");
                    listUser = serviceUser.filterBy(type);
                    email = CommonEmailType.toStringEmails(listUser);
                    break;
                case "6": //mean all member
                    map.put("type","6");
                    listUser = serviceUser.filterBy(type);
                    email = CommonEmailType.toStringEmails(listUser);
                    break;

            }
            map.put("formEmail",formMail);
            map.put("mailsTo",email);
        }else{
        }
        System.out.println("==========================");
        return BASE_TEMPLATE + "main_email_2";
    }

    /**
     *
     * @return
     */
    @RequestMapping(value = "/create/popup/{type}", method = RequestMethod.POST, produces = MediaType.TEXT_HTML_VALUE)
    public String createPopup(
            @ModelAttribute FormSlide formSlide,
            @PathVariable(value="type") String type,
            ModelMap map
    ){
        System.out.println("====================================");
        System.out.println("come here first");
        System.out.println("type:"+type);
        System.out.println("link: "+formSlide.getLink());
        System.out.println("attachment: "+formSlide.getAttachments().getOriginalFilename());
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Slide slide = serviceSlide.uploadSlide(formSlide, username);
        System.out.println("====================================");
        return "redirect:/admin/main/popup";
    }

    /**
     *
     * @return
     */
    @RequestMapping(value = "/create/slide/{type}", method = RequestMethod.POST, produces = MediaType.TEXT_HTML_VALUE)
    public String createWebSlideImg(
            @ModelAttribute FormSlide formSlide,
            @PathVariable(value="type") String type,
            ModelMap map
    ){
        System.out.println("====================================");
        System.out.println("come here first");
        System.out.println("type:"+type);
        System.out.println("link: "+formSlide.getLink());
        System.out.println("attachment: "+formSlide.getAttachments().getOriginalFilename());
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Slide slide = serviceSlide.uploadSlide(formSlide, username);
        System.out.println("====================================");
        return "redirect:/admin/main";
    }

//    /**
//     *
//     * @param form
//     * @param map
//     * @return
//     */
//    @RequestMapping(value = "/{boardId}", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
//    public String detail(
//            @ModelAttribute FormBoard form,
//            ModelMap map) {
//        Board board = serviceBoard.findOne(form);
//        map.put("board", board);
//        return BASE_TEMPLATE + "update";
//    }
//
//    /**
//     *
//     * @param form
//     * @return
//     */
//    @RequestMapping(value = "/{boardId}", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
//    @ResponseBody
//    public ResponseEntity update(
//            @ModelAttribute FormBoard form) {
//        serviceBoard.update(form);
//        return new ResponseEntity(HttpStatus.OK);
//    }
//
//    /**
//     *
//     * @param form
//     * @return
//     */
//    @RequestMapping(value = "/{boardId}", method = RequestMethod.DELETE, produces = MediaType.TEXT_HTML_VALUE)
//    public ResponseEntity delete(
//            @ModelAttribute FormBoard form) {
//        serviceBoard.delete(form);
//        return new ResponseEntity(HttpStatus.OK);
//    }


    // --------------------------------------------------------
    // ----------------------- Mail ---------------------------
    // --------------------------------------------------------
    /**
     *
     * @param form
     * @param map
     * @return
     */
    @RequestMapping(value = "/mail", method = RequestMethod.GET,
            produces = MediaType.TEXT_HTML_VALUE)
    public String mailFilter(
            @ModelAttribute FormMail form,
            ModelMap map) {
        Page<Mail> mails = serviceMail.filter(form);
        map.put("items", mails);
        return BASE_TEMPLATE + "mail/list";
    }

    /**
     *
     * @param form
     * @param map
     * @return
     */
    @RequestMapping(value = "/mail/create", method = {RequestMethod.POST, RequestMethod.GET})
    public Object mailCreatePost(
            @ModelAttribute FormMail form,
            ModelMap map) {
        logger.info("mailCreatePost form  : "+form.toString());
        System.out.println("type mail: "+form.getType());
        if(Strings.isNullOrEmpty(form.getType())){
            switch (getRequestMethod()) {
                case POST:
                    FormError error = validationEmail.validateCreate(form);
                    if (error != null) {
                        return new ResponseEntity(error, HttpStatus.BAD_REQUEST);
                    }
                    serviceMail.send(form);
                    return "redirect:/admin/main/email/0";
                case GET:
                default:
                    return "redirect:/admin/main/email/0";
            }
        }else{
            switch (getRequestMethod()) {
                case POST:
                    FormError error = validationEmail.validateCreate(form);
                    if (error != null) {
                        return new ResponseEntity(error, HttpStatus.BAD_REQUEST);
                    }
                    serviceMail.sendMulti(form);
                    return "redirect:/admin/mail/create";
                case GET:
                default:
                    return "redirect:/admin/mail/create";
            }
        }

    }

    @RequestMapping(value="/mail/{id}", method = RequestMethod.DELETE, produces = MediaType.TEXT_HTML_VALUE)
    public String mailDelete(@PathVariable(value = "id") String id){
        System.out.println("id: "+id);
        FormMail formMail = new FormMail();
        formMail.setId(id);
        serviceMail.delete(formMail);
        return "redirect:/admin/mail/create";
    }

    @RequestMapping(value ="/mail/deletearr", method = RequestMethod.POST, produces = MediaType.TEXT_HTML_VALUE)
    public String mailArrDelete(@ModelAttribute FormMail form){
        System.out.println("=================================");
        System.out.println("call delete multi mail");
        int[] arr = form.getArrDelete();
        System.out.println("size length: "+arr.length);
        for(int i = 0 ; i < arr.length ; i++){
            FormMail formMail = new FormMail();
            formMail.setId(arr[i]+"");
            System.out.println("pos: "+i+" - "+arr[i]);
            serviceMail.delete(formMail);
        }
        System.out.println("=================================");
        return "redirect:/admin/main/history";
    }
    // --------------------------------------------------------
    // --------------------- End Mail -------------------------
    // --------------------------------------------------------

    @RequestMapping(value="/delete/web", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
    public String deleteSlideWeb(){
        FormSlide formSlide = new FormSlide();
        formSlide.setType("WEB");
        List<Slide> list = serviceSlide.filterPopup(formSlide);
        if(list != null && list.size() > 1) {
            FormSlide delete = new FormSlide();
            delete.setId(list.get(list.size()- 1).getId()+"");
            serviceSlide.delete(delete);
        }
        return "redirect:/admin/main";
    }

    @RequestMapping(value="/delete/app", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
    public String deleteSlideApp(){
        FormSlide formSlide = new FormSlide();
        formSlide.setType("APP");
        List<Slide> list = serviceSlide.filterPopup(formSlide);
        if(list != null && list.size() > 1) {
            FormSlide delete = new FormSlide();
            delete.setId(list.get(list.size()- 1).getId()+"");
            serviceSlide.delete(delete);
        }
        return "redirect:/admin/main";
    }
}
