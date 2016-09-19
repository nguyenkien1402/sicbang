package org.trams.sicbang.api.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.trams.sicbang.model.dto.Response;
import org.trams.sicbang.model.entity.*;
import org.trams.sicbang.model.enumerate.MessageResponse;
import org.trams.sicbang.model.exception.FormError;
import org.trams.sicbang.model.form.*;

import java.util.List;

/**
 * Created by voncount on 4/8/16.
 */
@Api(description = "Authenticated API, must login before using")
@RestController
@RequestMapping(value = "/api/auth")
@SuppressWarnings({"rawtype", "unchecked"})
public class AuthenticatedApi extends AbstractController {

    // ------------------------------------------------------
    // ---------------------- User --------------------------
    // ------------------------------------------------------
    @ApiOperation(
            value = "User detail")
    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "/user", method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Response userDetail(
            @RequestHeader(value = HttpHeaders.AUTHORIZATION) String authorization) {
        FormUser form = new FormUser();
        form.setUserId(getUserDetail().getUserId());
        User user = serviceUser.findOne(form);
        return new Response(MessageResponse.OK, user);
    }

    @ApiOperation(
            value = "User update settings - slide 36",
            notes = "Request:" +
                    "<ol>" +
                    "<li>username</li>" +
                    "<li>cellphoneNumber</li>" +
                    "</ol>" +
                    "Response:")
    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "/user", method = RequestMethod.PUT,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE,
            consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Response userUpdate(
            @RequestHeader(value = HttpHeaders.AUTHORIZATION) String authorization,
            @RequestBody FormUser form) {
        logger.info(form.getCellphoneNumber());
        form.setUserId(getUserDetail().getUserId());
        User updateUser = serviceUser.update(form);
        return new Response(MessageResponse.OK, updateUser);
    }

    @ApiOperation(
            value = "User withdraw - slide 38",
            notes = "Request:" +
                    "<ol>" +
                    "<li>password</li>" +
                    "<li>content</li>" +
                    "</ol>" +
                    "Response:")
    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "/user", method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE,
            consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Response userWithdraw(
            @RequestHeader(value = HttpHeaders.AUTHORIZATION) String authorization,
            @RequestBody FormWithdraw form) {
        form.setUserId(getUserDetail().getUserId());
        FormError error = validationUser.validateWithdraw(form);
        if (error != null) {
            return new Response(MessageResponse.EXCEPTION_BAD_REQUEST, error);
        }
        User user = serviceUser.withdraw(form);
        return new Response(MessageResponse.OK, user);
    }

    @ApiOperation(
            value = "User reset password - slide 37")
    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "/user/password", method = RequestMethod.PUT,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE,
            consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Response userResetPassword(
            @RequestHeader(value = HttpHeaders.AUTHORIZATION) String authorization,
            @RequestBody FormPassword form) {
        form.setUserId(getUserDetail().getUserId());
        FormError error = validationUser.validateResetPassword(form);
        if (error != null) {
            return new Response(MessageResponse.EXCEPTION_BAD_REQUEST, error);
        }
        User user = serviceUser.resetPassword(form);
        return new Response(MessageResponse.OK, user);
    }

    @ApiOperation(
            value = "User upload avatar",
            notes = "Request:" +
                    "<ol>" +
                    "<li>attachments: attachments[0]</li>" +
                    "</ol>")
    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "/user/avatar", method = RequestMethod.PUT,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Response userUploadAvatar(
            @RequestHeader(value = HttpHeaders.AUTHORIZATION) String authorization,
            @ModelAttribute FormFile form) {
        form.setOwnerId(getUserDetail().getUserId());
        Attachment attachment = serviceFile.userUploadAvatar(form);
        return new Response(MessageResponse.OK, attachment);
    }
    // ------------------------------------------------------
    // -------------------- End User ------------------------
    // ------------------------------------------------------

