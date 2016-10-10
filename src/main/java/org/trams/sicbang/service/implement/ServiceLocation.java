package org.trams.sicbang.service.implement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.trams.sicbang.model.entity.Category;
import org.trams.sicbang.model.entity.City;
import org.trams.sicbang.model.entity.District;
import org.trams.sicbang.repository.RepositoryCategory;
import org.trams.sicbang.repository.RepositoryCity;
import org.trams.sicbang.repository.RepositoryDistrict;
import org.trams.sicbang.service.IServiceLocation;

import java.util.List;

/**
 * Created by voncount on 19/04/2016.
 */
@Service
public class ServiceLocation implements IServiceLocation {

    @Autowired
    private RepositoryCity repositoryCity;

    @Autowired
    private RepositoryDistrict repositoryDistrict;

    @Autowired
    private RepositoryCategory repositoryCategory;

    @Override
    public List<City> findAllCity() {
        return repositoryCity.findAll();
    }

    @Override
    public List<Category> findAllCategory() {
        return repositoryCategory.findAll();
    }

    @Override
    public List<District> findAllDistrict(){ return repositoryDistrict.findAll();}
}
