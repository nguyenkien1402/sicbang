package org.trams.sicbang.repository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.trams.sicbang.model.entity.Ask;

/**
 * Created by voncount on 5/4/16.
 */
public interface RepositoryAsk extends CrudRepository<Ask, Long>, JpaSpecificationExecutor<Ask> {
}
