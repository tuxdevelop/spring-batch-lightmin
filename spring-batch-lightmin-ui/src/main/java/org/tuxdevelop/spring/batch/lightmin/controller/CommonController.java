package org.tuxdevelop.spring.batch.lightmin.controller;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Date;

public abstract class CommonController {

    @ExceptionHandler(Exception.class)
    public ModelAndView handleAllExceptions(final HttpServletRequest request, Exception
            ex) {
        final ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("exception", ex);
        modelAndView.addObject("stackTrace", Arrays.toString(ex.getStackTrace()));
        modelAndView.addObject("url", request.getRequestURL());
        modelAndView.addObject("timestamp", new Date());
        modelAndView.addObject("status", HttpStatus.INTERNAL_SERVER_ERROR.value() + " " + HttpStatus
                .INTERNAL_SERVER_ERROR.name());
        modelAndView.setViewName("error");
        return modelAndView;
    }

}
