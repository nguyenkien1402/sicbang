package org.trams.sicbang.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.trams.sicbang.model.entity.Attachment;
import org.trams.sicbang.model.entity.Slide;

import java.util.Collection;

/**
 * Created by KienNT on 08/19/2016.
 */
public interface RepositorySlide extends JpaRepository<Slide, Long>, JpaSpecificationExecutor<Slide> {

    @Query(
            value="SELECT * FROM slide s WHERE s.type='POPUP' ORDER BY s.id DESC LIMIT 1", nativeQuery = true
    )
    Slide findPopup();
}
