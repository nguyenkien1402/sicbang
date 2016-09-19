package org.trams.sicbang.service;

import org.springframework.data.domain.Page;
import org.trams.sicbang.model.entity.Wishlist;
import org.trams.sicbang.model.form.FormWishlist;

/**
 * Created by voncount on 22/04/2016.
 */
public interface IServiceWishlist {

    /**
     * Create
     * @param form
     * @return
     */
    Wishlist create(FormWishlist form);

    /**
     * Update
     * @param form
     * @return
     */
    Wishlist update(FormWishlist form);

    /**
     * Filter
     * @param form
     * @return
     */
    Page<Wishlist> filter(FormWishlist form);

    /**
     * Delete
     * @param form
     */
    void delete(FormWishlist form);

    /**
     * Find one
     * @param form
     * @return
     */
    Wishlist findOne(FormWishlist form);

}
