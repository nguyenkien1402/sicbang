package org.trams.sicbang.service;

import org.trams.sicbang.model.entity.BusinessType;
import org.trams.sicbang.model.entity.Category;

import java.util.List;

/**
 * Created by DinhTruong on 10/8/2016.
 */
public interface IServiceBusinessType {
    List<BusinessType> findAll();
    List<Category> findAllCategory();
    List<BusinessType> findByCategory(Long categoryId);
}
