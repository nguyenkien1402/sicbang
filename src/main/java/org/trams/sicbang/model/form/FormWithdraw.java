package org.trams.sicbang.model.form;

import io.swagger.annotations.ApiModelProperty;
import org.springframework.data.jpa.domain.Specification;
import org.trams.sicbang.common.utils.ConvertUtils;
import org.trams.sicbang.model.dto.BaseFormSearch;
import org.trams.sicbang.model.entity.*;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Created by voncount on 4/13/16.
 */
public class FormWithdraw extends BaseFormSearch<Withdrawal> {

    @ApiModelProperty(hidden = true)
    private String userId;
    private String password;
    private String content;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public Specification<Withdrawal> getSpecification() {
        return (Root<Withdrawal> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) -> {
            root.join(Withdrawal_.user, JoinType.INNER);

            List<Predicate> predicates = new ArrayList<>();
            Optional<Long> _userId = ConvertUtils.toLongNumber(userId);

            if (_userId.isPresent()) {
                predicates.add(
                        criteriaBuilder.equal(root.get(Withdrawal_.user).get(User_.id), _userId.get())
                );
            }

            predicates.add(
                    criteriaBuilder.equal(root.get(Withdrawal_.isDelete), isDelete)
            );

            if (predicates.isEmpty()) {
                return criteriaBuilder.isNotNull(root.get(Withdrawal_.id));
            } else {
                return criteriaBuilder.and(predicates.toArray(new Predicate[] {}));
            }
        };
    }
}
