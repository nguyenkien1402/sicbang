package org.trams.sicbang.model.form;

import com.google.common.base.Strings;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.multipart.MultipartFile;
import org.trams.sicbang.common.utils.ConvertUtils;
import org.trams.sicbang.model.dto.BaseFormSearch;
import org.trams.sicbang.model.entity.Slide;
import org.trams.sicbang.model.entity.Slide_;
import org.trams.sicbang.model.entity.User_;
import org.trams.sicbang.model.enumerate.CommonStatus;

import javax.persistence.criteria.*;
import java.util.*;

/**
 * Created by KienNT on 8/18/2016.
 */

public class FormSlide extends BaseFormSearch<Slide>{

    private String id;
    private String webUrl;
    private String appUrl;
    private String name;
    private String link;
    private String userId;
    @ApiModelProperty(value = "APP | WEB")
    private String type;

    private MultipartFile attachments;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getWebUrl() {
        return webUrl;
    }

    public void setWebUrl(String webUrl) {
        this.webUrl = webUrl;
    }

    public String getAppUrl() {
        return appUrl;
    }

    public void setAppUrl(String appUrl) {
        this.appUrl = appUrl;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public MultipartFile getAttachments() {
        return attachments;
    }

    public void setAttachments(MultipartFile attachments) {
        this.attachments = attachments;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    @Override
    public Specification<Slide> getSpecification() {
        return (Root<Slide> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) -> {
            root.join(Slide_.user, JoinType.INNER);

            List<Predicate> predicates = new ArrayList<>();
//            Optional<Long> _userId = ConvertUtils.toLongNumber(userId);
//            Optional<Date> _date = ConvertUtils.toDate(date);
//            Optional<CommonStatus> _status = ConvertUtils.toEnum(status, CommonStatus.class);
//
//            if (_userId.isPresent()) {
//                predicates.add(
//                        criteriaBuilder.equal(root.get(User_.id), _userId.get())
//                );
//            }
//            if (!Strings.isNullOrEmpty(query)) {
//                predicates.add(
//                        criteriaBuilder.like(root.get(User_.email), "%" + query + "%")
//                );
//            }
//            if (!Strings.isNullOrEmpty(type)) {
//                String[] types = type.split(",");
//                List<Predicate> subPredicates = new ArrayList<>();
//                for (String t : types) {
//                    Predicate p = criteriaBuilder.equal(root.get(User_.type), UserType.valueOf(t));
//                    subPredicates.add(p);
//                }
//                predicates.add(criteriaBuilder.or(subPredicates.toArray(new Predicate[] {})));
//            }
//            if (!Strings.isNullOrEmpty(role)) {
//                predicates.add(
//                        criteriaBuilder.equal(root.get(User_.role).get(UserRole_.name), role)
//                );
//            }
//            if (!Strings.isNullOrEmpty(phoneNumber)) {
//                predicates.add(
//                        criteriaBuilder.like(root.get(User_.phoneNumber), "%" + phoneNumber + "%")
//                );
//            }
//            if (_date.isPresent()) {
//                predicates.add(
//                        criteriaBuilder.greaterThanOrEqualTo(root.get(User_.createdDate), _date.get())
//                );
//            }
//            if (_status.isPresent()) {
//                predicates.add(
//                        criteriaBuilder.equal(root.get(User_.status), _status.get())
//                );
//            }
//            if (!Strings.isNullOrEmpty(companyName)) {
//                predicates.add(
//                        criteriaBuilder.like(root.get(User_.companyName), "%" + companyName + "%")
//                );
//            }
//            if (!Strings.isNullOrEmpty(addressDetail)) {
//                predicates.add(
//                        criteriaBuilder.like(root.get(User_.addressDetail), "%" + addressDetail + "%")
//                );
//            }
//            if (!Strings.isNullOrEmpty(corporationRegistration)) {
//                predicates.add(
//                        criteriaBuilder.like(root.get(User_.corporationRegistration), "%" + corporationRegistration + "%")
//                );
//            }
            if(!Strings.isNullOrEmpty(type)){
                predicates.add(
                        criteriaBuilder.equal(root.get(Slide_.type), type)
                );
            }

            predicates.add(
                    criteriaBuilder.equal(root.get(User_.isDelete), isDelete)
            );
            if (predicates.isEmpty()) {
                return criteriaBuilder.isNotNull(root.get(Slide_.id));
            } else {
                return criteriaBuilder.and(predicates.toArray(new Predicate[] {}));
            }
        };
    }
}
