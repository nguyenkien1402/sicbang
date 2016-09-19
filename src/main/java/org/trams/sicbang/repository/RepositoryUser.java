package org.trams.sicbang.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.trams.sicbang.model.entity.Mail;
import org.trams.sicbang.model.entity.User;

import java.util.List;

/**
 * Created by voncount on 4/8/16.
 */
public interface RepositoryUser extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {

    User findByEmail(String email);

    @Query(value="select * from user where id = ?1 ", nativeQuery = true)
    User findByUserId(int id);

    @Query(value = "SELECT * FROM user u WHERE MONTH(u.created_date) = :month AND YEAR(u.created_date) = :year", nativeQuery = true)
    List<User> findBySignUpLastMonth(@Param("month") Integer month, @Param("year") Integer year);

    @Query(value = "SELECT * FROM user u " +
            "WHERE MONTH(u.created_date) = :month AND YEAR(u.created_date) = :year AND DAY(u.created_date) = :day", nativeQuery = true)
    List<User> findBySignUpToDay(@Param("day") Integer day, @Param("month") Integer month, @Param("year") Integer year);


}
