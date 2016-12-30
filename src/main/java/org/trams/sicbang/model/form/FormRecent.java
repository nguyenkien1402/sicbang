package org.trams.sicbang.model.form;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.jpa.domain.Specification;
import org.trams.sicbang.common.utils.ConvertUtils;
import org.trams.sicbang.model.dto.BaseFormSearch;
import org.trams.sicbang.model.entity.*;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Created by voncount on 4/14/2016.
 */
@Getter @Setter
public class FormRecent extends BaseFormSearch<Recent> {

    @ApiModelProperty(hidden = true)
    private String recentId;
    @ApiModelProperty(hidden = true)
    private String userId;
    @ApiModelProperty(hidden = true)
    private String estateId;

    private String modifiedDate;

    @Override
    public Specification<Recent> getSpecification() {
        return (Root<Recent> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) -> {
            root.join(Recent_.user, JoinType.INNER);
            root.join(Recent_.estate, JoinType.INNER);

            List<Predicate> predicates = new ArrayList<>();
            Optional<Long> _recentId = ConvertUtils.toLongNumber(recentId);
            Optional<Long> _userId = ConvertUtils.toLongNumber(userId);
            Optional<Long> _estateId = ConvertUtils.toLongNumber(estateId);
            System.out.println("recentId : "+recentId);
            System.out.println("userId : "+userId);
            System.out.println("estateId : "+estateId);
            if (_recentId.isPresent()) {
                predicates.add(
                        criteriaBuilder.equal(root.get(Recent_.id), _recentId.get())
                );
            }
            if (_userId.isPresent()) {
                predicates.add(
                        criteriaBuilder.equal(root.get(Recent_.user).get(User_.id), _userId.get())
                );
            }
            if (_estateId.isPresent()) {
                predicates.add(
                        criteriaBuilder.equal(root.get(Recent_.estate).get(Estate_.id), _estateId.get())
                );
            }

            predicates.add(
                    criteriaBuilder.equal(root.get(Recent_.isDelete), isDelete)
            );

            if (predicates.isEmpty()) {
                return criteriaBuilder.isNotNull(root.get(Wishlist_.id));
            } else {
                return criteriaBuilder.and(predicates.toArray(new Predicate[] {}));
            }
        };
    }

    public String getRecentId() {
        return recentId;
    }

    public void setRecentId(String recentId) {
        this.recentId = recentId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getEstateId() {
        return estateId;
    }

    public void setEstateId(String estateId) {
        this.estateId = estateId;
    }

    public String getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(String modifiedDate) {
        this.modifiedDate = modifiedDate;
    }
}
