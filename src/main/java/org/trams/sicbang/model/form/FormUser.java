package org.trams.sicbang.model.form;

import com.google.common.base.Strings;
import io.swagger.annotations.ApiModelProperty;
import lombok.ToString;
import org.springframework.data.jpa.domain.Specification;
import org.trams.sicbang.common.utils.ConvertUtils;
import org.trams.sicbang.model.dto.BaseFormSearch;
import org.trams.sicbang.model.entity.User;
import org.trams.sicbang.model.entity.UserPermission_;
import org.trams.sicbang.model.entity.UserRole_;
import org.trams.sicbang.model.entity.User_;
import org.trams.sicbang.model.enumerate.CommonStatus;
import org.trams.sicbang.model.enumerate.UserType;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * Created by voncount on 4/13/16.
 */
@ToString
public class FormUser extends BaseFormSearch<User> {

    @ApiModelProperty(hidden = true)
    private String userId;
    // MEMBER INFO
    private String email;
    private String password;
    private String passwordConfirm;
    @ApiModelProperty(hidden = true)
    private String status;
    @ApiModelProperty(value = "MEMBER | BROKER")
    private String type;
    @ApiModelProperty(hidden = true)
    private String role;
    // END MEMBER INFO

    // BROKER INFO
    private String corporationRegistration;
    private String username;
    private String companyName;
    private String townAddress;
    private String addressDetail;
    private String phoneNumber;
    private String cellphoneNumber;
    // END BROKER INFO

    private String base64image;

    private String emailAbsolute;

    private String permission;

    private String dueDate;

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPasswordConfirm() {
        return passwordConfirm;
    }

    public void setPasswordConfirm(String passwordConfirm) {
        this.passwordConfirm = passwordConfirm;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getCorporationRegistration() {
        return corporationRegistration;
    }

    public void setCorporationRegistration(String corporationRegistration) {
        this.corporationRegistration = corporationRegistration;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getTownAddress() {
        return townAddress;
    }

    public void setTownAddress(String townAddress) {
        this.townAddress = townAddress;
    }

    public String getAddressDetail() {
        return addressDetail;
    }

    public void setAddressDetail(String addressDetail) {
        this.addressDetail = addressDetail;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getCellphoneNumber() {
        return cellphoneNumber;
    }

    public void setCellphoneNumber(String cellphoneNumber) {
        this.cellphoneNumber = cellphoneNumber;
    }

    public String getBase64image() {
        return base64image;
    }

    public void setBase64image(String base64image) {
        this.base64image = base64image;
    }

    public String getEmailAbsolute() {
        return emailAbsolute;
    }

    public void setEmailAbsolute(String emailAbsolute) {
        this.emailAbsolute = emailAbsolute;
    }

    @Override
    public Specification<User> getSpecification() {
        return (Root<User> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) -> {
            root.join(User_.role, JoinType.INNER);

            List<Predicate> predicates = new ArrayList<>();
            Optional<Long> _userId = ConvertUtils.toLongNumber(userId);
            Optional<Date> _date = ConvertUtils.toDate(date);
            Optional<CommonStatus> _status = ConvertUtils.toEnum(status, CommonStatus.class);

            if (_userId.isPresent()) {
                predicates.add(
                        criteriaBuilder.equal(root.get(User_.id), _userId.get())
                );
            }
            if (!Strings.isNullOrEmpty(query)) {
                predicates.add(
                        criteriaBuilder.like(root.get(User_.email), "%" + query + "%")
                );
            }
            if (!Strings.isNullOrEmpty(type)) {
                String[] types = type.split(",");
                List<Predicate> subPredicates = new ArrayList<>();
                for (String t : types) {
                    Predicate p = criteriaBuilder.equal(root.get(User_.type), UserType.valueOf(t));
                    subPredicates.add(p);
                }
                predicates.add(criteriaBuilder.or(subPredicates.toArray(new Predicate[] {})));
            }
            if (!Strings.isNullOrEmpty(role)) {
                predicates.add(
                        criteriaBuilder.equal(root.get(User_.role).get(UserRole_.name), role)
                );
            }

            if(!Strings.isNullOrEmpty(permission)){
                predicates.add(criteriaBuilder.equal(root.get(User_.permission).get(UserPermission_.name),permission));
            }

            if (!Strings.isNullOrEmpty(phoneNumber)) {
                predicates.add(
                        criteriaBuilder.like(root.get(User_.phoneNumber), "%" + phoneNumber + "%")
                );
            }
            if (_date.isPresent()) {
                predicates.add(
                        criteriaBuilder.greaterThanOrEqualTo(root.get(User_.createdDate), _date.get())
                );
            }
            if (_status.isPresent()) {
                predicates.add(
                        criteriaBuilder.equal(root.get(User_.status), _status.get())
                );
            }
            if (!Strings.isNullOrEmpty(companyName)) {
                predicates.add(
                        criteriaBuilder.like(root.get(User_.companyName), "%" + companyName + "%")
                );
            }
            if (!Strings.isNullOrEmpty(addressDetail)) {
                predicates.add(
                        criteriaBuilder.like(root.get(User_.addressDetail), "%" + addressDetail + "%")
                );
            }
            if (!Strings.isNullOrEmpty(corporationRegistration)) {
                predicates.add(
                        criteriaBuilder.like(root.get(User_.corporationRegistration), "%" + corporationRegistration + "%")
                );
            }
            if(!Strings.isNullOrEmpty(email)){
                predicates.add(
                        criteriaBuilder.like(root.get(User_.email), "%"+email+"%")
                );
            }

            if(!Strings.isNullOrEmpty(emailAbsolute)){
                predicates.add(
                        criteriaBuilder.equal(root.get(User_.email), emailAbsolute)
                );
            }

            predicates.add(
                    criteriaBuilder.equal(root.get(User_.isDelete), isDelete)
            );
            if (predicates.isEmpty()) {
                return criteriaBuilder.isNotNull(root.get(User_.id));
            } else {
                return criteriaBuilder.and(predicates.toArray(new Predicate[] {}));
            }
        };
    }
}
