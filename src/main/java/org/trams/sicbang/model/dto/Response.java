package org.trams.sicbang.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.trams.sicbang.model.enumerate.MessageResponse;

/**
 * Created by voncount on 4/6/16.
 */
public class Response<T> {

    private int status;
    private String message;
    @JsonIgnore
    private MessageResponse messageResponse;
    private T data;

    public Response(MessageResponse messageResponse) {
        this.status = messageResponse.getCode();
        this.message = messageResponse.getMessage();
        this.messageResponse = messageResponse;
    }

    public Response(MessageResponse messageResponse, T data) {
        this.status = messageResponse.getCode();
        this.message = messageResponse.getMessage();
        this.messageResponse = messageResponse;
        this.data = data;
    }

    public int getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public MessageResponse getMessageResponse() {
        return messageResponse;
    }

    public T getData() {
        return data;
    }

}

