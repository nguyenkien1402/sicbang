package org.trams.sicbang.service.implement;

import com.google.common.base.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.trams.sicbang.model.entity.Mail;
import org.trams.sicbang.model.entity.User;
import org.trams.sicbang.model.enumerate.MessageResponse;
import org.trams.sicbang.model.exception.ApplicationException;
import org.trams.sicbang.model.form.FormMail;
import org.trams.sicbang.repository.RepositoryMail;
import org.trams.sicbang.service.BaseService;
import org.trams.sicbang.service.IServiceMail;
import org.trams.sicbang.web.controller.AbstractController;

import javax.mail.internet.MimeMessage;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by voncount on 21/04/2016.
 */
@Service
@Transactional
public class ServiceMail extends BaseService implements IServiceMail{

    @Value("${spring.mail.username}")
    private String MAIL_USERNAME;
    @Value("${spring.mail.password}")
    private String MAIL_PASSWORD;
    @Value("${spring.mail.host}")
    private String MAIL_HOST;
    @Value("${spring.mail.default-encoding}")
    private String MAIL_DEFAULT_ENCODING;

    private ServiceUser serviceUser = new ServiceUser();

    @Autowired
    private RepositoryMail repositoryMail;
    @Autowired
    private JavaMailSenderImpl mailSender;


    @Override
    public List<Mail> filterBy(String type, int pageIndex,String mailSubject, String mailContent) {
        List<Mail> list = null;
        pageIndex = pageIndex * 10;
        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String[] dateFm = format.format(date).split("-");
        int year = Integer.parseInt(dateFm[0]);
        int month = Integer.parseInt(dateFm[1]);
        int day = Integer.parseInt(dateFm[2]);
            switch (type) {
                case "0":
                    System.out.println("find all");
                    list = repositoryMail.findAllMail(pageIndex, mailSubject, mailContent);
                case "1": { // email today
                    System.out.println("Email today");
                    list = repositoryMail.findByToDay(day, month, year, pageIndex, mailSubject, mailContent);
                    System.out.println("total element: " + list.size());
                    break;
                }
                case "2": // email one week
                {
                    System.out.println("Email one week");
                    list = repositoryMail.findByOneWeek(pageIndex, mailSubject, mailContent);
                    break;
                }
                case "3": // email 15 day
                {
                    System.out.println("Email 15 day nearest");
                    list = repositoryMail.findByFifteenDay(pageIndex, mailSubject, mailContent);
                    break;
                }
                case "4": // email one month
                {
                    System.out.println("Email 1 month nearest");
                    list = repositoryMail.findByMonth(0, pageIndex, mailSubject, mailContent);
                    break;
                }
                case "5": // email two month
                {
                    System.out.println("Email 2 month nearest");
                    list = repositoryMail.findByMonth(1, pageIndex, mailSubject, mailContent);
                    break;
                }
                case "6": // email three month
                {
                    System.out.println("Email 3 month nearest");
                    list = repositoryMail.findByMonth(2, pageIndex, mailSubject, mailContent);
                    break;
                }
            }
        return list;
    }

    @Override
    public List<Mail> filterByWithDate(int pageIndex, String mailSubject, String mailContent, String startDate, String endDate) {
        List<Mail> list = null;
        pageIndex = pageIndex * 10;
        String[] start = startDate.split("/");
        String[] end   = endDate.split("/");
        startDate = start[0]+"-"+start[1]+"-"+start[2];
        endDate = end[0]+"-"+end[1]+"-"+end[2];
        System.out.println("start date 3: "+startDate);
        System.out.println("end date 3: "+endDate);
        list = repositoryMail.findAllWithDate(pageIndex,mailSubject,mailContent,startDate,endDate);
        return list;
    }

    @Override
    public Long countAllElementWithDate(String mailSubject, String mailContent, String startDate, String endDate) {
        return repositoryMail.totalOfEmailWithDate( mailSubject,  mailContent, startDate,endDate);
    }

    @Override
    public Long countAllElement(String mailSubject, String mailContent) {
        return repositoryMail.totalOfEmail( mailSubject,  mailContent);
    }

    @Override
    public Long countOneWeek(String mailSubject, String mailContent) {
        return repositoryMail.totalMailOneWeek( mailSubject,  mailContent);
    }

    @Override
    public Long countOneFifteenDay(String mailSubject, String mailContent) {
        return repositoryMail.totalMailFifteen( mailSubject,  mailContent);
    }

    @Override
    public Long countMonth(int sub,String mailSubject, String mailContent) {
        return repositoryMail.totalMailMonth(sub,mailSubject, mailContent);
    }


    @Override
    public Long countToDay(String mailSubject, String mailContent) {
        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String[] dateFm = format.format(date).split("-");
        int year = Integer.parseInt(dateFm[0]);
        int month = Integer.parseInt(dateFm[1]);
        int day = Integer.parseInt(dateFm[2]);
        return repositoryMail.totalMailToday(day,month,year, mailSubject,  mailContent);
    }

