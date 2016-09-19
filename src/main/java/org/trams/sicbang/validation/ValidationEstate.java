package org.trams.sicbang.validation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.trams.sicbang.common.utils.ConvertUtils;
import org.trams.sicbang.model.dto.CustomUserDetail;
import org.trams.sicbang.model.entity.*;
import org.trams.sicbang.model.enumerate.EstateType;
import org.trams.sicbang.model.enumerate.MessageResponse;
import org.trams.sicbang.model.exception.FormError;
import org.trams.sicbang.model.form.FormEstate;
import org.trams.sicbang.model.form.FormWishlist;
import org.trams.sicbang.repository.*;

import java.util.Date;
import java.util.Optional;

/**
 * Created by voncount on 4/25/16.
 */
@Component
public class ValidationEstate {

    @Autowired
    private RepositoryCategory repositoryCategory;
    @Autowired
    private RepositoryUser repositoryUser;
    @Autowired
    private RepositoryEstate repositoryEstate;
    @Autowired
    private RepositoryCity repositoryCity;
    @Autowired
    private RepositoryDistrict repositoryDistrict;
    @Autowired
    private RepositoryTown repositoryTown;
    @Autowired
    private RepositoryWishlist repositoryWishlist;

    public FormError validateCreate(FormEstate form) {
        FormError error = new FormError();

        // Validate User
        Optional<Long> _userId = ConvertUtils.toLongNumber(form.getUserId());
        if (_userId.isPresent()) {
            User user = repositoryUser.findOne(_userId.get());
            if (user == null) {
                error.rejectValue("userId", MessageResponse.EXCEPTION_FIELD_INVALID.getMessage());
            }
        } else {
            error.rejectValue("userId", MessageResponse.EXCEPTION_FIELD_INVALID.getMessage());
        }

        // Validate Estate type
        Optional<EstateType> _estateType = ConvertUtils.toEnum(form.getEstateType(), EstateType.class);
        if (_estateType.isPresent()) {
            switch (_estateType.get()) {
                case STARTUP:
                    Optional<Double> _depositeCost = ConvertUtils.toDoubleNumber(form.getDepositeCost());
                    Optional<Double> _rentCost = ConvertUtils.toDoubleNumber(form.getRentCost());
                    Optional<Double> _serviceCost = ConvertUtils.toDoubleNumber(form.getServiceCost());
                    Optional<Double> _premiumCost = ConvertUtils.toDoubleNumber(form.getPremiumCost());
                    Optional<Double> _otherCost = ConvertUtils.toDoubleNumber(form.getOtherCost());
                    Optional<Double> _monthlyIncome = ConvertUtils.toDoubleNumber(form.getMonthlyIncome());
                    Optional<Double> _monthlyTax = ConvertUtils.toDoubleNumber(form.getMonthlyTax());
                    Optional<Double> _gainRatio = ConvertUtils.toDoubleNumber(form.getGainRatio());
                    Optional<Double> _area = ConvertUtils.toDoubleNumber(form.getArea());
                    Optional<Long> _categoryId = ConvertUtils.toLongNumber(form.getCategory());
                    Optional<Long> _businessTypeId = ConvertUtils.toLongNumber(form.getBusinessType());

                    if (!_depositeCost.isPresent()) {
                        error.rejectValue("depositeCost", MessageResponse.EXCEPTION_FIELD_INVALID.getMessage());
                    }
                    if (!_rentCost.isPresent()) {
                        error.rejectValue("rentCost", MessageResponse.EXCEPTION_FIELD_INVALID.getMessage());
                    }
                    if (!_serviceCost.isPresent()) {
                        error.rejectValue("serviceCost", MessageResponse.EXCEPTION_FIELD_INVALID.getMessage());
                    }
                    if (!_premiumCost.isPresent()) {
                        error.rejectValue("premiumCost", MessageResponse.EXCEPTION_FIELD_INVALID.getMessage());
                    }
                    if (!_otherCost.isPresent()) {
                        error.rejectValue("otherCost", MessageResponse.EXCEPTION_FIELD_INVALID.getMessage());
                    }
                    if (!_monthlyIncome.isPresent()) {
                        error.rejectValue("monthlyIncome", MessageResponse.EXCEPTION_FIELD_INVALID.getMessage());
                    }
                    if (!_monthlyTax.isPresent()) {
                        error.rejectValue("monthlyTax", MessageResponse.EXCEPTION_FIELD_INVALID.getMessage());
                    }
                    if (!_gainRatio.isPresent()) {
                        error.rejectValue("gainRatio", MessageResponse.EXCEPTION_FIELD_INVALID.getMessage());
                    }
                    if (!_area.isPresent()) {
                        error.rejectValue("area", MessageResponse.EXCEPTION_FIELD_INVALID.getMessage());
                    }
                    if (!_categoryId.isPresent()) {
                        error.rejectValue("category", MessageResponse.EXCEPTION_FIELD_INVALID.getMessage());
                    }
                    if (!_businessTypeId.isPresent()) {
                        error.rejectValue("businessType", MessageResponse.EXCEPTION_FIELD_INVALID.getMessage());
                    }

                    // Validate Category
                    if (_categoryId.isPresent() && _businessTypeId.isPresent()) {
                        Category cat = repositoryCategory.findOne(_categoryId.get());
                        BusinessType bType = null;
                        if (cat == null) {
                            error.rejectValue("category", MessageResponse.EXCEPTION_FIELD_INVALID.getMessage());
                        } else {

                            for (BusinessType bt : cat.getBusinessTypes()) {
                                if (_businessTypeId.get().equals(bt.getId())) {
                                    bType = bt;
                                }
                            }
                            if (bType == null) {
                                error.rejectValue("businessType", MessageResponse.EXCEPTION_FIELD_INVALID.getMessage());
                            }
                        }
                    }
                    break;
                case VACANT:
                    Optional<Date> _constructDate = ConvertUtils.toDate(form.getConstructDate());
                    Optional<Date> _availableDate = ConvertUtils.toDate(form.getAvailableDate());
                    Optional<Double> _loan = ConvertUtils.toDoubleNumber(form.getLoan());
                    Optional<Double> _contractArea = ConvertUtils.toDoubleNumber(form.getContractArea());
                    Optional<Double> _exclusiveArea = ConvertUtils.toDoubleNumber(form.getExclusiveArea());
                    Optional<Double> _exclusiveRate = ConvertUtils.toDoubleNumber(form.getExclusiveRate());
                    Optional<Boolean> _parking = ConvertUtils.toBoolean(form.getParking());

                    if (!_constructDate.isPresent()) {
                        error.rejectValue("constructDate", MessageResponse.EXCEPTION_FIELD_INVALID.getMessage());
                    }
                    if (!_availableDate.isPresent()) {
                        error.rejectValue("availableDate", MessageResponse.EXCEPTION_FIELD_INVALID.getMessage());
                    }
                    if (!_loan.isPresent()) {
                        error.rejectValue("loan", MessageResponse.EXCEPTION_FIELD_INVALID.getMessage());
                    }
                    if (!_contractArea.isPresent()) {
                        error.rejectValue("contractArea", MessageResponse.EXCEPTION_FIELD_INVALID.getMessage());
                    }
                    if (!_exclusiveArea.isPresent()) {
                        error.rejectValue("exclusiveArea", MessageResponse.EXCEPTION_FIELD_INVALID.getMessage());
                    }
                    if (!_exclusiveRate.isPresent()) {
                        error.rejectValue("exclusiveRate", MessageResponse.EXCEPTION_FIELD_INVALID.getMessage());
                    }
                    if (!_parking.isPresent()) {
                        error.rejectValue("parking", MessageResponse.EXCEPTION_FIELD_INVALID.getMessage());
                    }
                    break;
                default:
                    error.rejectValue("estateType", MessageResponse.EXCEPTION_FIELD_INVALID.getMessage());
                    break;
            }
        } else {
            error.rejectValue("estateType", MessageResponse.EXCEPTION_FIELD_INVALID.getMessage());
        }

        return error.hasErrors() ? error : null;
    }

    public FormError existWishListAdded(Wishlist wishlist, CustomUserDetail user) {
        FormError error = new FormError();

        if (user.getUserId().equals(String.valueOf(wishlist.getUser().getId()))) {
            error.rejectValue("wishList", "매물이 이미 찜 목록에 존재합니다.");
        }
        return error.hasErrors() ? error : null;

    }

    public FormError validateWishlistAdd(FormWishlist form) {
        FormError error = new FormError();

        String estateId = form.getEstateId();
        String userId = form.getUserId();

        User user = repositoryUser.findOne(Long.parseLong(userId));
        Estate estate = repositoryEstate.findOne(Long.parseLong(estateId));

        if (user == null) {
            error.rejectValue("userId", MessageResponse.EXCEPTION_FIELD_INVALID.getMessage());
        }
        if (estate == null) {
            error.rejectValue("estateId", MessageResponse.EXCEPTION_FIELD_INVALID.getMessage());
        }

        return error.hasErrors() ? error : null;
    }
}
