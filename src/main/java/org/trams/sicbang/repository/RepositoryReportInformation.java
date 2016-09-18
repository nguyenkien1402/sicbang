package org.trams.sicbang.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.trams.sicbang.model.entity.ReportInformation;

/**
 * Created by voncount on 4/14/2016.
 */
public interface RepositoryReportInformation extends JpaRepository<ReportInformation, Long>, JpaSpecificationExecutor<ReportInformation> {
}
