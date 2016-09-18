package org.trams.sicbang.model.form;

import io.swagger.annotations.ApiModelProperty;

/**
 * Created by voncount on 25/04/2016.
 */
public class FormPassword {

    @ApiModelProperty(hidden = true)
    private String userId;

    private String password;
    private String passwordNew;
    private String passwordConfirm;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPasswordNew() {
        return passwordNew;
    }

    public void setPasswordNew(String passwordNew) {
        this.passwordNew = passwordNew;
    }

    public String getPasswordConfirm() {
        return passwordConfirm;
    }

    public void setPasswordConfirm(String passwordConfirm) {
        this.passwordConfirm = passwordConfirm;
    }
}
