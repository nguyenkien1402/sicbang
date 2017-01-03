package org.trams.sicbang.service.implement;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.trams.sicbang.model.entity.Attachment;
import org.trams.sicbang.model.entity.Estate;
import org.trams.sicbang.model.entity.Recent;
import org.trams.sicbang.model.entity.User;
import org.trams.sicbang.model.form.FormRecent;
import org.trams.sicbang.service.BaseService;
import org.trams.sicbang.service.IServiceRecent;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;

/**
 * Created by voncount on 22/04/2016.
 */
@Service
@Transactional
public class ServiceRecent extends BaseService implements IServiceRecent {

    @Override
    public Recent create(FormRecent form) {
        String estateId = form.getEstateId();
        String userId = form.getUserId();

        User user = repositoryUser.findOne(Long.parseLong(userId));
        Estate estate = repositoryEstate.findOne(Long.parseLong(estateId));

        Recent recent = new Recent();
        recent.setUser(user);
        recent.setEstate(estate);
        return repositoryRecent.save(recent);
    }

    @Override
    public Recent update(FormRecent form) {
        System.out.println("update recent");
        Recent recent = repositoryRecent.findOne(form.getSpecification());
        DateFormat dt = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.0");
        Date date = new Date();
        recent.setModifiedDate(date);
        return repositoryRecent.save(recent);
    }

    @Override
    public Page<Recent> filter(FormRecent form) {
        int pageIndex = form.getPageIndex();
        int pageSize = form.getPageSize();
        PageRequest pageRequest = new PageRequest(pageIndex,pageSize, Sort.Direction.DESC,"modifiedDate");
        Page<Recent> recents = repositoryRecent.findAll(form.getSpecification(), pageRequest);
        System.err.println(recents.getTotalElements());
        for (Recent recent : recents) {
            Collection<Attachment> attachments = repositoryAttachment.findByReference("estate", recent.getEstate().getId());
            recent.getEstate().setAttachments(attachments);
        }
        return recents;
    }

    @Override
    public void delete(FormRecent form) {
        Recent recent = repositoryRecent.findOne(form.getSpecification());
        if (recent != null) {
            recent.setIsDelete(1);
        }
    }

    @Override
    public Recent findOne(FormRecent formRecent) {
        return repositoryRecent.findOne(formRecent.getSpecification());
    }


}
