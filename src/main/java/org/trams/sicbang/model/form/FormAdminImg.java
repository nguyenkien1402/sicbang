package org.trams.sicbang.model.form;

import com.google.common.base.Strings;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.multipart.MultipartFile;
import org.trams.sicbang.model.dto.BaseFormSearch;
import org.trams.sicbang.model.entity.AdminImg;
import org.trams.sicbang.model.entity.Mail;
import org.trams.sicbang.model.entity.Mail_;
import org.trams.sicbang.model.entity.Notice_;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by voncount on 4/27/16.
 */
public class FormAdminImg extends BaseFormSearch<AdminImg> {

    @ApiModelProperty(hidden = true)
    private String mailId;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    private String url;
    private Set<MultipartFile> attachments;

    public Set<MultipartFile> getAttachments() {
        return attachments;
    }

    public void setAttachments(Set<MultipartFile> attachments) {
        this.attachments = attachments;
    }

    @Override
    public Specification<AdminImg> getSpecification() {
        return (Root<AdminImg> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) -> {

            List<Predicate> predicates = new ArrayList<>();
            if (!Strings.isNullOrEmpty(mailId)) {
                String[] ids = mailId.split(",");
                List<Predicate> subPredicates = new ArrayList<>();
                for (String id : ids) {
                    Predicate p = criteriaBuilder.equal(root.get(Mail_.id), Long.parseLong(id));
                    subPredicates.add(p);
                }
                predicates.add(criteriaBuilder.or(subPredicates.toArray(new Predicate[]{})));
            }


            predicates.add(
                    criteriaBuilder.equal(root.get(Notice_.isDelete), isDelete)
            );
            if (predicates.isEmpty()) {
                return criteriaBuilder.isNotNull(root.get(Notice_.id));
            } else {
                return criteriaBuilder.and(predicates.toArray(new Predicate[]{}));
            }
        };
    }

}
