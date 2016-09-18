package org.trams.sicbang.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.trams.sicbang.model.entity.Notice;

/**
 * Created by voncount on 25/04/2016.
 */
public interface RepositoryNotice extends JpaRepository<Notice, Long>, JpaSpecificationExecutor<Notice> {
}
