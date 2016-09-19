package org.trams.sicbang.validation;

import com.google.common.base.Strings;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.trams.sicbang.model.entity.User;
import org.trams.sicbang.model.entity.UserRole;
import org.trams.sicbang.model.enumerate.CommonStatus;
import org.trams.sicbang.model.enumerate.MessageResponse;
import org.trams.sicbang.model.enumerate.UserType;
import org.trams.sicbang.model.exception.FormError;
import org.trams.sicbang.model.form.*;
import org.trams.sicbang.repository.RepositoryUser;
import org.trams.sicbang.repository.RepositoryUserRole;

/**
 * Created by voncount on 22/04/2016.
 */
@Component
public class ValidationUser {

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    @Autowired
    private RepositoryUser repositoryUser;
    @Autowired
    private RepositoryUserRole repositoryUserRole;

    public FormError validateLogin(FormLogin form) {
        FormError error = new FormError();

        String username = form.getUsername();
        String password = form.getPassword();

        // check empty, null params
        if (Strings.isNullOrEmpty(username)) {
            error.rejectValue("username", MessageResponse.EXCEPTION_FIELD_INVALID.getMessage());
        }
        if (Strings.isNullOrEmpty(password)) {
            error.rejectValue("password", MessageResponse.EXCEPTION_FIELD_INVALID.getMessage());
        }
        // find User
        User user = repositoryUser.findByEmail(username);
        if (user == null) {
            error.rejectValue("username", MessageResponse.EXCEPTION_NOT_FOUND.getMessage());
        } else {
            if (!passwordEncoder.matches(password, user.getPassword())) {
                error.rejectValue("password", MessageResponse.EXCEPTION_FIELD_INVALID.getMessage());
            }
            // check status
            if (!user.getStatus().equals(CommonStatus.ACTIVE)) {
                error.rejectValue("username", MessageResponse.EXCEPTION_NOT_AVAILABLE.getMessage());
            }
            if (user.getIsDelete() == 1) {
                error.rejectValue("username", MessageResponse.EXCEPTION_NOT_AVAILABLE.getMessage());
            }
        }

        return error.hasErrors() ? error : null;
    }

    public FormError validateResetPassword(FormPassword form) {
        FormError error = new FormError();

        String userId = form.getUserId();
        String passwordNew = form.getPasswordNew();
        String passwordConfirm = form.getPasswordConfirm();

        User existedUser = repositoryUser.findOne(Long.parseLong(userId));
        if (existedUser == null) {
            error.rejectValue("password", MessageResponse.EXCEPTION_NOT_FOUND.getMessage());
        }
        if (Strings.isNullOrEmpty(passwordNew)) {
            error.rejectValue("passwordNew", MessageResponse.EXCEPTION_FIELD_INVALID.getMessage());
        }
        if (Strings.isNullOrEmpty(passwordConfirm)) {
            error.rejectValue("passwordInvalid", MessageResponse.EXCEPTION_FIELD_INVALID.getMessage());
            return error.hasErrors() ? error : null;
        }
        if (passwordNew != null && passwordConfirm != null && !passwordNew.equals(passwordConfirm)) {
            error.rejectValue("passwordConfirm", MessageResponse.EXCEPTION_PASSWORD_NOT_CONFIRM.getMessage());
            return error.hasErrors() ? error : null;
        }

        return error.hasErrors() ? error : null;
    }

    public FormError validateUpdateSettings(FormUser form) {
        FormError error = new FormError();

        return error.hasErrors() ? error : null;
    }

