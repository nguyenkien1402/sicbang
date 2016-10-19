package org.trams.sicbang.service.implement;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.trams.sicbang.model.entity.District;
import org.trams.sicbang.model.entity.Town;
import org.trams.sicbang.model.form.FormDistrict;
import org.trams.sicbang.model.form.FormTown;
import org.trams.sicbang.service.BaseService;
import org.trams.sicbang.service.IServiceDistrict;
import org.trams.sicbang.service.IServiceTown;

import java.util.List;

/**
 * Created by voncount on 10/17/16.
 */
@Service
public class ServiceTown extends BaseService implements IServiceTown {


    @Override
    public Page<Town> filter(FormTown form) {
        return null;
    }

    @Override
    public Town findOne(FormTown form) {
        return null;
    }

    @Override
    public List<Town> findAll() {
        return repositoryTown.findAll();
    }
}
