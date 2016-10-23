package org.trams.sicbang.api.controller;

import com.google.common.base.Strings;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.trams.sicbang.common.utils.EncryptionUtils;
import org.trams.sicbang.model.dto.CustomUserDetail;
import org.trams.sicbang.model.dto.Response;
import org.trams.sicbang.model.entity.*;
import org.trams.sicbang.model.enumerate.CommonStatus;
import org.trams.sicbang.model.enumerate.MessageResponse;
import org.trams.sicbang.model.enumerate.UserType;
import org.trams.sicbang.model.enumerate.UserTypePermission;
import org.trams.sicbang.model.exception.FormError;
import org.trams.sicbang.model.form.*;
import org.trams.sicbang.validation.ValidationEstate;

import java.util.List;

/**
 * Created by voncount on 4/8/16.
 */
@Api(description = "Public API, does not need to authenticate")
@RestController
@RequestMapping("/api/public")
@SuppressWarnings({"rawtype", "unchecked"})
public class PublicApi extends AbstractController {

    // ------------------------------------------------------
    // ---------------------- User --------------------------
    // ------------------------------------------------------
    @ApiOperation(
            value = "User login - slide 27",
            notes = "Request:" +
                    "<ol>" +
                    "<li>username</li>" +
                    "<li>password</li>" +
                    "<li>rememberme - on|null</li>" +
                    "</ol>" +
                    "Response:" +
                    "<ol>" +
                    "<li>authenticated key</li>" +
                    "</ol>")
    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "/login", method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE,
            consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Response userLogin(@RequestBody FormLogin form) {
        FormError error = validationUser.validateLogin(form);
        System.out.println("call login");
        if (error != null) {
            return new Response(MessageResponse.EXCEPTION_BAD_REQUEST, error);
        }
        CustomUserDetail userDetail = serviceUser.authenticateUser(form);
        if(userDetail.getType().equals(UserType.NON_BROKER.name())) {
            error = new FormError();
            error.rejectValue("message",MessageResponse.EXCEPTION_NOT_TRUST_BROKER.getMessage());
            return new Response(MessageResponse.EXCEPTION_NOT_TRUST_BROKER, error);
        }
        logger.info(userDetail.getUserId());
        return new Response(MessageResponse.OK, EncryptionUtils.jwtBuild(userDetail));
    }

    @ApiOperation(
            value = "User create - slide 28",
            notes = "Request:<br/>" +
                    "<strong>Normal user join - slide 29</strong>" +
                    "<ol>" +
                    "<li>email</li>" +
                    "<li>password</li>" +
                    "<li>passwordConfirm</li>" +
                    "<li>type - MEMBER</li>" +
                    "</ol>" +
                    "<hr/>" +
                    "<strong>" +
                    " user join - slide 30</strong>" +
                    "<ol>" +
                    "<li>email</li>" +
                    "<li>password</li>" +
                    "<li>passwordConfirm</li>" +
                    "<li>type - BROKER</li>" +
                    "<li>corporationRegistration</li>" +
                    "<li>username</li>" +
                    "<li>companyName</li>" +
                    "<li>townAddress</li>" +
                    "<li>addressDetail</li>" +
                    "<li>phoneNumber</li>" +
                    "<li>cellphoneNumber</li>" +
                    "<li>base64image</li>" +
                    "</ol>" +
                    "Response:")
    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(value = "/user", method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE,
            consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Response userCreate(@RequestBody FormUser form) {

        // default value
        System.out.println("===============================");
        System.out.println("api join user - broker: "+form.getType());
        form.setRole("MEMBER");
        if(form.getType().equals("MEMBER")){
            form.setType("NON_BROKER");
        }
        form.setStatus(CommonStatus.ACTIVE.name());
        FormError error = validationUser.validateCreate(form);
        if (error != null) {
            return new Response(MessageResponse.EXCEPTION_BAD_REQUEST, error);
        }

        User user = serviceUser.create(form);
        return new Response(MessageResponse.OK, user);
    }

    @ApiOperation(
            value = "User ask - page 44",
            notes = "Request:" +
                    "<ol>" +
                    "<li>title</li>" +
                    "<li>content</li>" +
                    "<li>name</li>" +
                    "<li>contact</li>" +
                    "</ol>")
    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "/ask", method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE,
            consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Response userAsk(@RequestBody FormAsk form) {
        FormError error = validationUser.validateAsk(form);
        if (error != null) {
            return new Response(MessageResponse.EXCEPTION_BAD_REQUEST, error);
        }

        return new Response(MessageResponse.OK, serviceAsk.create(form));
    }
    // ------------------------------------------------------
    // -------------------- End User ------------------------
    // ------------------------------------------------------

    @ApiOperation(value = "City filter")
    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "/city", method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Response cityFilter() {
        List<City> cities = serviceLocation.findAllCity();
        return new Response(MessageResponse.OK, cities);
    }

    @ApiOperation(value = "Category filter")
    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "/category", method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Response categoryFilter() {
        List<Category> categories = serviceLocation.findAllCategory();
        return new Response(MessageResponse.OK, categories);

    }

    // ------------------------------------------------------
    // ---------------------- Board -------------------------
    // ------------------------------------------------------
    @ApiOperation(value = "Board filter - slide 39")
    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "/board", method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Response boardFilter(@ModelAttribute FormBoard form) {
        Page<Board> boards = serviceBoard.filter(form);
        return new Response(MessageResponse.OK, boards);
    }
    // ------------------------------------------------------
    // -------------------- End Board -----------------------
    // ------------------------------------------------------

    // ------------------------------------------------------
    // ---------------------- Notice ------------------------
    // ------------------------------------------------------
    @ApiOperation(value = "Notice filter - slide 40")
    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "/notice", method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Response noticeFilter(@ModelAttribute FormNotice form) {
        Page<Notice> notices = serviceNotice.filter(form);
        return new Response(MessageResponse.OK, notices);
    }
    // ------------------------------------------------------
    // -------------------- End Notice ----------------------
    // ------------------------------------------------------

    // --------------------------------------------------------
    // ---------------------- Estate --------------------------
    // --------------------------------------------------------
    @ApiOperation(
            value = "Estate filter - slide 7,8,11",
            notes = "using for both anonymous & authenticated user")
    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "/estate", method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Response estateFilter(
            @RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false) String authorization,
            @ModelAttribute FormEstate form) {

        Page<Estate> estates;
        if (!Strings.isNullOrEmpty(authorization)) {
            logger.info("isLogin : " + true);
            estates = serviceEstate.filter(form, getUserDetail());
        } else {
            logger.info("isLogin : " + false);
            logger.info(form.toString());
            estates = serviceEstate.filter(form);
        }
        logger.info(estates);
        return new Response(MessageResponse.OK, estates);
    }

