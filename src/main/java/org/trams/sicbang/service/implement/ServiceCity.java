package org.trams.sicbang.service.implement;

import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.trams.sicbang.model.entity.Ask;
import org.trams.sicbang.model.entity.City;
import org.trams.sicbang.model.enumerate.MessageResponse;
import org.trams.sicbang.model.exception.ApplicationException;
import org.trams.sicbang.model.form.FormAsk;
import org.trams.sicbang.model.form.FormCity;
import org.trams.sicbang.service.BaseService;
import org.trams.sicbang.service.IServiceAsk;
import org.trams.sicbang.service.IServiceCity;

import java.util.Collection;
import java.util.List;

/**
 * Created by voncount on 10/17/16.
 */
@Service
public class ServiceCity extends BaseService implements IServiceCity {


    @Override
    public Page<City> filter(FormCity form) {
        return null;
    }

    @Override
    public City findOne(FormCity form) {
        return null;
    }

    @Override
    public List<City> findAll() {
        return repositoryCity.getAll();
    }
}
