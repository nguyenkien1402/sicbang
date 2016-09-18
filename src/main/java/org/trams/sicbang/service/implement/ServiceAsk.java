package org.trams.sicbang.service.implement;

import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.trams.sicbang.model.entity.Ask;
import org.trams.sicbang.model.enumerate.MessageResponse;
import org.trams.sicbang.model.exception.ApplicationException;
import org.trams.sicbang.model.form.FormAsk;
import org.trams.sicbang.service.BaseService;
import org.trams.sicbang.service.IServiceAsk;

/**
 * Created by voncount on 5/4/16.
 */
@Service
@Transactional
public class ServiceAsk extends BaseService implements IServiceAsk {

    @Override
    public Ask create(FormAsk form) {
        Ask ask = new Ask();
        BeanUtils.copyProperties(form, ask);
        return repositoryAsk.save(ask);
    }

    @Override
    public Ask update(FormAsk form) {
        Ask ask = repositoryAsk.findOne(form.getSpecification());
        if (ask == null) {
            throw new ApplicationException(MessageResponse.EXCEPTION_NOT_FOUND);
        }
        BeanUtils.copyProperties(form, ask);
        return repositoryAsk.save(ask);
    }

    @Override
    public Page<Ask> filter(FormAsk form) {
        return repositoryAsk.findAll(form.getSpecification(), form.getPaging());
    }

    @Override
    public void delete(FormAsk form) {
        Ask ask = repositoryAsk.findOne(form.getSpecification());
        if (ask == null) {
            throw new ApplicationException(MessageResponse.EXCEPTION_NOT_FOUND);
        }
        ask.setIsDelete(1);
    }

    @Override
    public Ask findOne(FormAsk form) {
        return repositoryAsk.findOne(form.getSpecification());
    }
}
