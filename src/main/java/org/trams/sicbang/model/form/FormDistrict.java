package org.trams.sicbang.model.form;

import com.google.common.base.Strings;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.criterion.CriteriaQuery;
import org.springframework.data.jpa.domain.Specification;
import org.trams.sicbang.model.dto.BaseFormSearch;
import org.trams.sicbang.model.entity.*;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by voncount on 5/4/16.
 */
public class FormDistrict extends BaseFormSearch<District> {

    private String id;
    private String name;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public Specification<District> getSpecification() {
        return (Root<District> root, javax.persistence.criteria.CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (!Strings.isNullOrEmpty(name)) {
                predicates.add(
                        criteriaBuilder.equal(root.get(District_.name), name)
                );
            }

            predicates.add(
                    criteriaBuilder.equal(root.get(District_.isDelete), isDelete)
            );

            if (predicates.isEmpty()) {
                return criteriaBuilder.isNotNull(root.get(District_.id));
            } else {
                return criteriaBuilder.and(predicates.toArray(new Predicate[] {}));
            }
        };
    }
}