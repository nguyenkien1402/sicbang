package org.trams.sicbang.model.form;

import io.swagger.annotations.ApiModelProperty;
import org.springframework.data.jpa.domain.Specification;
import org.trams.sicbang.common.utils.ConvertUtils;
import org.trams.sicbang.model.dto.BaseFormSearch;
import org.trams.sicbang.model.entity.Notice;
import org.trams.sicbang.model.entity.Notice_;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Created by voncount on 25/04/2016.
 */
public class FormNotice extends BaseFormSearch<Notice> {

    @ApiModelProperty(hidden = true)
    private String noticeId;

    private String title;
    private String content;

    public String getNoticeId() {
        return noticeId;
    }

    public void setNoticeId(String noticeId) {
        this.noticeId = noticeId;
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
    public Specification<Notice> getSpecification() {
        return (Root<Notice> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) -> {

            List<Predicate> predicates = new ArrayList<>();
            Optional<Long> _noticeId = ConvertUtils.toLongNumber(noticeId);

            if (_noticeId.isPresent()) {
                predicates.add(
                        criteriaBuilder.equal(root.get(Notice_.id), _noticeId.get())
                );
            }

            predicates.add(
                    criteriaBuilder.equal(root.get(Notice_.isDelete), isDelete)
            );
            if (predicates.isEmpty()) {
                return criteriaBuilder.isNotNull(root.get(Notice_.id));
            } else {
                return criteriaBuilder.and(predicates.toArray(new Predicate[] {}));
            }
        };
    }
}