    // ----------------------------------------------------------
    // ---------------------- Wishlist --------------------------
    // ----------------------------------------------------------
    @ApiOperation(value = "Wishlist filter - slide 35")
    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "/wishlist", method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Response wishlistFilter(
            @RequestHeader(value = HttpHeaders.AUTHORIZATION) String authorization,
            @ModelAttribute FormWishlist form) {
        form.setUserId(getUserDetail().getUserId());

        Page<Wishlist> wishlists = serviceWishlist.filter(form);

        return new Response(MessageResponse.OK, wishlists);
    }


    @ApiOperation(
            value = "Wishlist create - slide 15,18 (operation 3)")
    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "/wishlist/{estateId}", method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Response wishlistCreate(
            @RequestHeader(value = HttpHeaders.AUTHORIZATION) String authorization,
            @PathVariable(value = "estateId") String estateId) {
        String userId = getUserDetail().getUserId();

        FormWishlist form = new FormWishlist();
        Wishlist wishlist = null;
        form.setUserId(userId);
        form.setEstateId(estateId);
        FormError error = validationEstate.validateWishlistAdd(form);

        if (serviceWishlist.findOne(form) != null) {
            Wishlist wishCreated = serviceWishlist.findOne(form);

            logger.info("estate Id : " + wishCreated.getEstate().getId() + "User ID : " + wishCreated.getUser().getId());
            logger.info("User ID : " + userId);

            FormError isAdded = validationEstate.existWishListAdded(wishCreated, getUserDetail());
            if (wishCreated.getUser().getId().toString().equals(userId)) {
                return new Response(MessageResponse.EXCEPTION_BAD_REQUEST, isAdded);
            }
        } else if (error != null) {
            return new Response(MessageResponse.EXCEPTION_BAD_REQUEST, error);
        } else {
            wishlist = serviceWishlist.create(form);
        }

        return new Response(MessageResponse.OK, wishlist);
    }

    @ApiOperation(
            value = "Wishlist delete")
    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "/wishlist/{estateId}", method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Response wishlistDelete(
            @RequestHeader(value = HttpHeaders.AUTHORIZATION) String authorization,
            @PathVariable(value = "estateId") String estateId) {
        FormWishlist form = new FormWishlist();
        form.setUserId(getUserDetail().getUserId());
        form.setEstateId(estateId);

        serviceWishlist.delete(form);
        return new Response(MessageResponse.OK);
    }
    // --------------------------------------------------------------
    // ---------------------- End Wishlist --------------------------
    // --------------------------------------------------------------

    // --------------------------------------------------------
    // ---------------------- Recent --------------------------
    // --------------------------------------------------------
    @ApiOperation(value = "Recent estate filter - slide 34")
    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "/recent", method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Response recentEstateFilter(
            @RequestHeader(value = HttpHeaders.AUTHORIZATION) String authorization,
            @ModelAttribute FormRecent form) {
        form.setUserId(getUserDetail().getUserId());

        Page<Recent> recents = serviceRecent.filter(form);
        return new Response(MessageResponse.OK, recents);
    }
    // --------------------------------------------------------
    // -------------------- End Recent ------------------------
    // --------------------------------------------------------

    // --------------------------------------------------------
    // ---------------------- Report --------------------------
    // --------------------------------------------------------
    @ApiOperation(value = "Report filter")
    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "/report", method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Response reportFilter(
            @RequestHeader(value = HttpHeaders.AUTHORIZATION) String authorization,
            @ModelAttribute FormReport form) {
        Page<ReportInformation> reportInformations = serviceReport.filter(form);
        return new Response(MessageResponse.OK, reportInformations);
    }

    @ApiOperation(
            value = "Report create - slide 23",
            notes = "Request:" +
                    "<ol>" +
                    "<li>name</li>" +
                    "<li>cellphone</li>" +
                    "<li>content</li>" +
                    "</ol>" +
                    "Response:")
    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "/report/{estateId}", method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE,
            consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Response reportCreate(
            @RequestHeader(value = HttpHeaders.AUTHORIZATION) String authorization,
            @PathVariable(value = "estateId") String estateId,
            @RequestBody FormReport form) {

        form.setUserId(getUserDetail().getUserId());
        form.setEstateId(estateId);
        FormError error = validationReport.validateCreateReport(form);
        if (error != null) {
            return new Response(MessageResponse.EXCEPTION_BAD_REQUEST, error);
        }
        serviceReport.create(form);
        return new Response(MessageResponse.OK);
    }

