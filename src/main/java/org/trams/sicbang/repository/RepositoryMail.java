package org.trams.sicbang.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.access.method.P;
import org.trams.sicbang.model.entity.Mail;

import java.util.List;

/**
 * Created by voncount on 19/04/2016.
 */
public interface RepositoryMail extends JpaRepository<Mail, Long>, JpaSpecificationExecutor<Mail> {


    @Query(value = "SELECT * FROM mail m WHERE m.is_delete = 0 " +
            "AND m.mail_subject like :subject AND m.mail_content like :content LIMIT :page, 10 ", nativeQuery = true)
    List<Mail> findAllMail(@Param("page") Integer page, @Param("subject") String mailSubject, @Param("content") String mailContent);

    /*
     * find all email with date
     */
    @Query(value = "SELECT * FROM mail m WHERE m.is_delete = 0 " +
            "AND m.mail_subject like :subject AND m.mail_content like :content AND m.created_date BETWEEN :start and :enddate LIMIT :page, 10 ", nativeQuery = true)
    List<Mail> findAllWithDate(@Param("page") Integer page, @Param("subject") String mailSubject, @Param("content") String mailContent, @Param("start") String start, @Param("enddate") String end);

    /*
    * find all email in today
     */
    @Query(value = "SELECT * FROM mail m " +
            "WHERE MONTH(m.created_date) = :month AND YEAR(m.created_date) = :year AND DAY(m.created_date) = :day AND m.is_delete = 0 " +
            "AND m.mail_subject like :subject AND m.mail_content like :content LIMIT :page, 10 ", nativeQuery = true)
    List<Mail> findByToDay(@Param("day") Integer day, @Param("month") Integer month, @Param("year") Integer year, @Param("page") Integer page, @Param("subject") String mailSubject, @Param("content") String mailContent);

    /*
     * find all email in month
     */
    @Query(value = "SELECT * FROM mail m WHERE MONTH(m.created_date) >= MONTH(NOW())- :sub AND YEAR(u.created_date) = YEAR(NOW()) " +
            "AND u.is_delete = 0  AND m.mail_subject like :subject AND m.mail_content like :content LIMIT :page,10", nativeQuery = true)
    List<Mail> findByMonth(@Param("sub") Integer sub,@Param("page") Integer page,@Param("subject") String mailSubject, @Param("content") String mailContent);


    /*
     * find all email in one week
     */
    @Query(value = "SELECT * FROM mail m WHERE YEARWEEK(m.created_date) = YEARWEEK(NOW()) AND m.is_delete = 0 " +
            " AND m.mail_subject like :subject AND m.mail_content like :content LIMIT :page,10 ", nativeQuery = true)
    List<Mail> findByOneWeek(@Param("page") Integer page, @Param("subject") String mailSubject, @Param("content") String mailContent);

    /*
     * find all email in fifteen day
     */
    @Query(value = "SELECT * FROM mail m WHERE m.created_date BETWEEN DATE_SUB(NOW(),INTERVAL 15 DAY) AND NOW() AND m.is_delete = 0  " +
            "AND m.mail_subject like :subject AND m.mail_content like :content LIMIT :page,10 ", nativeQuery = true)
    List<Mail> findByFifteenDay(@Param("page") Integer page, @Param("subject") String mailSubject, @Param("content") String mailContent);


    @Query(value = "SELECT COUNT(m.id) as count FROM mail m WHERE m.is_delete = 0  AND m.mail_subject like :subject AND m.mail_content like :content ", nativeQuery = true)
    Long totalOfEmail(@Param("subject") String mailSubject, @Param("content") String mailContent);


    /*
     * total of email filter by start date and end date
     */
    @Query(value = "SELECT COUNT(m.id) as count FROM mail m WHERE m.is_delete = 0  AND m.mail_subject like :subject AND m.mail_content like :content AND m.created_date BETWEEN :start and :enddate", nativeQuery = true)
    Long totalOfEmailWithDate(@Param("subject") String mailSubject, @Param("content") String mailContent, @Param("start") String start, @Param("enddate") String end);


    @Query(value = "SELECT COUNT(m.id) as count FROM mail m WHERE YEARWEEK(m.created_date) = YEARWEEK(NOW()) AND m.is_delete = 0  " +
            "AND m.mail_subject like :subject AND m.mail_content like :content ", nativeQuery = true)
    Long totalMailOneWeek(@Param("subject") String mailSubject, @Param("content") String mailContent);

    @Query(value = "SELECT count(m.id) as COUNT FROM mail m WHERE m.created_date BETWEEN DATE_SUB(NOW(),INTERVAL 15 DAY) AND NOW() " +
            "AND m.is_delete = 0  AND m.mail_subject like :subject AND m.mail_content like :content ", nativeQuery = true)
    Long totalMailFifteen(@Param("subject") String mailSubject, @Param("content") String mailContent);

    @Query(value = "SELECT count(m.id) as COUNT from mail m WHERE MONTH(m.created_date) >= MONTH(NOW())- :sub " +
            "AND YEAR(m.created_date) = YEAR(NOW()) AND m.is_delete = 0  AND m.mail_subject like :subject AND m.mail_content like :content ", nativeQuery = true)
    Long totalMailMonth(@Param("sub") Integer sub,@Param("subject") String mailSubject, @Param("content") String mailContent);

    @Query(value = "SELECT count(m.id) as count FROM mail m " +
            "WHERE MONTH(m.created_date) = :month AND YEAR(m.created_date) = :year AND DAY(m.created_date) = :day AND m.is_delete = 0  " +
            "AND m.mail_subject like :subject AND m.mail_content like :content ", nativeQuery = true)
    Long totalMailToday(@Param("day") Integer day, @Param("month") Integer month, @Param("year") Integer year,@Param("subject") String mailSubject, @Param("content") String mailContent);

}
