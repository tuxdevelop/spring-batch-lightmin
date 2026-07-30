package org.tuxdevelop.spring.batch.lightmin.server.fe.controller;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Date;

@Slf4j
@ControllerAdvice
public class GlobalExceptionControllerAdvice {

    @ExceptionHandler(Exception.class)
    public ModelAndView handleError(final HttpServletRequest request, final Exception exception)
            throws Exception {


        log.error("Request: {}" + " raised {} ", request.getRequestURL(), exception);

        final ExceptionModel exceptionModel = new ExceptionModel();
        exceptionModel.setException(exception);
        exceptionModel.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        exceptionModel.setTimestamp(new Date().toString());
        exceptionModel.setUrl(request.getRequestURL().toString());

        final ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("exceptionModel", exceptionModel);

        modelAndView.setViewName("error");
        return modelAndView;
    }

    @Data
    private static class ExceptionModel {

        private Exception exception;
        private String url;
        private String timestamp;
        private Integer status;
    }

}
