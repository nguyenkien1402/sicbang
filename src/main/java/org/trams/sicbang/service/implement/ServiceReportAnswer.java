package org.trams.sicbang.service.implement;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.trams.sicbang.model.entity.Ask;
import org.trams.sicbang.model.entity.ReportAnswer;
import org.trams.sicbang.model.entity.ReportInformation;
import org.trams.sicbang.model.enumerate.MessageResponse;
import org.trams.sicbang.model.exception.ApplicationException;
import org.trams.sicbang.model.form.FormReportAnswer;
import org.trams.sicbang.repository.RepositoryEstate;
import org.trams.sicbang.repository.RepositoryReportInformation;
import org.trams.sicbang.repository.RepositoryUser;
import org.trams.sicbang.service.BaseService;
import org.trams.sicbang.service.IServiceReportAnswer;

/**
 * Created by voncount on 22/04/2016.
 */
@Service
@Transactional
public class ServiceReportAnswer extends BaseService implements IServiceReportAnswer {

    @Autowired
    private RepositoryUser repositoryUser;
    @Autowired
    private RepositoryEstate repositoryEstate;
    @Autowired
    private RepositoryReportInformation repositoryReportInformation;

    @Override
    public ReportAnswer create(FormReportAnswer form) {

        String askId = form.getAskId();
        Ask ask = repositoryAsk.findOne(Long.parseLong(askId));
        System.out.println("ask title: "+ask.getTitle());
        ReportAnswer reportAnswer = new ReportAnswer();
        reportAnswer.setAsk(ask);
        reportAnswer.setContent(form.getContent());
        reportAnswer.setTitle(ask.getTitle());
//        ReportInformation reportInformation = repositoryReportInformation.findOne(Long.parseLong(reportId));

//        ReportAnswer reportAnswer = new ReportAnswer();
//        reportAnswer.setReportInformation(reportInformation);
//        BeanUtils.copyProperties(form, reportAnswer);

        return repositoryReportAnswer.save(reportAnswer);
    }

    @Override
    public ReportAnswer update(FormReportAnswer form) {
        ReportAnswer answer = repositoryReportAnswer.findOne(form.getSpecification());
        if (answer == null) {
            throw new ApplicationException(MessageResponse.EXCEPTION_NOT_FOUND);
        }
        BeanUtils.copyProperties(form, answer);
        return repositoryReportAnswer.save(answer);
    }

    @Override
    public Page<ReportAnswer> filter(FormReportAnswer form) {
        return repositoryReportAnswer.findAll(form.getSpecification(), form.getPaging());
    }

    @Override
    public void delete(FormReportAnswer form) {
        ReportAnswer report = repositoryReportAnswer.findOne(form.getSpecification());
        report.setIsDelete(1);
    }

    @Override
    public ReportAnswer findOne(FormReportAnswer form) {
        return repositoryReportAnswer.findOne(form.getSpecification());
    }

}
