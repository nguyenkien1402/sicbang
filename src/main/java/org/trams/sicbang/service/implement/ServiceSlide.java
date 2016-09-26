package org.trams.sicbang.service.implement;

import javafx.application.Application;
import org.apache.commons.io.IOExceptionWithCause;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.trams.sicbang.common.utils.FileUtils;
import org.trams.sicbang.config.ConfigParams;
import org.trams.sicbang.model.entity.Slide;
import org.trams.sicbang.model.entity.User;
import org.trams.sicbang.model.enumerate.MessageResponse;
import org.trams.sicbang.model.exception.ApplicationException;
import org.trams.sicbang.model.form.FormSlide;
import org.trams.sicbang.model.form.FormUser;
import org.trams.sicbang.repository.RepositorySlide;
import org.trams.sicbang.service.IServiceSlide;
import org.trams.sicbang.service.IServiceUser;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

/**
 * Created by KienNT on 8/19/2016.
 */
@Service
public class ServiceSlide implements IServiceSlide {

    @Autowired
    private RepositorySlide repositorySlide;

    @Autowired
    protected ConfigParams configParams;

    @Autowired
    private IServiceUser serviceUser;


    @Override
    public Slide uploadSlide(FormSlide form, String username) {
        System.out.println("Upload image");
        // get admin user, who uploaded image
        MultipartFile slide = form.getAttachments();
        FormUser formUser = new FormUser();
        formUser.setEmail(username);
        User user = serviceUser.findOne(formUser);
        System.out.println("id: "+user.getId());
        if(form.getLink()!= null && !form.getLink().trim().isEmpty()){
            Slide file = new Slide();
            if(form.getType().equals("APP")){
                file.setAppUrl(form.getLink());
//                file.setName(form.getLink());
                file.setType("APP");
                file.setIsDelete(0);
                file.setUser(user);
            }else{
                file.setWebUrl(form.getLink());
//                file.setName(form.getAttachments().getOriginalFilename());
                file.setType("WEB");
                file.setIsDelete(0);
                file.setUser(user);
            }
            repositorySlide.save(file);
        }
        if(slide != null && !slide.isEmpty()){
            try {
                String fileRelativePath[], fileUrl;
                fileRelativePath = FileUtils.uploadImage(slide.getInputStream(), configParams.UPLOAD_DIRECTORY,"/slide/");
                System.out.println("fileRelativePath:" +fileRelativePath[0]);
                fileUrl = configParams.BASE_URL +"/public" + fileRelativePath[0];
                System.out.println("file url: "+fileUrl);
                Slide file = new Slide();
                if(form.getType().equals("APP")){
                    file.setAppUrl(fileUrl);
                    file.setName(form.getAttachments().getOriginalFilename());
                    file.setType("APP");
                    file.setIsDelete(0);
                    file.setUser(user);
                }else if(form.getType().equals("WEB")){
                    file.setWebUrl(fileUrl);
                    file.setName(form.getAttachments().getOriginalFilename());
                    file.setType("WEB");
                    file.setIsDelete(0);
                    file.setUser(user);
                }else{
                    file.setWebUrl(fileUrl);
                    file.setName(form.getAttachments().getOriginalFilename());
                    file.setType("POPUP");
                    file.setIsDelete(0);
                    file.setUser(user);
                }
                repositorySlide.save(file);
                return file;
            }catch (IOException e){
                throw new ApplicationException(MessageResponse.EXCEPTION_FILEUPLOAD_FAILED);
            }
        }
        return null;
    }

    @Override
    public Page<Slide> filter(FormSlide form) {
        Page<Slide> slides = repositorySlide.findAll(form.getSpecification(), form.getPaging());
        return slides;
    }

    @Override
    public Slide fileterPopup(FormSlide form) {
        Slide slide = null;
        slide = repositorySlide.findPopup();
        return slide;
    }

    @Override
    public List<Slide> filterPopup(FormSlide form) {
        List<Slide> list = null;
        list = repositorySlide.findAll(form.getSpecification());
        return list;
    }

    @Transactional
    @Override
    public void delete(FormSlide formSlide) {
        Slide slide = repositorySlide.findOne(Long.parseLong(formSlide.getId()));
        if(slide == null){
            throw new ApplicationException(MessageResponse.EXCEPTION_NOT_FOUND);
        }
        slide.setIsDelete(1);
    }
}
