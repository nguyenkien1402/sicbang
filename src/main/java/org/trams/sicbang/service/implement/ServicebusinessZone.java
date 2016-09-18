package org.trams.sicbang.service.implement;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.trams.sicbang.model.entity.*;
import org.trams.sicbang.model.form.FormBusiness;
import org.trams.sicbang.repository.RepositorySanggun;
import org.trams.sicbang.service.BaseService;
import org.trams.sicbang.service.IServicebusinessZone;

import java.util.List;

/**
 * Created by voncount on 19/04/2016.
 */
@Service
@Transactional
public class ServicebusinessZone extends BaseService implements IServicebusinessZone {

    private final Logger logger = Logger.getLogger(this.getClass());

    @Autowired
    private RepositorySanggun repositorySanggun;

    @Override
    public List<Sanggun> filter(FormBusiness form) {
//        Specification<Sanggun> spec = Specifications.where(form.cityLike(form.getCityDegree()));
        form.setOrders("township");
        logger.info("getPageIndex : "+form.getPageIndex());
        logger.info("getPageSize : "+form.getPageSize());

        List<Sanggun> zone = repositorySanggun.findAll(form.cityLike(form.getCityDegree()));
        return zone;
    }

    @Override
    public List<Sanggun> findAll() {
        return repositorySanggun.findAll();
    }

    @Override
    public Page<Sanggun> basicFilter(FormBusiness form) {
//        Specification<Sanggun> spec = Specifications.where(form.cityLike(form.getCityDegree()));
        Page<Sanggun> zone = repositorySanggun.findAll(form.getSpecification(),form.getPaging());
        return zone;
    }

    @Override
    public List<String> findTownList(String city_degree) {
        List<String> sangguns = repositorySanggun.findTownList(city_degree);
        return sangguns;
    }

    @Override
    public List<String> findDistinctSanggunBycityDegree(String city) {
        return null;
    }

    @Override
    public List<String> findDongList(String city_degree, String township) {
        List<String> sangguns = repositorySanggun.findDongList(city_degree,township);
        return sangguns;
    }


}
