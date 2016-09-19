package org.trams.sicbang.model.form;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.apache.log4j.Logger;
import org.springframework.data.jpa.domain.Specification;
import org.trams.sicbang.common.utils.ConvertUtils;
import org.trams.sicbang.model.dto.BaseFormSearch;
import org.trams.sicbang.model.entity.Board;
import org.trams.sicbang.model.entity.Board_;

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
public class FormBoard extends BaseFormSearch<Board> {

    @ApiModelProperty(hidden = true)
    private String boardId;

    private String title;
    private String content;

    @Override
    public Specification<Board> getSpecification() {
        Logger logger = Logger.getLogger(this.getClass());

        return (Root<Board> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) -> {

            List<Predicate> predicates = new ArrayList<>();
            Optional<Long> _boardId = ConvertUtils.toLongNumber(boardId);
//            logger.info("_boardId : "+_boardId);
            if (_boardId.isPresent()) {
                predicates.add(
                        criteriaBuilder.equal(root.get(Board_.id), _boardId.get())
                );
            }

            predicates.add(
                    criteriaBuilder.equal(root.get(Board_.isDelete), isDelete)
            );
            if (predicates.isEmpty()) {
                return criteriaBuilder.isNotNull(root.get(Board_.id));
            } else {
                return criteriaBuilder.and(predicates.toArray(new Predicate[] {}));
            }
        };
    }
}
