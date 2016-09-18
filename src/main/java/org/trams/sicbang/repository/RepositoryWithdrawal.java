package org.trams.sicbang.repository;

import org.springframework.data.repository.CrudRepository;
import org.trams.sicbang.model.entity.Withdrawal;

/**
 * Created by voncount on 4/12/16.
 */
public interface RepositoryWithdrawal extends CrudRepository<Withdrawal, Long> {
}
