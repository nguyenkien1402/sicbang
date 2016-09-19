package org.trams.sicbang.repository;

import org.hibernate.annotations.NamedNativeQuery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.trams.sicbang.model.entity.BusinessType;
import org.trams.sicbang.model.entity.Sanggun;

import java.util.List;

/**
 * Created by voncount on 5/12/16.
 */
public interface RepositoryBusinessType extends JpaRepository<BusinessType, Long> {

}
