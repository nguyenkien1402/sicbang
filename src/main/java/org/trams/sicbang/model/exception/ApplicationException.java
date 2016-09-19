package org.trams.sicbang.model.exception;

import org.trams.sicbang.model.enumerate.MessageResponse;

/**
 * Created by voncount on 4/6/16.
 */
public class ApplicationException extends RuntimeException {

    private MessageResponse messageResponse;

    public ApplicationException(MessageResponse messageResponse) {
        this.messageResponse = messageResponse;
    }

    public ApplicationException(String message, MessageResponse messageResponse) {
        super(message);
        this.messageResponse = messageResponse;
    }

    public ApplicationException(String message, Throwable cause, MessageResponse messageResponse) {
        super(message, cause);
        this.messageResponse = messageResponse;
    }

    public ApplicationException(Throwable cause, MessageResponse messageResponse) {
        super(cause);
        this.messageResponse = messageResponse;
    }

    public ApplicationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, MessageResponse messageResponse) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.messageResponse = messageResponse;
    }

    public MessageResponse getMessageResponse() {
        return messageResponse;
    }

    @Override
    public String getMessage() {
        return this.messageResponse.getMessage();
    }
}
