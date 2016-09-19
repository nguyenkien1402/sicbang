package org.trams.sicbang.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.trams.sicbang.model.entity.Town;

/**
 * Created by voncount on 5/12/16.
 */
public interface RepositoryTown extends JpaRepository<Town, Long> {
}
