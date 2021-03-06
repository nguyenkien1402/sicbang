package org.trams.sicbang.model.enumerate;

/**
 * Created by voncount on 4/6/16.
 */
public enum MessageResponse {

    // DONE
    OK(0, "OK"),

    // EXCEPTION
    EXCEPTION_FIELD_INVALID(1, "Field not found or invalid"),
    EXCEPTION_NOT_FOUND(2, "Resource not found"),
    EXCEPTION_NOT_AVAILABLE(3, "Resource not available"),
    EXCEPTION_EXISTED(4, "Resource existed"),

    EXCEPTION_FILEUPLOAD_FAILED(500, "Failed to upload file"),

    EXCEPTION_UNAUTHORIZED(996, "Unauthorized request"),
    EXCEPTION_BAD_REQUEST(997, "Request params missing or invalid"),
    EXCEPTION_PARSE_JSON(998, "Cannot parse json"),
    EXCEPTION_UNHANDLE(999, "Unhandled exception"),
    EXCEPTION_PASSWORD_NOT_CONFIRM(5, "Password and Password confirmation do not match");

    private int code;
    private String message;

    MessageResponse(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

}
