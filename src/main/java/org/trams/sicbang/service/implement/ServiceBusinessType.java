package org.trams.sicbang.service.implement;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.trams.sicbang.model.entity.BusinessType;
import org.trams.sicbang.model.entity.Category;
import org.trams.sicbang.service.BaseService;
import org.trams.sicbang.service.IServiceBusinessType;

import java.util.List;

/**
 * Created by DinhTruong on 10/8/2016.
 */
@Service
@Transactional
public class ServiceBusinessType extends BaseService implements IServiceBusinessType {

    @Override
    public List<BusinessType> findAll() {
        List<BusinessType> businessTypes = repositoryBusinessType.findAll();
        return businessTypes;
    }

    @Override
    public List<Category> findAllCategory() {
        List<Category> categories = repositoryCategory.findAll();
        return categories;
    }

    @Override
    public List<BusinessType> findByCategory(Long categoryId) {
        List<BusinessType> businessTypes = repositoryBusinessType.findByCategory(categoryId);
        return businessTypes;
    }
}
