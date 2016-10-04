package org.trams.sicbang.service;

import org.springframework.data.domain.Page;
import org.trams.sicbang.model.entity.Slide;
import org.trams.sicbang.model.form.FormSlide;

import java.util.List;

/**
 * Created by root on 8/19/2016.
 */
public interface IServiceSlide {

    Integer uploadSlide(FormSlide form, String username);

    /*
     * filter
     * @Param: form
     * @return
     */
    Page<Slide> filter(FormSlide form);

    Slide fileterPopup(FormSlide form);

    List<Slide> filterPopup(FormSlide form);

    void delete(FormSlide formSlide);

}
