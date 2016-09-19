package org.trams.sicbang.model.form;

import com.google.common.base.Strings;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.data.jpa.domain.Specification;
import org.trams.sicbang.common.utils.ConvertUtils;
import org.trams.sicbang.model.dto.BaseFormSearch;
import org.trams.sicbang.model.entity.ReportInformation;
import org.trams.sicbang.model.entity.ReportInformation_;
import org.trams.sicbang.model.entity.User_;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Created by voncount on 4/13/16.
 */
public class FormReport extends BaseFormSearch<ReportInformation> {

    @ApiModelProperty(hidden = true)
    private String reportId;
    @ApiModelProperty(hidden = true)
    private String userId;
    @ApiModelProperty(hidden = true)
    private String estateId;

    private String name;
    private String cellphone;
    private String content;

    public String getReportId() {
        return reportId;
    }

    public void setReportId(String reportId) {
        this.reportId = reportId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getEstateId() {
        return estateId;
    }

    public void setEstateId(String estateId) {
        this.estateId = estateId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCellphone() {
        return cellphone;
    }

    public void setCellphone(String cellphone) {
        this.cellphone = cellphone;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public Specification<ReportInformation> getSpecification() {
        return (Root<ReportInformation> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) -> {
            root.join(ReportInformation_.user, JoinType.INNER);

            List<Predicate> predicates = new ArrayList<>();
            Optional<Long> _reportId = ConvertUtils.toLongNumber(reportId);
            Optional<Long> _userId = ConvertUtils.toLongNumber(userId);

            if (_reportId.isPresent()) {
                predicates.add(
                        criteriaBuilder.equal(root.get(ReportInformation_.id), _reportId.get())
                );
            }
            if (_userId.isPresent()) {
                predicates.add(
                        criteriaBuilder.equal(root.get(ReportInformation_.user).get(User_.id), _userId.get())
                );
            }
            if (!Strings.isNullOrEmpty(query)) {
                predicates.add(
                        criteriaBuilder.like(root.get(ReportInformation_.name), "%" + query + "%")
                );
            }
            if (!Strings.isNullOrEmpty(name)) {
                predicates.add(
                        criteriaBuilder.like(root.get(ReportInformation_.name), "%" + name + "%")
                );
            }
            if (!Strings.isNullOrEmpty(cellphone)) {
                predicates.add(
                        criteriaBuilder.like(root.get(ReportInformation_.cellphone), "%" + cellphone + "%")
                );
            }

            predicates.add(
                    criteriaBuilder.equal(root.get(ReportInformation_.isDelete), isDelete)
            );

            if (predicates.isEmpty()) {
                return criteriaBuilder.isNotNull(root.get(ReportInformation_.id));
            } else {
                return criteriaBuilder.and(predicates.toArray(new Predicate[] {}));
            }
        };
    }
}
