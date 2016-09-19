package org.trams.sicbang.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.trams.sicbang.model.entity.District;

/**
 * Created by voncount on 5/12/16.
 */
public interface RepositoryDistrict extends JpaRepository<District, Long> {
}
