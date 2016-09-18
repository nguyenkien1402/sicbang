package org.trams.sicbang.service;

import org.springframework.data.domain.Page;
import org.trams.sicbang.model.entity.Board;
import org.trams.sicbang.model.form.FormBoard;

/**
 * Created by voncount on 25/04/2016.
 */
public interface IServiceBoard {

    /**
     * Create
     * @param form
     * @return
     */
    Board create(FormBoard form);

    /**
     * Update
     * @param form
     * @return
     */
    Board update(FormBoard form);

    /**
     * Filter
     * @param form
     * @return
     */
    Page<Board> filter(FormBoard form);

    /**
     * Delete
     * @param form
     */
    void delete(FormBoard form);

    /**
     * Find one
     * @param form
     * @return
     */
    Board findOne(FormBoard form);

}
