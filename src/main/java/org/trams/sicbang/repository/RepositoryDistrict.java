package org.trams.sicbang.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.trams.sicbang.model.entity.City;
import org.trams.sicbang.model.entity.District;

import java.util.List;

/**
 * Created by voncount on 5/12/16.
 */
public interface RepositoryDistrict extends JpaRepository<District, Long>, JpaSpecificationExecutor<District> {

    @Query(value = "SELECT * FROM district d WHERE d.city_id = :cityId", nativeQuery = true)
    List<District> findByCityId(@Param("cityId") Integer cityId);
}
