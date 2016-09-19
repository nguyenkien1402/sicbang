package org.trams.sicbang.validation;

import com.google.common.base.Strings;
import org.springframework.stereotype.Component;
import org.trams.sicbang.model.enumerate.MessageResponse;
import org.trams.sicbang.model.exception.FormError;
import org.trams.sicbang.model.form.FormMail;

import java.util.List;

/**
 * Created by voncount on 4/27/16.
 */
@Component
public class ValidationEmail {

    public FormError validateCreate(FormMail form) {
        FormError error = new FormError();

        String title = form.getMailSubject();
//        String recipient = form.getMailTo();
        if(form.getEmailsTo().size() >0){
            String recipient = "";
            String content = form.getMailContent();
            String type = form.getType();

            for(int i = 0 ; i < form.getEmailsTo().size() ; i++) {
                recipient = form.getEmailsTo().get(i);
                if (Strings.isNullOrEmpty(title)) {
                    error.rejectValue("title", MessageResponse.EXCEPTION_FIELD_INVALID.getMessage());
                }
                if (Strings.isNullOrEmpty(type)) {
                    if (Strings.isNullOrEmpty(recipient)) {
                        error.rejectValue("recipient", MessageResponse.EXCEPTION_FIELD_INVALID.getMessage());
                    }
                }
                if (Strings.isNullOrEmpty(content)) {
                    error.rejectValue("content", MessageResponse.EXCEPTION_FIELD_INVALID.getMessage());
                }
            }

        }else{
            error.rejectValue("null","Not found any email to send");
        }
        return error.hasErrors() ? error : null;
    }

}
