package org.trams.sicbang.service.implement;

import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.trams.sicbang.model.entity.Notice;
import org.trams.sicbang.model.enumerate.MessageResponse;
import org.trams.sicbang.model.exception.ApplicationException;
import org.trams.sicbang.model.form.FormNotice;
import org.trams.sicbang.service.BaseService;
import org.trams.sicbang.service.IServiceNotice;

/**
 * Created by voncount on 25/04/2016.
 */
@Service
@Transactional
public class ServiceNotice extends BaseService implements IServiceNotice {

    @Override
    public Notice create(FormNotice form) {
        Notice notice = new Notice();
        BeanUtils.copyProperties(form, notice);
        return repositoryNotice.save(notice);
    }

    @Override
    public Notice update(FormNotice form) {
        Notice notice = repositoryNotice.findOne(form.getSpecification());
        if (notice == null) {
            throw new ApplicationException(MessageResponse.EXCEPTION_NOT_FOUND);
        }
        BeanUtils.copyProperties(form, notice);
        return repositoryNotice.save(notice);
    }

    @Override
    public Page<Notice> filter(FormNotice form) {
        return repositoryNotice.findAll(form.getSpecification(), form.getPaging());
    }

    @Override
    public void delete(FormNotice form) {
        Notice notice = repositoryNotice.findOne(form.getSpecification());
        if (notice == null) {
            throw new ApplicationException(MessageResponse.EXCEPTION_NOT_FOUND);
        }
        notice.setIsDelete(1);
    }

    @Override
    public Notice findOne(FormNotice form) {
        return repositoryNotice.findOne(form.getSpecification());
    }

}
