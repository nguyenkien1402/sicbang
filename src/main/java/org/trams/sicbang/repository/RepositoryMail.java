package org.trams.sicbang.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.trams.sicbang.model.entity.Mail;

/**
 * Created by voncount on 19/04/2016.
 */
public interface RepositoryMail extends JpaRepository<Mail, Long>, JpaSpecificationExecutor<Mail> {
}
