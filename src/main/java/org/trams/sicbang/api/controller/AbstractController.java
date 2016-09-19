package org.trams.sicbang.api.controller;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.trams.sicbang.common.utils.ServletUtils;
import org.trams.sicbang.config.ConfigParams;
import org.trams.sicbang.model.dto.CustomUserDetail;
import org.trams.sicbang.model.enumerate.MessageResponse;
import org.trams.sicbang.model.exception.ApplicationException;
import org.trams.sicbang.service.*;
import org.trams.sicbang.service.security.RestUserDetailService;
import org.trams.sicbang.validation.ValidationEstate;
import org.trams.sicbang.validation.ValidationReport;
import org.trams.sicbang.validation.ValidationUser;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by voncount on 4/8/16.
 */
public class AbstractController {

    protected final Logger logger = Logger.getLogger(this.getClass());

    @Autowired
    protected HttpServletRequest servletRequest;
    @Autowired
    protected ConfigParams configParams;

    @Autowired
    protected IServiceUser serviceUser;
    @Autowired
    protected IServiceEstate serviceEstate;
    @Autowired
    protected IServiceLocation serviceLocation;
    @Autowired
    protected IServiceBoard serviceBoard;
    @Autowired
    protected IServiceNotice serviceNotice;
    @Autowired
    protected IServiceReport serviceReport;
    @Autowired
    protected IServiceWishlist serviceWishlist;
    @Autowired
    protected IServiceRecent serviceRecent;
    @Autowired
    protected IServiceAsk serviceAsk;
    @Autowired
    protected IServiceFile serviceFile;
    @Autowired
    protected IServicebusinessZone serviceBusinessZone;
    @Autowired
    protected RestUserDetailService userDetailService;
    @Autowired
    protected ValidationUser validationUser;
    @Autowired
    protected ValidationEstate validationEstate;
    @Autowired
    protected ValidationReport validationReport;
    @Autowired
    protected IServiceSlide serviceSlide;

    protected CustomUserDetail getUserDetail() {
        try {
            return ServletUtils.extractCredential(servletRequest);
        } catch (Exception e) {
            throw new ApplicationException(MessageResponse.EXCEPTION_UNAUTHORIZED);
        }
    }

}
