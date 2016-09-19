package org.trams.sicbang.service.implement;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.trams.sicbang.model.entity.Estate;
import org.trams.sicbang.model.entity.ReportInformation;
import org.trams.sicbang.model.entity.User;
import org.trams.sicbang.model.form.FormReport;
import org.trams.sicbang.repository.RepositoryEstate;
import org.trams.sicbang.repository.RepositoryReportInformation;
import org.trams.sicbang.repository.RepositoryUser;
import org.trams.sicbang.service.BaseService;
import org.trams.sicbang.service.IServiceReport;


/**
 * Created by voncount on 22/04/2016.
 */
@Service
@Transactional
public class ServiceReport extends BaseService implements IServiceReport {

    @Autowired
    private RepositoryUser repositoryUser;
    @Autowired
    private RepositoryEstate repositoryEstate;
    @Autowired
    private RepositoryReportInformation repositoryReportInformation;

    @Override
    public ReportInformation create(FormReport form) {

        String userId = form.getUserId();
        String estateId = form.getEstateId();

        User user = repositoryUser.findOne(Long.parseLong(userId));
        Estate estate = repositoryEstate.findOne(Long.parseLong(estateId));

        ReportInformation reportInformation = new ReportInformation();
        BeanUtils.copyProperties(form, reportInformation);
        reportInformation.setUser(user);
        reportInformation.setEstate(estate);

        return repositoryReportInformation.save(reportInformation);
    }

    @Override
    public ReportInformation update(FormReport form) {
        return null;
    }

    @Override
    public Page<ReportInformation> filter(FormReport form) {
        return repositoryReportInformation.findAll(form.getSpecification(), form.getPaging());
    }

    @Override
    public void delete(FormReport form) {
        ReportInformation report = repositoryReportInformation.findOne(form.getSpecification());
        report.setIsDelete(1);
    }

    @Override
    public ReportInformation findOne(FormReport form) {
        return repositoryReportInformation.findOne(form.getSpecification());
    }

}
