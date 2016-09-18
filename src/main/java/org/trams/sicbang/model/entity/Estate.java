package org.trams.sicbang.model.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.trams.sicbang.model.enumerate.EstateType;

import javax.persistence.*;
import java.util.Collection;
import java.util.Date;

/**
 * Created by voncount on 4/11/16.
 */
@Entity
@Table(indexes = {@Index(name = "ESTATE_UNIQUE_CODE", columnList = "estateCode", unique = true)})
public class Estate extends BaseTimestampEntity {

    private String estateCode;
    private String name;
    private String businessZone;
    private String subwayStation;
    private String onelineComment;
    private String detail;
    private String floor;

    private String estateType;
    private String latitude;
    private String longitude;
    @Getter
    @Setter
    private String all_addr;
    private Boolean isAdvertised;

//    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
//    @JsonIdentityReference(alwaysAsId = true)
//    @OneToOne
//    private City city;
//
//    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
//    @JsonIdentityReference(alwaysAsId = true)
//    @OneToOne
//    private District district;
//
//    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
//    @JsonIdentityReference(alwaysAsId = true)
//    @OneToOne
//    private Town town;

    @ManyToOne
    private User user;

    // STARTUP FIELDS
    private Double depositeCost;
    private Double rentCost;
    private Double serviceCost;
    private Double premiumCost;
    private Double otherCost;
    private Double monthlyIncome;
    private Double monthlyTax;
    private Double gainRatio;

    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
    @JsonIdentityReference(alwaysAsId = true)
    @OneToOne
    private Category category;

    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
    @JsonIdentityReference(alwaysAsId = true)
    @OneToOne
    private BusinessType businessType;
    private Double area;
    // END STARTUP FIELDS

    // VACANT FIELDS
    private String currentBusiness;
    @Temporal(TemporalType.TIMESTAMP)
    private Date constructDate;
    @Temporal(TemporalType.TIMESTAMP)
    private Date availableDate;
    private Double loan;
    private Double contractArea;
    private Double exclusiveArea;
    private Double exclusiveRate;
    private Boolean parking = Boolean.FALSE;
    // END VACANT FIELDS

    @Transient
    private Collection<Attachment> attachments;
    @Transient
    private Boolean isWishlist = Boolean.FALSE;

    public String getEstateCode() {
        return estateCode;
    }

    public void setEstateCode(String estateCode) {
        this.estateCode = estateCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBusinessZone() {
        return businessZone;
    }

    public void setBusinessZone(String businessZone) {
        this.businessZone = businessZone;
    }

    public String getSubwayStation() {
        return subwayStation;
    }

    public void setSubwayStation(String subwayStation) {
        this.subwayStation = subwayStation;
    }
    public String getEstateType() {
        return estateType;
    }

    public void setEstateType(String estateType) {
        this.estateType = estateType;
    }
    public String getOnelineComment() {
        return onelineComment;
    }

    public void setOnelineComment(String onelineComment) {
        this.onelineComment = onelineComment;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getFloor() {
        return floor;
    }

    public void setFloor(String floor) {
        this.floor = floor;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public Boolean getAdvertised() {
        return isAdvertised;
    }

    public void setAdvertised(Boolean advertised) {
        isAdvertised = advertised;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Double getDepositeCost() {
        return depositeCost;
    }

    public void setDepositeCost(Double depositeCost) {
        this.depositeCost = depositeCost;
    }

    public Double getRentCost() {
        return rentCost;
    }

    public void setRentCost(Double rentCost) {
        this.rentCost = rentCost;
    }

    public Double getServiceCost() {
        return serviceCost;
    }

    public void setServiceCost(Double serviceCost) {
        this.serviceCost = serviceCost;
    }

    public Double getPremiumCost() {
        return premiumCost;
    }

    public void setPremiumCost(Double premiumCost) {
        this.premiumCost = premiumCost;
    }

    public Double getOtherCost() {
        return otherCost;
    }

    public void setOtherCost(Double otherCost) {
        this.otherCost = otherCost;
    }

    public Double getMonthlyIncome() {
        return monthlyIncome;
    }

    public void setMonthlyIncome(Double monthlyIncome) {
        this.monthlyIncome = monthlyIncome;
    }

    public Double getMonthlyTax() {
        return monthlyTax;
    }

    public void setMonthlyTax(Double monthlyTax) {
        this.monthlyTax = monthlyTax;
    }

    public Double getGainRatio() {
        return gainRatio;
    }

    public void setGainRatio(Double gainRatio) {
        this.gainRatio = gainRatio;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public BusinessType getBusinessType() {
        return businessType;
    }

    public void setBusinessType(BusinessType businessType) {
        this.businessType = businessType;
    }

    public Double getArea() {
        return area;
    }

    public void setArea(Double area) {
        this.area = area;
    }

    public String getCurrentBusiness() {
        return currentBusiness;
    }

    public void setCurrentBusiness(String currentBusiness) {
        this.currentBusiness = currentBusiness;
    }

    public Date getConstructDate() {
        return constructDate;
    }

    public void setConstructDate(Date constructDate) {
        this.constructDate = constructDate;
    }

    public Date getAvailableDate() {
        return availableDate;
    }

    public void setAvailableDate(Date availableDate) {
        this.availableDate = availableDate;
    }

    public Double getLoan() {
        return loan;
    }

    public void setLoan(Double loan) {
        this.loan = loan;
    }

    public Double getContractArea() {
        return contractArea;
    }

    public void setContractArea(Double contractArea) {
        this.contractArea = contractArea;
    }

    public Double getExclusiveArea() {
        return exclusiveArea;
    }

    public void setExclusiveArea(Double exclusiveArea) {
        this.exclusiveArea = exclusiveArea;
    }

    public Double getExclusiveRate() {
        return exclusiveRate;
    }

    public void setExclusiveRate(Double exclusiveRate) {
        this.exclusiveRate = exclusiveRate;
    }

    public Boolean getParking() {
        return parking;
    }

    public void setParking(Boolean parking) {
        this.parking = parking;
    }

    public Collection<Attachment> getAttachments() {
        return attachments;
    }

    public void setAttachments(Collection<Attachment> attachments) {
        this.attachments = attachments;
    }

    public Boolean getWishlist() {
        return isWishlist;
    }

    public void setWishlist(Boolean wishlist) {
        isWishlist = wishlist;
    }
}
