package org.trams.sicbang.model.form;

import io.swagger.annotations.ApiModelProperty;
import org.springframework.data.jpa.domain.Specification;
import org.trams.sicbang.common.utils.ConvertUtils;
import org.trams.sicbang.model.dto.BaseFormSearch;
import org.trams.sicbang.model.entity.Ask;
import org.trams.sicbang.model.entity.Ask_;
import org.trams.sicbang.model.entity.ReportAnswer;
import org.trams.sicbang.model.entity.ReportAnswer_;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Created by voncount on 5/4/16.
 */
public class FormAsk extends BaseFormSearch<Ask> {

    @ApiModelProperty(hidden = true)
    private String askId;

    private String title;
    private String content;
    private String name;
    private String contact;
    private String userId;

    public String getAskId() {
        return askId;
    }

    public void setAskId(String askId) {
        this.askId = askId;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Override
    public Specification<Ask> getSpecification() {
        return (Root<Ask> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) -> {
//            root.join(ReportAnswer_.reportInformation, JoinType.INNER);

            List<Predicate> predicates = new ArrayList<>();
            Optional<Long> _askId = ConvertUtils.toLongNumber(askId);

            if (_askId.isPresent()) {
                predicates.add(
                        criteriaBuilder.equal(root.get(Ask_.id), _askId.get())
                );
            }

            predicates.add(
                    criteriaBuilder.equal(root.get(Ask_.isDelete), isDelete)
            );
            criteriaQuery.orderBy(criteriaBuilder.desc(root.get("id")));
            if (predicates.isEmpty()) {
                return criteriaBuilder.isNotNull(root.get(Ask_.id));
            } else {
                return criteriaBuilder.and(predicates.toArray(new Predicate[] {}));
            }
        };
    }
}
