package org.trams.sicbang.repository;

import org.springframework.data.repository.CrudRepository;
import org.trams.sicbang.model.entity.UserPermission;

/**
 * Created by voncount on 4/8/16.
 */
public interface RepositoryUserPermission extends CrudRepository<UserPermission, Long> {

    UserPermission findByName(String name);

}
