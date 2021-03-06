package org.trams.sicbang.model.form;

import com.google.common.base.Strings;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.apache.log4j.Logger;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.multipart.MultipartFile;
import org.trams.sicbang.common.utils.ConvertUtils;
import org.trams.sicbang.model.dto.BaseFormSearch;
import org.trams.sicbang.model.entity.*;
import org.trams.sicbang.model.enumerate.EstateType;
import org.trams.sicbang.model.enumerate.UserType;

import javax.persistence.criteria.*;
import javax.swing.text.html.Option;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Created by voncount on 4/14/2016.
 */

public class FormEstate extends BaseFormSearch<Estate> {
    @Getter(AccessLevel.NONE)
    private final Logger logger = Logger.getLogger(this.getClass());
    @ApiModelProperty(hidden = true)
    private String estateId;
    @ApiModelProperty(hidden = true)
    private String estateCode;
    @ApiModelProperty(hidden = true)
    private String userId;
    @ApiModelProperty(value = "TRUSTED_BROKER|BROKER|MEMBER")
    private String userType;

    private String name;
    private String businessZone;
    private String subwayStation;
    private String onelineComment;
    private String detail;
    private String floor;
    private List<String> attachments;
    private List<MultipartFile> attachmentFiles;
    private MultipartFile attach; // upload one image

    @ApiModelProperty(value = "STARTUP | VACANT")
    private String estateType;
    private String latitude;
    private String longitude;
    @ApiModelProperty(notes = "ID of the City")
    private String city;
    @ApiModelProperty(notes = "ID of the District")
    private String district;
    @ApiModelProperty(notes = "ID of the Town")
    private String town;
    private String all_addr;
    private String isAdvertised;

    // STARTUP FIELDS
    private String depositeCost;
    private String rentCost;
    private String serviceCost;
    private String premiumCost;
    private String otherCost;
    private String monthlyIncome;
    private String monthlyTax;
    private String gainRatio;
    @ApiModelProperty(notes = "ID of the Category")
    private String category;
    @ApiModelProperty(notes = "ID of the BusinessType")
    private String businessType;
    private String area;
    // END STARTUP FIELDS

    // VACANT FIELDS
    private String currentBusiness;
    private String constructDate;
    private String availableDate;
    private String loan;
    private String contractArea;
    private String exclusiveArea;
    private String exclusiveRate;
    private String parking;
    // END VACANT FIELDS

    public static Specification<Estate> dongLike(final String keyword) {
        return (Root<Estate> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) -> {
            Predicate predicate = criteriaBuilder.like(root.get(Estate_.all_addr), "%" + keyword);
            return predicate;
        };
    }

