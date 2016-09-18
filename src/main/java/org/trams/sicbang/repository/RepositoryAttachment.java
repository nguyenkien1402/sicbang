package org.trams.sicbang.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.trams.sicbang.model.entity.Attachment;

import java.util.Collection;

/**
 * Created by voncount on 4/14/2016.
 */
public interface RepositoryAttachment extends JpaRepository<Attachment, Long> {

    @Query(value = "select a from Attachment a where a.tableRef = :tableRef and a.rowRef = :rowRef")
    Collection<Attachment> findByReference(@Param("tableRef") String tableRef, @Param("rowRef") Long rowRef);

}