    public FormError validateCreate(FormUser form) {
        FormError error = new FormError();

        String email = form.getEmail();
        String password = form.getPassword();
        String passwordConfirm = form.getPasswordConfirm();
        String type = form.getType();
        String role = form.getRole();
        String status = form.getStatus();
        String avatar = form.getBase64image();

        if (Strings.isNullOrEmpty(email) || !EmailValidator.getInstance().isValid(email)) {
            error.rejectValue("email", MessageResponse.EXCEPTION_FIELD_INVALID.getMessage());
        }
        User existedUser = repositoryUser.findByEmail(email);
        if (existedUser != null) {
            error.rejectValue("email", MessageResponse.EXCEPTION_EXISTED.getMessage());
        }
        if (Strings.isNullOrEmpty(password)) {
            error.rejectValue("password", MessageResponse.EXCEPTION_FIELD_INVALID.getMessage());
        }
        if (Strings.isNullOrEmpty(passwordConfirm)) {
            error.rejectValue("passwordConfirm", MessageResponse.EXCEPTION_FIELD_INVALID.getMessage());
        }
        if (password != null && passwordConfirm != null && !password.equals(passwordConfirm)) {
            error.rejectValue("passwordConfirm", MessageResponse.EXCEPTION_FIELD_INVALID.getMessage());
        }
        if (Strings.isNullOrEmpty(type)) {
            error.rejectValue("type", MessageResponse.EXCEPTION_FIELD_INVALID.getMessage());
        }
        if (Strings.isNullOrEmpty(role)) {
            error.rejectValue("role", MessageResponse.EXCEPTION_FIELD_INVALID.getMessage());
        }
        if (Strings.isNullOrEmpty(status)) {
            error.rejectValue("status", MessageResponse.EXCEPTION_FIELD_INVALID.getMessage());
        }

        try {
            UserType _type = UserType.valueOf(type);
            if (_type.equals(UserType.BROKER)) {
                if (Strings.isNullOrEmpty(avatar)) {
                    error.rejectValue("base64image", MessageResponse.EXCEPTION_FIELD_INVALID.getMessage());
                }
            }
        } catch (Exception e) {
            error.rejectValue("type", MessageResponse.EXCEPTION_FIELD_INVALID.getMessage());
        }
        UserRole userRole = repositoryUserRole.findByName(role);
        if (userRole == null) {
            error.rejectValue("role", MessageResponse.EXCEPTION_FIELD_INVALID.getMessage());
        }
        try {
            CommonStatus.valueOf(status);
        } catch (Exception e) {
            error.rejectValue("status", MessageResponse.EXCEPTION_FIELD_INVALID.getMessage());
        }

        return error.hasErrors() ? error : null;
    }

    public FormError validateWithdraw(FormWithdraw form) {
        FormError error = new FormError();

        String userId = form.getUserId();
        String password = form.getPassword();
        String content = form.getContent();

        // validate fields
        if (Strings.isNullOrEmpty(password)) {
            error.rejectValue("password", MessageResponse.EXCEPTION_FIELD_INVALID.getMessage());
        }
        if (Strings.isNullOrEmpty(content)) {
            error.rejectValue("content", MessageResponse.EXCEPTION_FIELD_INVALID.getMessage());
        }
        // Validate userId
        User existedUser = repositoryUser.findOne(Long.parseLong(userId));
        if (existedUser == null) {
            error.rejectValue("password", MessageResponse.EXCEPTION_NOT_FOUND.getMessage());
        }
        else if (!passwordEncoder.matches(password, existedUser.getPassword())) {
            error.rejectValue("password", MessageResponse.EXCEPTION_FIELD_INVALID.getMessage());
        }

        return error.hasErrors() ? error : null;
    }

    public FormError validateAsk(FormAsk form) {

        FormError error = new FormError();

        String title = form.getTitle();
        String content = form.getContent();
        String name = form.getName();
        String contact = form.getContact();

        if (Strings.isNullOrEmpty(title)) {
            error.rejectValue("title", MessageResponse.EXCEPTION_FIELD_INVALID.getMessage());
        }
        if (Strings.isNullOrEmpty(content)) {
            error.rejectValue("content", MessageResponse.EXCEPTION_FIELD_INVALID.getMessage());
        }
        if (Strings.isNullOrEmpty(name)) {
            error.rejectValue("name", MessageResponse.EXCEPTION_FIELD_INVALID.getMessage());
        }
        if (Strings.isNullOrEmpty(contact)) {
            error.rejectValue("contact", MessageResponse.EXCEPTION_FIELD_INVALID.getMessage());
        }

        return error.hasErrors() ? error : null;
    }

}
