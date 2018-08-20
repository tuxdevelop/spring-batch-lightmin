package org.tuxdevelop.spring.batch.lightmin.server.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;
import org.tuxdevelop.spring.batch.lightmin.server.configuration.LightminServerProperties;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Date;

/**
 * @author Marcel Becker
 * @since 0.1
 */
@Slf4j
public abstract class CommonController implements ApplicationContextAware {

    private static final String SLASH = "/";

    private Boolean useXForwardedHeaders;

    @ExceptionHandler(Exception.class)
    public ModelAndView handleAllExceptions(final HttpServletRequest request, final Exception
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


    protected RedirectView createRedirectView(final String path, final HttpServletRequest request) {
        final String completePath = this.createPath(path, request);
        final RedirectView redirectView = new RedirectView(completePath);
        if (this.useXForwardedHeaders) {
            this.prepareRedirectViewForXForwadedFor(redirectView, request);
        } else {
            log.debug("Using HOST header");
        }
        return redirectView;
    }

    private String createPath(final String path, final HttpServletRequest request) {
        final String prefix = this.getXForwadedPrefix(request);
        return this.buildFinalRedirectPath(path, prefix);
    }

    private String buildFinalRedirectPath(final String path, final String prefix) {
        final String result;
        if (StringUtils.hasText(path)) {
            if (SLASH.endsWith(path)) {
                if (StringUtils.hasText(prefix)) {
                    result = prefix;
                } else {
                    result = path;
                }
            } else if (path.startsWith(SLASH) && prefix.endsWith(SLASH)) {
                final String relativePath = path.replaceFirst(SLASH, "");
                result = prefix + relativePath;
            } else {
                result = prefix + path;
            }
        } else {
            result = prefix;
        }

        return result;
    }

    private void prepareRedirectViewForXForwadedFor(final RedirectView redirectView,
                                                    final HttpServletRequest request) {
        final String host = request.getHeader("X-FORWARDED-HOST");
        if (StringUtils.hasText(host)) {
            redirectView.setHosts(host);
        } else {
            log.debug("skipping setting X-FORWARED-HOST header");
        }
    }

    private String getXForwadedPrefix(final HttpServletRequest request) {
        final String prefix = request.getHeader("X-FORWARDED-PREFIX");
        final String result;
        if (StringUtils.hasText(prefix)) {
            if (prefix.endsWith(SLASH)) {
                result = prefix;
            } else {
                result = prefix + SLASH;
            }
        } else {
            result = "";
        }
        return result;
    }

    @Override
    public void setApplicationContext(final ApplicationContext applicationContext) throws BeansException {
        final LightminServerProperties lightminServerProperties =
                applicationContext.getBean(LightminServerProperties.class);
        this.useXForwardedHeaders = lightminServerProperties.getUseXForwardedHeaders();
    }

}
