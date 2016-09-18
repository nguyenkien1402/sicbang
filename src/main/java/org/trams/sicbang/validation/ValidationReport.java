package org.trams.sicbang.validation;

import com.google.common.base.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.trams.sicbang.common.utils.ConvertUtils;
import org.trams.sicbang.model.enumerate.MessageResponse;
import org.trams.sicbang.model.exception.FormError;
import org.trams.sicbang.model.form.FormReport;
import org.trams.sicbang.model.form.FormReportAnswer;
import org.trams.sicbang.repository.RepositoryEstate;
import org.trams.sicbang.repository.RepositoryReportInformation;
import org.trams.sicbang.repository.RepositoryUser;

import java.util.Optional;

/**
 * Created by voncount on 26/04/2016.
 */
@Component
public class ValidationReport {

    @Autowired
    private RepositoryUser repositoryUser;
    @Autowired
    private RepositoryEstate repositoryEstate;
    @Autowired
    private RepositoryReportInformation repositoryReportInformation;

    public FormError validateCreateReport(FormReport form) {
        FormError error = new FormError();

        String name = form.getName();
        String cellphone = form.getCellphone();
        String content = form.getContent();
        String userId = form.getUserId();
        String estateId = form.getEstateId();

        if (Strings.isNullOrEmpty(name)) {
            error.rejectValue("name", MessageResponse.EXCEPTION_FIELD_INVALID.getMessage());
        }
        if (Strings.isNullOrEmpty(cellphone)) {
            error.rejectValue("cellphone", MessageResponse.EXCEPTION_FIELD_INVALID.getMessage());
        }
        if (Strings.isNullOrEmpty(content)) {
            error.rejectValue("content", MessageResponse.EXCEPTION_FIELD_INVALID.getMessage());
        }
        if (repositoryUser.findOne(Long.parseLong(userId)) == null) {
            error.rejectValue("name", MessageResponse.EXCEPTION_NOT_FOUND.getMessage());
        }
        if (repositoryEstate.findOne(Long.parseLong(estateId)) == null) {
            error.rejectValue("name", MessageResponse.EXCEPTION_NOT_FOUND.getMessage());
        }

        return error.hasErrors() ? error : null;
    }

    public FormError validateCreateReportAnswer(FormReportAnswer form) {
        FormError error = new FormError();

        String title = form.getTitle();
        String content = form.getContent();
        String reportId = form.getReportId();
        Optional<Long> _reportId = ConvertUtils.toLongNumber(form.getReportId());

        if (Strings.isNullOrEmpty(title)) {
            error.rejectValue("title", MessageResponse.EXCEPTION_FIELD_INVALID.getMessage());
        }
        if (Strings.isNullOrEmpty(content)) {
            error.rejectValue("content", MessageResponse.EXCEPTION_FIELD_INVALID.getMessage());
        }
        if (_reportId.isPresent()) {
            if (repositoryReportInformation.findOne(_reportId.get()) == null) {
                error.rejectValue("title", MessageResponse.EXCEPTION_NOT_FOUND.getMessage());
            }
        } else {
            error.rejectValue("title", MessageResponse.EXCEPTION_NOT_FOUND.getMessage());
        }

        return error.hasErrors() ? error : null;
    }

}
