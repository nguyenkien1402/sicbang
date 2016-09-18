package org.trams.sicbang.api.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import org.trams.sicbang.model.dto.Response;
import org.trams.sicbang.model.enumerate.MessageResponse;
import org.trams.sicbang.model.exception.ApplicationException;

/**
 * Created by voncount on 4/8/16.
 */
@ControllerAdvice(annotations = RestController.class)
public class RestExceptionHandler extends AbstractController {

    /**
     * Handle exceptions thrown by handlers.
     */
    @ExceptionHandler(value = ApplicationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public Response userException(ApplicationException e, WebRequest request) {
        logger.error(e.getMessage(), e);
        return new Response(e.getMessageResponse());
    }

    /**
     * Handle exceptions thrown by handlers.
     */
    @ExceptionHandler(value = Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public Response exception(Exception e, WebRequest request) {
        logger.error(e.getMessage(), e);
        return new Response(MessageResponse.EXCEPTION_UNHANDLE);
    }

}
