package org.trams.sicbang.service.implement;

import com.google.common.base.Strings;
import com.google.common.collect.Interner;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.trams.sicbang.common.utils.ConvertUtils;
import org.trams.sicbang.common.utils.FileUtils;
import org.trams.sicbang.model.dto.CustomAuthority;
import org.trams.sicbang.model.dto.CustomUserDetail;
import org.trams.sicbang.model.entity.*;
import org.trams.sicbang.model.enumerate.CommonStatus;
import org.trams.sicbang.model.enumerate.MessageResponse;
import org.trams.sicbang.model.enumerate.UserType;
import org.trams.sicbang.model.exception.ApplicationException;
import org.trams.sicbang.model.form.FormLogin;
import org.trams.sicbang.model.form.FormPassword;
import org.trams.sicbang.model.form.FormUser;
import org.trams.sicbang.model.form.FormWithdraw;
import org.trams.sicbang.service.BaseService;
import org.trams.sicbang.service.IServiceFile;
import org.trams.sicbang.service.IServiceUser;

import java.io.ByteArrayInputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * Created by voncount on 4/13/16.
 */
@Service
@Transactional
public class ServiceUser extends BaseService implements IServiceUser {

    @Autowired
    private IServiceFile serviceFile;

    @Override
    public CustomUserDetail authenticateUser(FormLogin form) {
        String username = form.getUsername();
        String password = form.getPassword();
        String rememberme = form.getRememberme();

        // find User
        User user = repositoryUser.findByEmail(username);
        // get Permissions
        List<CustomAuthority> grantedAuthorities = new ArrayList<>();
        for (UserPermission permission : user.getRole().getPermissions()) {
            CustomAuthority authority = new CustomAuthority(permission.getName());
            grantedAuthorities.add(authority);
        }
        grantedAuthorities.add(new CustomAuthority(user.getRole().getName()));

        CustomUserDetail userDetail = new CustomUserDetail();
        userDetail.setUserId(user.getId().toString());
        userDetail.setUsername(username);
        userDetail.setPassword(password);
        userDetail.setAuthorities(grantedAuthorities);
        userDetail.setAccountNonExpired(true);
        userDetail.setAccountNonLocked(true);
        userDetail.setEnabled(true);
        userDetail.setCredentialsNonExpired(true);
        userDetail.setType(user.getType().name());
        if (rememberme != null && !rememberme.trim().isEmpty() && rememberme.equals("on")) {
            userDetail.setExpiry(-1L);
        }
        return userDetail;
    }

    @Override
    public User create(FormUser form) {

        String type = form.getType();
        String role = form.getRole();
        String status = form.getStatus();


        UserType userType = UserType.valueOf(type);
        UserRole userRole = repositoryUserRole.findByName(role);
        CommonStatus userStatus= CommonStatus.valueOf(status);

        User user = new User();
        BeanUtils.copyProperties(form, user);

        user.setRole(userRole);
        user.setType(userType);
        user.setStatus(userStatus);

        user = repositoryUser.save(user);

        switch (userType) {
            case TRUSTED_BROKER:
                break;
            case BROKER: {
                String avatar = form.getBase64image();
                // insert avatar
                if (!Strings.isNullOrEmpty(avatar)) {
                    try {
                        byte[] imageBytes = javax.xml.bind.DatatypeConverter.parseBase64Binary(avatar);
                        String fileRelativePath[], fileUrl, thumbUrl;
                        fileRelativePath = FileUtils.uploadImage(new ByteArrayInputStream(imageBytes), configParams.UPLOAD_DIRECTORY, "/user/");
                        fileUrl = configParams.BASE_URL + "/public" + fileRelativePath[0];
                        thumbUrl = configParams.BASE_URL + "/public" + fileRelativePath[1];

                        Attachment attachment = new Attachment();
                        attachment.setOrigin(fileUrl);
                        attachment.setThumbnail(thumbUrl);
                        attachment.setTableRef("user");
                        attachment.setRowRef(user.getId());

                        attachment = repositoryAttachment.save(attachment);
                        user.setAvatar(attachment);
                    } catch (Exception e) {
                        e.printStackTrace();
                        throw new ApplicationException(MessageResponse.EXCEPTION_FILEUPLOAD_FAILED);
                    }
                }
                break;
            }
            case MEMBER:
                break;
            default:
                throw new ApplicationException(MessageResponse.EXCEPTION_BAD_REQUEST);
        }
        return repositoryUser.save(user);
    }

