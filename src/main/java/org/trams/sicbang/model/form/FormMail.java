package org.trams.sicbang.model.form;

import com.google.common.base.Strings;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.multipart.MultipartFile;
import org.trams.sicbang.common.utils.CommonUserType;
import org.trams.sicbang.model.dto.BaseFormSearch;
import org.trams.sicbang.model.entity.Mail;
import org.trams.sicbang.model.entity.Mail_;
import org.trams.sicbang.model.entity.Notice_;
import org.trams.sicbang.model.entity.User;

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
@ToString
public class FormMail extends BaseFormSearch<Mail> {

    @ApiModelProperty(hidden = true)
    private String id;
    private String mailSubject;
    private String mailTo;
    private String mailContent;
    private Set<MultipartFile> attachments;

    private List<String> emailsTo;

    private int [] arrDelete;

    private String type;

    @Override
    public Specification<Mail> getSpecification() {
        return (Root<Mail> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) -> {

            List<Predicate> predicates = new ArrayList<>();
            if (!Strings.isNullOrEmpty(id)) {
                String[] ids = id.split(",");
                List<Predicate> subPredicates = new ArrayList<>();
                for (String id : ids) {
                    Predicate p = criteriaBuilder.equal(root.get(Mail_.id), Long.parseLong(id));
                    subPredicates.add(p);
                }
                predicates.add(criteriaBuilder.or(subPredicates.toArray(new Predicate[] {})));
            }

            if (!Strings.isNullOrEmpty(mailSubject)) {
                predicates.add(
                        criteriaBuilder.like(root.get(Mail_.mailSubject), "%" + mailSubject + "%")
                );
            }

            if (!Strings.isNullOrEmpty(mailContent)) {
                predicates.add(
                        criteriaBuilder.like(root.get(Mail_.mailContent), "%" + mailContent + "%")
                );
            }

            predicates.add(
                    criteriaBuilder.equal(root.get(Mail_.isDelete), isDelete)
            );
            criteriaQuery.orderBy(criteriaBuilder.desc(root.get("id")));
            if (predicates.isEmpty()) {
                return criteriaBuilder.isNotNull(root.get(Mail_.id));
            } else {
                return criteriaBuilder.and(predicates.toArray(new Predicate[] {}));
            }
        };
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMailSubject() {
        return mailSubject;
    }

    public void setMailSubject(String mailSubject) {
        this.mailSubject = mailSubject;
    }

    public String getMailTo() {
        return mailTo;
    }

    public void setMailTo(String mailTo) {
        this.mailTo = mailTo;
    }

    public String getMailContent() {
        return mailContent;
    }

    public void setMailContent(String mailContent) {
        this.mailContent = mailContent;
    }

    public Set<MultipartFile> getAttachments() {
        return attachments;
    }

    public void setAttachments(Set<MultipartFile> attachments) {
        this.attachments = attachments;
    }

    public int[] getArrDelete() {
        return arrDelete;
    }

    public void setArrDelete(int[] arrDelete) {
        this.arrDelete = arrDelete;
    }


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<String> getEmailsTo() {
        return emailsTo;
    }

    public void setEmailsTo(List<String> emailsTo) {
        this.emailsTo = emailsTo;
    }


    public String converType(){
        switch (type){
            case "SignUpToday":
                return "1";
            case "SignUpLastMonth":
                return "2";
            case "RegularMember":
                return "3";
            case "Broker":
                return "4";
            case "TrustMember":
                return "5";
            case "AllMember":
                return "6";
        }
        return "0";
    }
}
