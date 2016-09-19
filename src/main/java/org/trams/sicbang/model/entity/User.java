package org.trams.sicbang.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.ToString;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.trams.sicbang.model.enumerate.CommonStatus;
import org.trams.sicbang.model.enumerate.UserType;

import javax.persistence.*;

/**
 * Created by voncount on 4/8/16.
 */
@Entity
@Table(indexes = {
        @Index(name = "USER_EMAIL_INDEX", columnList = "email", unique = true)
})
@ToString
public class User extends BaseTimestampEntity {

    // MEMBER INFO
    private String email;
    @JsonIgnore
    private String password;
    // END MEMBER INFO

    // BROKER INFO
    private String corporationRegistration;
    private String username;
    private String companyName;
    private String townAddress;
    private String addressDetail;
    private String phoneNumber;
    private String cellphoneNumber;
    private int advertisedRemain = 10;
    // END BROKER INFO

    @Enumerated(EnumType.STRING)
    private CommonStatus status;
    @Enumerated(EnumType.ORDINAL)
    private UserType type;

    @OneToOne
    @JoinColumn(nullable = false)
    private UserRole role;

    @OneToOne
    private Attachment avatar;

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

    public int getAdvertisedRemain() {
        return advertisedRemain;
    }

    public void setAdvertisedRemain(int advertisedRemain) {
        this.advertisedRemain = advertisedRemain;
    }

    public CommonStatus getStatus() {
        return status;
    }

    public void setStatus(CommonStatus status) {
        this.status = status;
    }

    public UserType getType() {
        return type;
    }

    public void setType(UserType type) {
        this.type = type;
    }

    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }

    public Attachment getAvatar() {
        return avatar;
    }

    public void setAvatar(Attachment avatar) {
        this.avatar = avatar;
    }

    @PrePersist
    private void encryptPassword() {
        this.password = new BCryptPasswordEncoder().encode(this.password);
    }

}
