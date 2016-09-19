package org.trams.sicbang.service.implement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.trams.sicbang.model.entity.Mail;
import org.trams.sicbang.model.form.FormAdminImg;
import org.trams.sicbang.model.form.FormMail;
import org.trams.sicbang.repository.RepositoryMail;
import org.trams.sicbang.service.BaseService;
import org.trams.sicbang.service.IServiceAdmin;
import org.trams.sicbang.service.IServiceMail;

import java.util.List;
import java.util.Set;

/**
 * Created by voncount on 21/04/2016.
 */
@Service
@Transactional
public class ServiceAdmin extends BaseService implements IServiceAdmin {
    @Override
    public void create(FormAdminImg form) {
//        repositoryAdmin.create(form);
    }

    @Override
    public Page<Mail> filter(FormAdminImg form) {
        return null;
    }

    @Override
    public void delete(FormAdminImg form) {

    }

    @Override
    public Mail findOne(FormAdminImg form) {
        return null;
    }


//    @Override
//    public Page<Mail> filter(FormMail form) {
//        return repositoryMail.findAll(form.getSpecification(), form.getPaging());
//    }
//
//    @Override
//    public void delete(FormMail form) {
//        List<Mail> mails = repositoryMail.findAll(form.getSpecification());
//        repositoryMail.delete(mails);
//    }
//
//    @Override
//    public Mail findOne(FormMail form) {
//        return repositoryMail.findOne(form.getSpecification());
//    }

}
