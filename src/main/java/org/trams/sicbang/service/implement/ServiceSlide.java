package org.trams.sicbang.service.implement;

import com.google.common.base.Strings;
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

import java.io.ByteArrayInputStream;
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
    public Integer uploadSlide(FormSlide form, String username) {
        System.out.println("Upload image");
        // get admin user, who uploaded image
        MultipartFile slide = form.getAttachments();
        System.out.println("user name: "+username);
        FormUser formUser = new FormUser();
        formUser.setEmailAbsolute(username);
        User user = serviceUser.findOne(formUser);
        System.out.println("id: "+user.getId());
        Slide file ;
        int check = 0;
        String link = Strings.isNullOrEmpty(form.getLink()) ? "" : form.getLink();
        if(slide != null && !slide.isEmpty()){
            try {
                String fileRelativePath[], fileUrl, thumbUrl;
                if(form.getType().equals("WEB") || form.getType().equals("APP"))
                    fileRelativePath = FileUtils.uploadImage(slide.getInputStream(), configParams.UPLOAD_DIRECTORY,"/slide/");
                else
                    fileRelativePath = FileUtils.uploadImage(slide.getInputStream(), configParams.UPLOAD_DIRECTORY,"/popup/");
                System.out.println("fileRelativePath:" +fileRelativePath[0]);
                fileUrl = configParams.BASE_URL +"/public" + fileRelativePath[0];
                thumbUrl = configParams.BASE_URL + "/public" + fileRelativePath[1];
                System.out.println("file url: "+fileUrl);
                file = new Slide();
                if(form.getType().equals("APP")){
                    file.setImgUrl(fileUrl);
                    file.setName(form.getAttachments().getOriginalFilename());
                    file.setType("APP");
                    file.setLink(link);
                    file.setIsDelete(0);
                    file.setUser(user);
                }else if(form.getType().equals("WEB")){
                    file.setImgUrl(fileUrl);
                    file.setName(form.getAttachments().getOriginalFilename());
                    file.setType("WEB");
                    file.setLink(link);
                    file.setIsDelete(0);
                    file.setUser(user);
                }else if(form.getType().equals("MAIN")){
                    FormSlide search = new FormSlide();
                    search.setType("MAIN");
                    Slide s = findOne(search);
                    if(s==null) {
                        file.setImgUrl(fileUrl);
                        file.setName(form.getAttachments().getOriginalFilename());
                        file.setType("MAIN");
                        file.setLink(link);
                        file.setIsDelete(0);
                        file.setUser(user);
                    }else{
                        s.setImgUrl(fileUrl);
                        s.setName(form.getAttachments().getOriginalFilename());
                        s.setLink(link);
                        repositorySlide.save(s);
                        return 1;
                    }
                }else{
                    file.setImgUrl(fileUrl);
                    file.setName(form.getAttachments().getOriginalFilename());
                    file.setType("POPUP");
                    file.setIsDelete(0);
                    file.setUser(user);
                }
                repositorySlide.save(file);
                check = 1;
            }catch (Exception e){
                throw new ApplicationException(MessageResponse.EXCEPTION_FILEUPLOAD_FAILED);
            }
        }
        return check;
    }

    @Override
    public Integer uploadMainImg(FormSlide form, String username) {
        System.out.println("Upload main image");
        // get admin user, who uploaded image
        MultipartFile slide = form.getAttachments();
        FormUser formUser = new FormUser();
        formUser.setEmailAbsolute(username);
        User user = serviceUser.findOne(formUser);
        System.out.println("id: "+user.getId());
        Slide file = new Slide();
        int check = 0;

        if(slide != null && !slide.isEmpty()){
            try {
                String fileRelativePath[], fileUrl, thumbUrl;
                fileRelativePath = FileUtils.uploadImage(slide.getInputStream(), configParams.UPLOAD_DIRECTORY,"/slide/");
                System.out.println("fileRelativePath:" +fileRelativePath[0]);
                fileUrl = configParams.BASE_URL +"/public" + fileRelativePath[0];
                thumbUrl = configParams.BASE_URL + "/public" + fileRelativePath[1];
                System.out.println("file url: "+fileUrl);
                file = new Slide();
//                file.setWebUrl(fileUrl);
                file.setName(form.getAttachments().getOriginalFilename());
                file.setType("MAIN");
                file.setIsDelete(0);
                file.setUser(user);
                repositorySlide.save(file);
                check = 1;
            }catch (Exception e){
                throw new ApplicationException(MessageResponse.EXCEPTION_FILEUPLOAD_FAILED);
            }
        }
        return check;
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
    public Integer delete(FormSlide formSlide) {
        Slide slide = repositorySlide.findOne(Long.parseLong(formSlide.getId()));
        if(slide == null){
            return 0;
        }
        slide.setIsDelete(1);
        return 1;
    }

    @Override
    public Slide findOne(FormSlide form) {
        Slide s = null;
        if(repositorySlide.findOne(form.getSpecification())!=null){
            s = repositorySlide.findOne(form.getSpecification());
        }
        return s;
    }
}
