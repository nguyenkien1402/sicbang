package org.trams.sicbang.model.exception;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by voncount on 22/04/2016.
 */
public class FormError {

    private Map<String, String> errors = new HashMap<>();

    public Map<String, String> getErrors() {
        return errors;
    }

    public void setErrors(Map<String, String> errors) {
        this.errors = errors;
    }

    public void rejectValue(String field, String msg) {
        errors.put(field, msg);
    }

    public boolean hasErrors() {
        return !errors.isEmpty();
    }


}
