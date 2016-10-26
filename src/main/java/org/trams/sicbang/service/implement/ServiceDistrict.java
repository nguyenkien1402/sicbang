package org.trams.sicbang.service.implement;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.trams.sicbang.model.entity.City;
import org.trams.sicbang.model.entity.District;
import org.trams.sicbang.model.form.FormCity;
import org.trams.sicbang.model.form.FormDistrict;
import org.trams.sicbang.service.BaseService;
import org.trams.sicbang.service.IServiceCity;
import org.trams.sicbang.service.IServiceDistrict;

import java.util.List;

/**
 * Created by voncount on 10/17/16.
 */
@Service
public class ServiceDistrict extends BaseService implements IServiceDistrict {


    @Override
    public Page<District> filter(FormDistrict form) {
        return null;
    }

    @Override
    public District findOne(FormDistrict form) {
        return null;
    }

    @Override
    public List<District> findAll() {
        return repositoryDistrict.findAll();
    }

    @Override
    public List<District> findByCityId(int cityId) {
        return repositoryDistrict.findByCityId(cityId);
    }
}
