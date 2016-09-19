package org.trams.sicbang.repository;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.trams.sicbang.model.entity.Category;

import java.util.List;

/**
 * Created by voncount on 19/04/2016.
 */
public interface RepositoryCategory extends JpaRepository<Category, Long> {

    @Cacheable(value = "categories")
    List<Category> findAll();

    @Cacheable(value = "categoryByName")
    Category findByName(String name);

    @Query(value = "select c from Category c where c.id = :id")
    Category findById(@Param("id") Long id);
}