    @ApiOperation(value = "Report detail")
    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "/report/{estateId}", method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Response reportDetail(
            @RequestHeader(value = HttpHeaders.AUTHORIZATION) String authorization,
            @PathVariable(value = "estateId") String estateId) {
        FormReport form = new FormReport();
        form.setUserId(getUserDetail().getUserId());
        form.setEstateId(estateId);
        ReportInformation report = serviceReport.findOne(form);
        return new Response(MessageResponse.OK, report);
    }
    // --------------------------------------------------------
    // -------------------- End Report ------------------------
    // --------------------------------------------------------

    // --------------------------------------------------------
    // ---------------------- Estate --------------------------
    // --------------------------------------------------------
    @ApiOperation(
            value = "Estate create - slide 45",
            notes = "Request:<br/>" +
                    "<strong>Common fields</strong>" +
                    "<ol>" +
                    "<li>name</li>" +
                    "<li>businessZone</li>" +
                    "<li>subwayStation</li>" +
                    "<li>onelineComment</li>" +
                    "<li>detail</li>" +
                    "<li>floor</li>" +
                    "<li>latitude</li>" +
                    "<li>longitude</li>" +
                    "<li>attachments (Array multipart file - attachments[0], attachments[1],...)</li>" +
                    "</ol>" +
                    "<strong>Startup estate</strong>" +
                    "<ol>" +
                    "<li>estateType - STARTUP</li>" +
                    "<li>depositeCost</li>" +
                    "<li>rentCost</li>" +
                    "<li>premiumCost</li>" +
                    "<li>otherCost</li>" +
                    "<li>monthlyIncome</li>" +
                    "<li>monthlyTax</li>" +
                    "<li>gainRatio</li>" +
                    "<li>category(ID of the category)</li>" +
                    "<li>businessType(ID of the business type)</li>" +
                    "<li>area</li>" +
                    "</ol>" +
                    "<hr/>" +
                    "<strong>Vacant estate</strong>" +
                    "<ol>" +
                    "<li>estateType - VACANT</li>" +
                    "<li>currentBusiness</li>" +
                    "<li>constructDate (timestamp)</li>" +
                    "<li>availableDate (timestamp)</li>" +
                    "<li>loan</li>" +
                    "<li>contractArea</li>" +
                    "<li>exclusiveArea</li>" +
                    "<li>exclusiveRate</li>" +
                    "<li>parking</li>" +
                    "</ol>" +
                    "Response:")
    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(value = "/estate", method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Response estateCreate(
            @RequestHeader(value = HttpHeaders.AUTHORIZATION) String authorization,
            @RequestBody FormEstate form) {

        logger.info(form.toString());
        logger.info(getUserDetail().getUserId());
        form.setUserId(getUserDetail().getUserId());
        form.setIsAdvertised(String.valueOf(false));
        FormError error = validationEstate.validateCreate(form);
        if (error != null) {
            return new Response(MessageResponse.EXCEPTION_BAD_REQUEST, error);
        }

        Estate estate = serviceEstate.create(form);
        return new Response(MessageResponse.OK, estate);
    }

    @ApiOperation(value = "주소검색")
    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "/estate/map/{dong}", method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Response estateMap(
            @RequestHeader(value = HttpHeaders.AUTHORIZATION) String authorization,
            @PathVariable String dong) {
        FormEstate formEstate = new FormEstate();
        formEstate.setAll_addr(dong);
        Page<Estate> estates = serviceEstate.filterAddr(formEstate);
        return new Response(MessageResponse.OK, estates);
    }

    ;
    // --------------------------------------------------------
    // -------------------- End Estate ------------------------
    // --------------------------------------------------------

}
