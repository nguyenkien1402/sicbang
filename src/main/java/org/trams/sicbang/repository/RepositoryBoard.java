package org.trams.sicbang.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.trams.sicbang.model.entity.Board;

/**
 * Created by voncount on 25/04/2016.
 */
public interface RepositoryBoard extends JpaRepository<Board, Long>, JpaSpecificationExecutor<Board> {
}
