package org.tuxdevelop.spring.batch.lightmin.server.support;

import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.tuxdevelop.spring.batch.lightmin.client.api.LightminClientApplication;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * @author Marcel Becker
 * @since 0.3
 */
public final class LightminApplicationValidator {

    private LightminApplicationValidator() {
    }

    public static void validate(final LightminClientApplication lightminClientApplication) {
        Assert.notNull(lightminClientApplication, "LightminClientApplication must not be null");
        Assert.hasText(lightminClientApplication.getName(), "Name must not be null");
        Assert.hasText(lightminClientApplication.getHealthUrl(), "Health-URL must not be null");
        Assert.isTrue(validateUrl(lightminClientApplication.getHealthUrl()), "Health-URL is not valid");
        Assert.isTrue(
                StringUtils.isEmpty(lightminClientApplication.getManagementUrl())
                        || validateUrl(lightminClientApplication.getManagementUrl()), "URL is not valid");
        Assert.isTrue(
                StringUtils.isEmpty(lightminClientApplication.getServiceUrl())
                        || validateUrl(lightminClientApplication.getServiceUrl()), "URL is not valid");
    }

    public static void checkApplicationId(final String applicationId) {
        Assert.notNull(applicationId, "Id must not be null");
    }

    private static Boolean validateUrl(final String url) {
        try {
            new URL(url);
        } catch (final MalformedURLException e) {
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }
}
