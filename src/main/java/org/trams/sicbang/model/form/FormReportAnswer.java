package org.trams.sicbang.model.form;

import io.swagger.annotations.ApiModelProperty;
import org.springframework.data.jpa.domain.Specification;
import org.trams.sicbang.common.utils.ConvertUtils;
import org.trams.sicbang.model.dto.BaseFormSearch;
import org.trams.sicbang.model.entity.*;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Created by voncount on 4/13/16.
 */
public class FormReportAnswer extends BaseFormSearch<ReportAnswer> {

    @ApiModelProperty(hidden = true)
    private String reportAnswerId;
    @ApiModelProperty(hidden = true)
    private String reportId;
    private String title;
    private String content;

    public String getReportAnswerId() {
        return reportAnswerId;
    }

    public void setReportAnswerId(String reportAnswerId) {
        this.reportAnswerId = reportAnswerId;
    }

    public String getReportId() {
        return reportId;
    }

    public void setReportId(String reportId) {
        this.reportId = reportId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public Specification<ReportAnswer> getSpecification() {
        return (Root<ReportAnswer> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) -> {
            root.join(ReportAnswer_.reportInformation, JoinType.INNER);

            List<Predicate> predicates = new ArrayList<>();
            Optional<Long> _reportAnswerId = ConvertUtils.toLongNumber(reportAnswerId);
            Optional<Long> _reportId = ConvertUtils.toLongNumber(reportId);

            if (_reportAnswerId.isPresent()) {
                predicates.add(
                        criteriaBuilder.equal(root.get(ReportAnswer_.id), _reportAnswerId.get())
                );
            }
            if (_reportId.isPresent()) {
                predicates.add(
                        criteriaBuilder.equal(root.get(ReportAnswer_.reportInformation).get(ReportInformation_.id), _reportId.get())
                );
            }

            predicates.add(
                    criteriaBuilder.equal(root.get(ReportAnswer_.isDelete), isDelete)
            );

            if (predicates.isEmpty()) {
                return criteriaBuilder.isNotNull(root.get(ReportAnswer_.id));
            } else {
                return criteriaBuilder.and(predicates.toArray(new Predicate[] {}));
            }
        };
    }
}
