package org.trams.sicbang.model.form;

import io.swagger.annotations.ApiModelProperty;
import org.apache.log4j.Logger;
import org.springframework.data.jpa.domain.Specification;
import org.trams.sicbang.common.utils.ConvertUtils;
import org.trams.sicbang.model.dto.BaseFormSearch;
import org.trams.sicbang.model.entity.Estate_;
import org.trams.sicbang.model.entity.User_;
import org.trams.sicbang.model.entity.Wishlist;
import org.trams.sicbang.model.entity.Wishlist_;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Created by voncount on 4/14/2016.
 */
public class FormWishlist extends BaseFormSearch<Wishlist> {
    private final Logger logger = Logger.getLogger(this.getClass());

    @ApiModelProperty(hidden = true)
    private String userId;
    @ApiModelProperty(hidden = true)
    private String estateId;

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

    @Override
    public Specification<Wishlist> getSpecification() {
        return (Root<Wishlist> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) -> {
            root.join(Wishlist_.user, JoinType.INNER);
            root.join(Wishlist_.estate, JoinType.INNER);

            List<Predicate> predicates = new ArrayList<>();
            Optional<Long> _userId = ConvertUtils.toLongNumber(userId);
            Optional<Long> _estateId = ConvertUtils.toLongNumber(estateId);

            if (_userId.isPresent()) {
                predicates.add(
                        criteriaBuilder.equal(root.get(Wishlist_.user).get(User_.id), _userId.get())
                );
            }
            if (_estateId.isPresent()) {
                predicates.add(
                        criteriaBuilder.equal(root.get(Wishlist_.estate).get(Estate_.id), _estateId.get())
                );
            }

            predicates.add(
                    criteriaBuilder.equal(root.get(Wishlist_.isDelete), isDelete)
            );

            if (predicates.isEmpty()) {
                return criteriaBuilder.isNotNull(root.get(Wishlist_.id));
            } else {
                return criteriaBuilder.and(predicates.toArray(new Predicate[] {}));
            }
        };
    }
}
