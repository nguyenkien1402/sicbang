package org.trams.sicbang.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.trams.sicbang.model.entity.Estate;

/**
 * Created by voncount on 4/12/16.
 */
public interface RepositoryEstate extends JpaRepository<Estate, Long>, JpaSpecificationExecutor<Estate> {
}
