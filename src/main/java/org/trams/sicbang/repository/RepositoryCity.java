package org.trams.sicbang.repository;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.trams.sicbang.model.entity.City;

import java.util.List;

/**
 * Created by voncount on 19/04/2016.
 */
public interface RepositoryCity extends JpaRepository<City, Long> {

    @Cacheable(value = "cities")
    List<City> findAll();
}
