package org.trams.sicbang.service;

import org.springframework.data.domain.Page;
import org.trams.sicbang.model.dto.CustomUserDetail;
import org.trams.sicbang.model.entity.User;
import org.trams.sicbang.model.form.FormLogin;
import org.trams.sicbang.model.form.FormPassword;
import org.trams.sicbang.model.form.FormUser;
import org.trams.sicbang.model.form.FormWithdraw;

import java.util.List;

/**
 * Created by voncount on 4/13/16.
 */
public interface IServiceUser {

    /**
     * Authen
     * @param form
     * @return
     */
    CustomUserDetail authenticateUser(FormLogin form);

    /**
     * Create
     * @param form
     * @return
     */
    User create(FormUser form);

    /**
     * Update
     * @param form
     * @return
     */
    User update(FormUser form);

    /**
     * Withdraw
     * @param form
     * @return
     */
    User withdraw(FormWithdraw form);

    /**
     * Filter
     * @param form
     * @return
     */
    Page<User> filter(FormUser form);

    /**
     * Delete
     * @param form
     */
    void delete(FormUser form);

    /**
     * Find one
     * @param form
     * @return
     */
    User findOne(FormUser form);

    User findByUserId(int userId);

    /**
     * Reset password
     * @param form
     * @return
     */
    User resetPassword(FormPassword form);


    /*
     * Filter
     * Param formUser
     * @return
     */
    List<User> filterBy(String type);




}
