package org.trams.sicbang.model.form;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.log4j.Logger;
import org.springframework.data.jpa.domain.Specification;
import org.trams.sicbang.common.utils.ConvertUtils;
import org.trams.sicbang.model.dto.BaseFormSearch;
import org.trams.sicbang.model.entity.Sanggun;
import org.trams.sicbang.model.entity.Sanggun_;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Created by voncount on 25/04/2016.
 */
@Getter @Setter
@ToString
public class FormBusiness extends BaseFormSearch<Sanggun> {


    private int serial_num;
    private String manage_id;
    @ApiModelProperty(value = "동코드")
    private String dong_code;
    @ApiModelProperty(value = "시/도")
    private String cityDegree;
    @ApiModelProperty(value = "구/군")
    private String township;
    @ApiModelProperty(value = "동 이름")
    private String dong_name;
    @ApiModelProperty(value = "리 이름")
    private String ri_name;

    public static Specification<Sanggun> cityLike(final String keyword) {
        return (Root<Sanggun> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) -> {
            Predicate predicate  = criteriaBuilder.like(root.get(Sanggun_.cityDegree), keyword + "%");
            return predicate;
        };
    }

    //조건을 정의할 수 있음
    @Override
    public Specification<Sanggun> getSpecification() {
        Logger logger = Logger.getLogger(this.getClass());

        return (Root<Sanggun> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) -> {
            //검색 조건 모아 놓은 클래스 를 리스트에 담아서 return
            List<Predicate> predicates = new ArrayList<>();
            criteriaQuery.distinct(true);
            Optional<Long> _zonePagingId = ConvertUtils.toLongNumber(dong_code);
//            logger.info("businessId : "+dong_code);
            if (_zonePagingId.isPresent()) {
                predicates.add(
                        criteriaBuilder.equal(root.get(Sanggun_.dong_code), _zonePagingId.get())
                );
            }

            if (predicates.isEmpty()) {
                return criteriaBuilder.isNotNull(root.get(Sanggun_.dong_code));
            } else {
                return criteriaBuilder.and(predicates.toArray(new Predicate[] {}));
            }
        };
    }
}
