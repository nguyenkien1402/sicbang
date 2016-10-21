package org.trams.sicbang.service;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.trams.sicbang.config.ConfigParams;
import org.trams.sicbang.repository.*;
import org.trams.sicbang.service.implement.ServiceAuthorized;
import org.trams.sicbang.service.implement.ServiceUser;

/**
 * Created by voncount on 4/11/16.
 */
public class BaseService {

    protected final Logger logger = Logger.getLogger(this.getClass());

    @Autowired
    protected ServiceAuthorized serviceAuthorized;
    @Autowired
    protected BCryptPasswordEncoder passwordEncoder;
    @Autowired
    protected ConfigParams configParams;

    @Autowired
    protected RepositoryUser repositoryUser;
    @Autowired
    protected RepositoryEstate repositoryEstate;
    @Autowired
    protected RepositoryReportInformation repositoryReportInformation;
    @Autowired
    protected RepositoryReportAnswer repositoryReportAnswer;
    @Autowired
    protected RepositoryWishlist repositoryWishlist;
    @Autowired
    protected RepositoryWithdrawal repositoryWithdrawal;
    @Autowired
    protected RepositoryUserRole repositoryUserRole;
    @Autowired
    protected RepositoryBoard repositoryBoard;
    @Autowired
    protected RepositoryNotice repositoryNotice;
    @Autowired
    protected RepositoryRecent repositoryRecent;
    @Autowired
    protected RepositoryAttachment repositoryAttachment;
    @Autowired
    protected RepositoryCategory repositoryCategory;
    @Autowired
    protected RepositoryBusinessType repositoryBusinessType;
    @Autowired
    protected RepositoryAsk repositoryAsk;
    @Autowired
    protected RepositoryCity repositoryCity;
    @Autowired
    protected RepositoryDistrict repositoryDistrict;
    @Autowired
    protected RepositoryTown repositoryTown;
    @Autowired
    protected RepositorySanggun repositorySanggun;
    @Autowired
    protected RepositoryAdmin repositoryAdmin;
    @Autowired
    protected RepositoryUserPermission repositoryUserPermission;
}
