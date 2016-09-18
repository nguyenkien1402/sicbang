package org.trams.sicbang.repository;

import org.springframework.data.repository.CrudRepository;
import org.trams.sicbang.model.entity.UserRole;

/**
 * Created by voncount on 4/8/16.
 */
public interface RepositoryUserRole extends CrudRepository<UserRole, Long> {

    UserRole findByName(String name);

}