    @Override
    public void send(FormMail form) {
        if(form.getEmailsTo().size() > 0) {
            List<String> emails = form.getEmailsTo();
                try {
                    logger.info("mail form.ToString : " + form.toString());
                    mailSender.setDefaultEncoding(MAIL_DEFAULT_ENCODING);
                    mailSender.setHost(MAIL_HOST);
                    mailSender.setUsername(MAIL_USERNAME);
                    mailSender.setPassword(MAIL_PASSWORD);

                    MimeMessage mimeMessage = mailSender.createMimeMessage();

                    MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
                    helper.setFrom(MAIL_USERNAME);
                    helper.setSubject(form.getMailSubject());
                    helper.setText(form.getMailContent(), true);

                    for (int i = 0 ; i < emails.size() ; i++) {
                        String recipient = emails.get(i);
                        helper.setTo(recipient);
                        helper.setReplyTo(recipient);

                        Set<MultipartFile> attachments = form.getAttachments();
                        if (attachments != null && !attachments.isEmpty()) {
                            for (MultipartFile attachment : attachments) {
                                helper.addAttachment(attachment.getOriginalFilename(), attachment);
                            }
                        }
                        // save mail
                        Mail mail = new Mail();
                        mail.setMailTo(recipient);
                        mail.setMailSubject(form.getMailSubject());
                        mail.setMailContent(form.getMailContent());
                        logger.info("mail recipient : " + recipient + "title : " + form.getMailSubject() + "form.getContent() : " + form.getMailContent());
//                String content = mail.getMailContent();
//                String enContent = Base64.getEncoder().encodeToString( content.getBytes( "utf-8" ) );
                        mail.encodeContent();
                        System.out.println("Encode 1: " + mail.getMailContent());
                        repositoryMail.save(mail);
//                mailSender.send(mimeMessage); // sau nay de cai nay len tren save mail
                    }
                } catch (Exception e) {
                    logger.error(e.getMessage(), e);
                }

        }
    }

    @Override
    public Page<Mail> filter(FormMail form) {
        form.setIsDelete(0);
        Page<Mail> list = null;
        try{
            list = repositoryMail.findAll(form.getSpecification(), form.getPaging());
            return list;
        }catch (Exception e){
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public void delete(FormMail form) {

        Mail mail = repositoryMail.findOne(form.getSpecification());
        if(mail == null){
            throw new ApplicationException(MessageResponse.EXCEPTION_NOT_FOUND);
        }
        mail.setIsDelete(1);
    }

    @Override
    public Mail findOne(FormMail form) {
        return repositoryMail.findOne(form.getSpecification());
    }


    @Override
    public void sendMulti(FormMail form) {

        // get list user
        List<User> listUser = new ArrayList<>();
        String type = form.converType();
        System.out.println("type: "+type);
        listUser = serviceUser.filterBy(type);
        System.out.println("list user: "+listUser.size());
//        for(int i = 0 ; i < listUser.size() ; i++) {
//            try {
//                logger.info("mail form.ToString : " + form.toString());
//                mailSender.setDefaultEncoding(MAIL_DEFAULT_ENCODING);
//                mailSender.setHost(MAIL_HOST);
//                mailSender.setUsername(MAIL_USERNAME);
//                mailSender.setPassword(MAIL_PASSWORD);
//
//                MimeMessage mimeMessage = mailSender.createMimeMessage();
//
//                MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
//                helper.setFrom(MAIL_USERNAME);
//                helper.setSubject(form.getTitle());
//                helper.setText(form.getContent(), true);
//
//                for (String recipient : form.getRecipient().split(",")) {
//                    helper.setTo(recipient);
//                    helper.setReplyTo(recipient);
//
//                    Set<MultipartFile> attachments = form.getAttachments();
//                    if (attachments != null && !attachments.isEmpty()) {
//                        for (MultipartFile attachment : attachments) {
//                            helper.addAttachment(attachment.getOriginalFilename(), attachment);
//                        }
//                    }
//
//
//                    // save mail
//                    Mail mail = new Mail();
//                    mail.setMailTo(recipient);
//                    mail.setMailSubject(form.getTitle());
//                    mail.setMailContent(form.getContent());
//                    logger.info("mail recipient : " + recipient + "title : " + form.getTitle() + "form.getContent() : " + form.getContent());
////                String content = mail.getMailContent();
////                String enContent = Base64.getEncoder().encodeToString( content.getBytes( "utf-8" ) );
//                    mail.encodeContent();
//                    System.out.println("Encode 1: " + mail.getMailContent());
//                    repositoryMail.save(mail);
////                mailSender.send(mimeMessage); // sau nay de cai nay len tren save mail
//                }
//
//            } catch (Exception e) {
//                logger.error(e.getMessage(), e);
//            }
//        }
    }



}
