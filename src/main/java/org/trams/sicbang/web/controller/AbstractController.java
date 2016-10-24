package org.trams.sicbang.web.controller;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;
import org.trams.sicbang.model.entity.User;
import org.trams.sicbang.service.*;
import org.trams.sicbang.service.implement.ServiceAuthorized;
import org.trams.sicbang.validation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * Created by voncount on 4/6/16.
 */
@SuppressWarnings({"unchecked","rawtype"})
public class AbstractController {

    @Autowired
    protected HttpServletRequest httpRequest;
    @Autowired
    private SessionLocaleResolver localeResolver;
    @Autowired
    private MessageSource messageSource;

    @Autowired
    protected IServiceUser serviceUser;
    @Autowired
    protected IServiceEstate serviceEstate;
    @Autowired
    protected IServiceLocation serviceLocation;
    @Autowired
    protected IServiceReport serviceReport;
    @Autowired
    protected IServiceRecent serviceRecent;
    @Autowired
    protected IServiceReportAnswer serviceReportAnswer;
    @Autowired
    protected IServiceBoard serviceBoard;
    @Autowired
    protected IServiceNotice serviceNotice;
    @Autowired
    protected IServiceMail serviceMail;
    @Autowired
    protected IServiceSlide serviceSlide;
    @Autowired
    protected IServiceWishlist serviceWishlist;
    @Autowired
    protected IServiceBusinessType serviceBusinessType;
    @Autowired
    protected ServiceAuthorized serviceAuthorized;

    @Autowired
    protected ValidationUser validationUser;
    @Autowired
    protected ValidationEstate validationEstate;
    @Autowired
    protected ValidationReport validationReport;
    @Autowired
    protected ValidationBoard validationBoard;
    @Autowired
    protected ValidationEmail validationEmail;
    @Autowired
    protected ValidationNotice validationNotice;
    @Autowired
    protected IServiceCity serviceCity;
    @Autowired
    protected IServiceDistrict serviceDistrict;
    @Autowired
    protected IServiceTown serviceTown;
    @Autowired
    protected IServiceAsk serviceAsk;


    protected final Logger logger = Logger.getLogger(this.getClass());

    protected String getMessage(String key) {
        return messageSource.getMessage(key, null, "N/A", localeResolver.resolveLocale(httpRequest));
    }

    protected RequestMethod getRequestMethod() {
        return RequestMethod.valueOf(httpRequest.getMethod());
    }

    @ModelAttribute(value = "uri")
    public String uri() {
        String baseUri = httpRequest.getRequestURI();
        String query = httpRequest.getQueryString();

        if (query == null) {
            return baseUri;
        } else {
            query = query.replaceAll("(pageIndex=\\d*&|&pageIndex=\\d*|pageIndex=\\d*$)", "");
            if (query.equals("")) return baseUri;
            else return baseUri + "?" + query;
        }
    }

    protected void isSession(){
        Authentication auth = serviceAuthorized.isAuthenticated();
        if(auth != null) {
            UserDetails userDetails = (UserDetails) auth.getPrincipal();
            User user = serviceUser.findUserByEmail(userDetails.getUsername());
            HttpSession session = httpRequest.getSession();
            session.setAttribute("type", user.getType().name());
            session.setAttribute("USER_SESSION", user);

        }
    }
}