    @Override
    public User update(FormUser form) {
        String userId = form.getUserId();
        String username = form.getUsername();
        String cellphoneNumber = form.getCellphoneNumber();
        String type = form.getType();
        String companyName = form.getCompanyName();
        // Validate userId
        User existedUser = repositoryUser.findOne(Long.parseLong(userId));
        if (existedUser == null) {
            throw new ApplicationException(MessageResponse.EXCEPTION_NOT_FOUND);
        }
        // Update username
        if (!Strings.isNullOrEmpty(username)) {
            existedUser.setUsername(username);
        }
        // Update username
        if (!Strings.isNullOrEmpty(companyName)) {
            existedUser.setCompanyName(companyName);
        }
        // Update cellphone
        if (!Strings.isNullOrEmpty(cellphoneNumber)) {
            existedUser.setCellphoneNumber(cellphoneNumber);
        }
        // Update type
        if (!Strings.isNullOrEmpty(type)) {
            Optional<UserType> _userType = ConvertUtils.toEnum(type, UserType.class);
            if (_userType.isPresent()) {
                existedUser.setType(_userType.get());
            }
        }
        return repositoryUser.save(existedUser);
    }

    @Override
    public User withdraw(FormWithdraw form) {
        String userId = form.getUserId();
        String content = form.getContent();

        // Validate userId
        User existedUser = repositoryUser.findOne(Long.parseLong(userId));

        // save new withdrawal
        Withdrawal withdrawal = new Withdrawal();
        withdrawal.setUser(existedUser);
        withdrawal.setReason(content);
        repositoryWithdrawal.save(withdrawal);

        // change user status to INACTIVE
        existedUser.setStatus(CommonStatus.INACTIVE);

        return existedUser;
    }

    @Override
    public Page<User> filter(FormUser form) {
        return repositoryUser.findAll(form.getSpecification(), form.getPaging());
    }

    @Override
    public void delete(FormUser form) {
        User user = repositoryUser.findOne(form.getSpecification());
        user.setIsDelete(1);
    }

    @Override
    public User findOne(FormUser form) {
        User user = repositoryUser.findOne(form.getSpecification());
        return user;
    }

    @Override
    public User findByUserId(int userId) {
        return repositoryUser.findByUserId(userId);
    }

    @Override
    public User resetPassword(FormPassword form) {
        String userId = form.getUserId();
        String password = form.getPassword();
        String passwordNew = form.getPasswordNew();
        String passwordConfirm = form.getPasswordConfirm();

        User existedUser = repositoryUser.findOne(Long.parseLong(userId));
        // Update password
        if (serviceAuthorized.isAdmin()) {
            if (passwordNew != null && !passwordNew.trim().isEmpty()
                    && passwordConfirm != null && !passwordConfirm.trim().isEmpty()
                    && passwordNew.equals(passwordConfirm)) {
                existedUser.setPassword(passwordEncoder.encode(passwordNew));
            }
        } else {
            if (!passwordEncoder.matches(password, existedUser.getPassword())) {
                throw new ApplicationException(MessageResponse.EXCEPTION_FIELD_INVALID);
            }
            if (passwordNew != null && !passwordNew.trim().isEmpty()
                    && passwordConfirm != null && !passwordConfirm.trim().isEmpty()
                    && passwordNew.equals(passwordConfirm)) {
                existedUser.setPassword(passwordEncoder.encode(passwordNew));
            }
        }
        return repositoryUser.save(existedUser);
    }

    @Override
    public List<User> filterBy(String type) {
        List<User> users = new ArrayList<>();
        switch (type){
            case "1": { // mean sign up today
                System.out.println("Sign up today");
                Date date = new Date();
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                String[] dateFm = format.format(date).split("-");
                int year = Integer.parseInt(dateFm[0]);
                int month = Integer.parseInt(dateFm[1]);
                int day = Integer.parseInt(dateFm[2]);
                users = repositoryUser.findBySignUpToDay(day,month,year);
                System.out.println("size: "+users.size());
                break;
            }
            case "2": // mean sign up last month
            {
                System.out.println("Sign up lastmonth");
                Date date = new Date();
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM");
                String[] dateFm = format.format(date).split("-");
                int year = Integer.parseInt(dateFm[0]);
                int month = Integer.parseInt(dateFm[1]);
                users = repositoryUser.findBySignUpLastMonth(month,year);
                System.out.println("size: "+users.size());
                break;
            }
            case "3": // mean regular memeber
            {

                System.out.println("Sign up regular member");
                break;
            }
            case "4": // mean broker
            {

                System.out.println("Sign up broker");
                break;
            }
            case "5": // mean approved broker
            {
                System.out.println("Sign up approved broker");
                break;
            }
            case "6": //mean all member
            {
                System.out.println("Sign up all member");
                users = repositoryUser.findAll();
                break;
            }
        }

        return users;
    }

    @Override
    public User findUserByEmail(String email) {
        User user = repositoryUser.findByEmail(email);
        return user;
    }
}