    @Override
    public Specification<Estate> getSpecification() {
        return (Root<Estate> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) -> {

            root.join(Estate_.user, JoinType.INNER);

            List<Predicate> predicates = new ArrayList<>();
            Optional<Long> _estateId = ConvertUtils.toLongNumber(estateId);
            Optional<Long> _userId = ConvertUtils.toLongNumber(userId);
            Optional<Double> _depositeCost = ConvertUtils.toDoubleNumber(depositeCost);
            Optional<Double> _rentCost = ConvertUtils.toDoubleNumber(rentCost);
            Optional<Double> _premiumCost = ConvertUtils.toDoubleNumber(premiumCost);
            Optional<Boolean> _isAdvertised = ConvertUtils.toBoolean(isAdvertised);

            logger.info("estateId : "+estateId);
            logger.info("userId : "+userId);
            logger.info("depositeCost : "+depositeCost);
            logger.info("rentCost : "+rentCost);
            logger.info("premiumCost : "+premiumCost);
            logger.info("isAdvertised : "+isAdvertised);
            logger.info("isEstateType : "+ estateType);
            if (_estateId.isPresent()) {
                predicates.add(
                        criteriaBuilder.equal(root.get(Estate_.id), _estateId.get())
                );
            }
            if (_userId.isPresent()) {
                predicates.add(
                        criteriaBuilder.equal(root.get(Estate_.user).get(User_.id), _userId.get())
                );
            }
            if (!Strings.isNullOrEmpty(estateCode)) {
                predicates.add(
                        criteriaBuilder.equal(root.get(Estate_.estateCode), estateCode)
                );
            }
            if (!Strings.isNullOrEmpty(userType)) {
                String[] types = userType.split(",");
                List<Predicate> subPredicates = new ArrayList<>();
                for (String t : types) {
                    Optional<UserType> _type = ConvertUtils.toEnum(t, UserType.class);
                    if (_type.isPresent()) {
                        Predicate p = criteriaBuilder.equal(root.get(Estate_.user).get(User_.type), _type.get());
                        subPredicates.add(p);
                    }
                }
                predicates.add(criteriaBuilder.or(subPredicates.toArray(new Predicate[]{})));
            }
            if (!Strings.isNullOrEmpty(estateType)) {
                String[] types = estateType.split(",");
                List<Predicate> subPredicates = new ArrayList<>();
                for (String t : types) {
                    Optional<EstateType> _type = ConvertUtils.toEnum(t, EstateType.class);
                    if (_type.isPresent()) {
                        Predicate p = criteriaBuilder.equal(root.get(Estate_.estateType), _type.get().name());
                        subPredicates.add(p);
                    }
                }
                predicates.add(criteriaBuilder.or(subPredicates.toArray(new Predicate[]{})));
            }
            if (!Strings.isNullOrEmpty(category)) {
                String[] types = category.split(",");
                List<Predicate> subPredicates = new ArrayList<>();
                for (String t : types) {
                    Optional<Long> _id = ConvertUtils.toLongNumber(t);
                    if (_id.isPresent()) {
                        Predicate p = criteriaBuilder.equal(root.get(Estate_.category).get(Category_.id), _id.get());
                        subPredicates.add(p);
                    }
                }
                predicates.add(criteriaBuilder.or(subPredicates.toArray(new Predicate[]{})));
            }
            if (!Strings.isNullOrEmpty(businessType)) {
                String[] types = businessType.split(",");
                List<Predicate> subPredicates = new ArrayList<>();
                for (String t : types) {
                    Optional<Long> _id = ConvertUtils.toLongNumber(t);
                    if (_id.isPresent()) {
                        Predicate p = criteriaBuilder.equal(root.get(Estate_.businessType).get(BusinessType_.id), _id.get());
                        subPredicates.add(p);
                    }
                }
                predicates.add(criteriaBuilder.or(subPredicates.toArray(new Predicate[]{})));
            }
//            if (_city.isPresent()) {
//                root.join(Estate_.city, JoinType.LEFT);
//                predicates.add(
//                        criteriaBuilder.equal(root.get(Estate_.city).get(City_.id), _city.get())
//                );
//            }
//            if (_district.isPresent()) {
//                root.join(Estate_.district, JoinType.LEFT);
//                predicates.add(
//                        criteriaBuilder.equal(root.get(Estate_.district).get(District_.id), _district.get())
//                );
//            }
//            if (_town.isPresent()) {
//                root.join(Estate_.town, JoinType.LEFT);
//                predicates.add(
//                        criteriaBuilder.equal(root.get(Estate_.town).get(Town_.id), _town.get())
//                );
//            }
            if (_depositeCost.isPresent()) {
                predicates.add(
                        criteriaBuilder.greaterThanOrEqualTo(root.get(Estate_.depositeCost), _depositeCost.get())
                );
            }
            if (_rentCost.isPresent()) {
                predicates.add(
                        criteriaBuilder.greaterThanOrEqualTo(root.get(Estate_.rentCost), _rentCost.get())
                );
            }
            if (_premiumCost.isPresent()) {
                predicates.add(
                        criteriaBuilder.greaterThanOrEqualTo(root.get(Estate_.premiumCost), _premiumCost.get())
                );
            }
            if (_isAdvertised.isPresent()) {
                predicates.add(
                        criteriaBuilder.equal(root.get(Estate_.isAdvertised), _isAdvertised.get())
                );
            }


            predicates.add(
                    criteriaBuilder.equal(root.get(Estate_.isDelete), isDelete)
            );


            if (predicates.isEmpty()) {
                return criteriaBuilder.isNotNull(root.get(Estate_.id));
            } else {
                return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
            }
        };
    }

