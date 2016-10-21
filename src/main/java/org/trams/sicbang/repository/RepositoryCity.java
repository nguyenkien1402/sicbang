package org.trams.sicbang.repository;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.trams.sicbang.model.entity.City;
import org.trams.sicbang.model.entity.ReportInformation;

import java.util.List;

/**
 * Created by voncount on 19/04/2016.
 */
public interface RepositoryCity extends JpaRepository<City, Long>, JpaSpecificationExecutor<City> {

    @Cacheable(value = "cities")
    List<City> findAll();

    @Query(value = "SELECT * FROM city",nativeQuery = true)
    List<City> getAll();
}
