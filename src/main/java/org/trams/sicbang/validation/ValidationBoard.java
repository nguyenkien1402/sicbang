package org.trams.sicbang.validation;

import com.google.common.base.Strings;
import org.springframework.stereotype.Component;
import org.trams.sicbang.model.enumerate.MessageResponse;
import org.trams.sicbang.model.exception.FormError;
import org.trams.sicbang.model.form.FormBoard;

/**
 * Created by voncount on 25/04/2016.
 */
@Component
public class ValidationBoard {

    public FormError validateCreate(FormBoard form) {

        FormError error = new FormError();

        String title = form.getTitle();
        String content = form.getContent();

        if (Strings.isNullOrEmpty(title)) {
            error.rejectValue("title", MessageResponse.EXCEPTION_FIELD_INVALID.getMessage());
        }
        if (Strings.isNullOrEmpty(content)) {
            error.rejectValue("content", MessageResponse.EXCEPTION_FIELD_INVALID.getMessage());
        }

        return error.hasErrors() ? error : null;

    }

}