    @ApiOperation(
            value = "Estate detail - slide 15,18",
            notes = "using for both anonymous & authenticated user")
    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "/estate/{estateId}", method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Response estateDetail(
            @RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false) String authorization,
            @PathVariable(value = "estateId") String estateid) {
        logger.info("estateid : " + estateid);
        FormEstate form = new FormEstate();
        form.setEstateId(estateid);

        Page<Estate> page = serviceEstate.filter(form);
        if (!page.iterator().hasNext()) {
            return new Response(MessageResponse.EXCEPTION_NOT_FOUND);
        } else {
            Estate estate = page.iterator().next();

            logger.info("estate.getId() : " + estate.getId());

            if (!Strings.isNullOrEmpty(authorization)) {
                FormRecent formRecent = new FormRecent();
                formRecent.setUserId(getUserDetail().getUserId());
                formRecent.setEstateId(estateid);

                if (!serviceRecent.filter(formRecent).iterator().hasNext()) {
                    serviceRecent.create(formRecent);
                }

                FormWishlist formWishlist = new FormWishlist();
                formWishlist.setUserId(getUserDetail().getUserId());
                formWishlist.setEstateId(estateid);
                Wishlist wishlist = serviceWishlist.findOne(formWishlist);
                estate.setWishlist(wishlist != null);
            }

            return new Response(MessageResponse.OK, estate);
        }
    }
    // --------------------------------------------------------
    // -------------------- End Estate ------------------------
    // --------------------------------------------------------

    /**
     * 상권검색, 시도, 구군, 동 내려주기
     *
     * @return
     */
    @ApiOperation(
            value = "상권 검색-> 시/도, 구/군, 동/리",
            notes = "using for both anonymous & authenticated user")
    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "/businessZone", method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Response businessZone(
            @RequestParam(required = true, value = "city") String city,
            @RequestParam(required = false, value = "town") String town,
            @ModelAttribute FormBusiness form) {
        logger.info("cityDegree :" + form.getCityDegree());
        logger.info("city :" + city);
        List<String> sangguns = null;

        if ((town != null) && (town.length() != 0)) {
            //읍면동리 내려주기
            sangguns = serviceBusinessZone.findDongList(city, town);
        } else {
            sangguns = serviceBusinessZone.findTownList(city);
        }

        return new Response(MessageResponse.OK, sangguns);
    }


    @ApiOperation(
            value = "Get Image of Slide Banner for Mobile App",
            notes = "No process page and quantity")
    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value="/slide", method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Response slideBanner(
            @ModelAttribute FormSlide form
    ){
        logger.info("api: " + "get list of slide banner");
        Page<Slide> slide = null;
        form.setType("APP");
        slide = serviceSlide.filter(form);
        if (!slide.iterator().hasNext()) {
            return new Response(MessageResponse.EXCEPTION_NOT_FOUND);
        } else {
            return new Response(MessageResponse.OK, slide);
        }
    }


    @ApiOperation(
            value = "Get pop up for api trust",
            notes = "No process page and quantity")
    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value="/popup", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Response popupTrust(
            @ModelAttribute FormSlide form
    ){
        logger.info("api: " + "get list of slide banner");
        Slide slide = null;
        slide = serviceSlide.fileterPopup(form);
        if (slide == null) {
            return new Response(MessageResponse.EXCEPTION_NOT_FOUND);
        } else {
            return new Response(MessageResponse.OK, slide);
        }
    }
}