    public Logger getLogger() {
        return logger;
    }

    public String getEstateId() {
        return estateId;
    }

    public void setEstateId(String estateId) {
        this.estateId = estateId;
    }

    public String getEstateCode() {
        return estateCode;
    }

    public void setEstateCode(String estateCode) {
        this.estateCode = estateCode;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
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

    public List<String> getAttachments() {
        return attachments;
    }

    public void setAttachments(List<String> attachments) {
        this.attachments = attachments;
    }

    public String getEstateType() {
        return estateType;
    }

    public void setEstateType(String estateType) {
        this.estateType = estateType;
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

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getTown() {
        return town;
    }

    public void setTown(String town) {
        this.town = town;
    }

    public String getAll_addr() {
        return all_addr;
    }

    public void setAll_addr(String all_addr) {
        this.all_addr = all_addr;
    }

    public String getIsAdvertised() {
        return isAdvertised;
    }

    public void setIsAdvertised(String isAdvertised) {
        this.isAdvertised = isAdvertised;
    }

    public String getDepositeCost() {
        return depositeCost;
    }

    public void setDepositeCost(String depositeCost) {
        this.depositeCost = depositeCost;
    }

    public String getRentCost() {
        return rentCost;
    }

    public void setRentCost(String rentCost) {
        this.rentCost = rentCost;
    }

    public String getServiceCost() {
        return serviceCost;
    }

    public void setServiceCost(String serviceCost) {
        this.serviceCost = serviceCost;
    }

    public String getPremiumCost() {
        return premiumCost;
    }

    public void setPremiumCost(String premiumCost) {
        this.premiumCost = premiumCost;
    }

    public String getOtherCost() {
        return otherCost;
    }

    public void setOtherCost(String otherCost) {
        this.otherCost = otherCost;
    }

    public String getMonthlyIncome() {
        return monthlyIncome;
    }

    public void setMonthlyIncome(String monthlyIncome) {
        this.monthlyIncome = monthlyIncome;
    }

    public String getMonthlyTax() {
        return monthlyTax;
    }

    public void setMonthlyTax(String monthlyTax) {
        this.monthlyTax = monthlyTax;
    }

    public String getGainRatio() {
        return gainRatio;
    }

    public void setGainRatio(String gainRatio) {
        this.gainRatio = gainRatio;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getBusinessType() {
        return businessType;
    }

    public void setBusinessType(String businessType) {
        this.businessType = businessType;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getCurrentBusiness() {
        return currentBusiness;
    }

    public void setCurrentBusiness(String currentBusiness) {
        this.currentBusiness = currentBusiness;
    }

    public String getConstructDate() {
        return constructDate;
    }

    public void setConstructDate(String constructDate) {
        this.constructDate = constructDate;
    }

    public String getAvailableDate() {
        return availableDate;
    }

    public void setAvailableDate(String availableDate) {
        this.availableDate = availableDate;
    }

    public String getLoan() {
        return loan;
    }

    public void setLoan(String loan) {
        this.loan = loan;
    }

    public String getContractArea() {
        return contractArea;
    }

    public void setContractArea(String contractArea) {
        this.contractArea = contractArea;
    }

    public String getExclusiveArea() {
        return exclusiveArea;
    }

    public void setExclusiveArea(String exclusiveArea) {
        this.exclusiveArea = exclusiveArea;
    }

    public String getExclusiveRate() {
        return exclusiveRate;
    }

    public void setExclusiveRate(String exclusiveRate) {
        this.exclusiveRate = exclusiveRate;
    }

    public String getParking() {
        return parking;
    }

    public void setParking(String parking) {
        this.parking = parking;
    }

    public List<MultipartFile> getAttachmentFiles() {
        return attachmentFiles;
    }

    public void setAttachmentFiles(List<MultipartFile> attachmentFiles) {
        this.attachmentFiles = attachmentFiles;
    }

    public MultipartFile getAttach() {
        return attach;
    }

    public void setAttach(MultipartFile attach) {
        this.attach = attach;
    }

    public static Estate convertEstateFormToEstate(FormEstate form, Estate estate){
        if(!Strings.isNullOrEmpty(form.getEstateId())) {
            estate.setId(Long.parseLong(form.getEstateId()));
        }
        estate.setIsDelete(0);
//        estate.setModifiedDate(form.get);
        if(!Strings.isNullOrEmpty(form.getArea())) {
            estate.setArea(Double.parseDouble(form.getArea()));
        }
        if(!Strings.isNullOrEmpty(form.getBusinessZone())) {
            estate.setBusinessZone(form.getBusinessZone());
        }
//        estate.setConstructDate();
        if(!Strings.isNullOrEmpty(form.getContractArea())) {
            estate.setContractArea(Double.parseDouble(form.getContractArea()));
        }
        if(!Strings.isNullOrEmpty(form.getCurrentBusiness())) {
            estate.setCurrentBusiness(form.getCurrentBusiness());
        }
        if(!Strings.isNullOrEmpty(form.getDepositeCost())) {
            estate.setDepositeCost(Double.parseDouble(form.getDepositeCost()));
        }
        if(!Strings.isNullOrEmpty(form.getDetail())) {
            estate.setDetail(form.getDetail());
        }
        if(!Strings.isNullOrEmpty(form.getEstateCode())) {
            estate.setEstateCode(form.getEstateCode());
        }
//        estate.setEstateType(form.getEstateType());
        if(!Strings.isNullOrEmpty(form.getExclusiveArea())) {
            estate.setExclusiveArea(Double.parseDouble(form.getExclusiveArea()));
        }
        if(!Strings.isNullOrEmpty(form.getExclusiveRate())) {
            estate.setExclusiveRate(Double.parseDouble(form.getExclusiveRate()));
        }
        if(!Strings.isNullOrEmpty(form.getFloor())) {
            estate.setFloor(form.getFloor());
        }
        if(!Strings.isNullOrEmpty(form.getGainRatio())) {
            estate.setGainRatio(Double.parseDouble(form.getGainRatio()));
        }
        if(!Strings.isNullOrEmpty(form.getIsAdvertised())) {
            estate.setAdvertised(Boolean.parseBoolean(form.getIsAdvertised()));
        }
        if(!Strings.isNullOrEmpty(form.getLoan())) {
            estate.setLoan(Double.parseDouble(form.getLoan()));
        }
        if(!Strings.isNullOrEmpty(form.getMonthlyIncome())) {
            estate.setMonthlyIncome(Double.parseDouble(form.getMonthlyIncome()));
        }
        if(!Strings.isNullOrEmpty(form.getMonthlyTax())) {
            estate.setMonthlyTax(Double.parseDouble(form.getMonthlyTax()));
        }
        if(!Strings.isNullOrEmpty(form.getName())) {
            estate.setName(form.getName());
        }
        // set parking
        if(!Strings.isNullOrEmpty(form.getOnelineComment())) {
            estate.setOnelineComment(form.getOnelineComment());
        }
        if(!Strings.isNullOrEmpty(form.getOtherCost())) {
            estate.setOtherCost(Double.parseDouble(form.getOtherCost()));
        }
        if(!Strings.isNullOrEmpty(form.getPremiumCost())) {
            estate.setPremiumCost(Double.parseDouble(form.getPremiumCost()));
        }
        if(!Strings.isNullOrEmpty(form.getRentCost())) {
            estate.setRentCost(Double.parseDouble(form.getRentCost()));
        }
        if(!Strings.isNullOrEmpty(form.getServiceCost())) {
            estate.setServiceCost(Double.parseDouble(form.getServiceCost()));
        }
        if(!Strings.isNullOrEmpty(form.getSubwayStation())) {
            estate.setSubwayStation(form.getSubwayStation());
        }
//       estate.setBusinessType();
//       estate.setCategory();
        //set city
        //set district
        //set town
        // set userid
        // ser all address
        return estate;
    }
}
