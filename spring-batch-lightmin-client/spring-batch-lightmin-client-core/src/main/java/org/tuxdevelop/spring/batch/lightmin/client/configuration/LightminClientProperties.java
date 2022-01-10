package org.tuxdevelop.spring.batch.lightmin.client.configuration;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.autoconfigure.endpoint.web.WebEndpointProperties;
import org.springframework.boot.actuate.autoconfigure.web.server.ManagementServerProperties;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.core.env.Environment;
import org.springframework.util.StringUtils;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

@ConfigurationProperties(prefix = "spring.batch.lightmin.client")
public class LightminClientProperties {

    @Setter
    private String managementUrl;
    @Setter
    private String serviceUrl;
    @Setter
    private String healthUrl;
    @Getter
    @Setter
    private boolean preferIp = false;
    @Getter
    @Setter
    private Integer serverPort;
    @Getter
    @Setter
    private Integer managementPort;
    @Getter
    @Setter
    private Boolean metricsEnabled = Boolean.TRUE;
    @Getter
    @Setter
    private Boolean publishJobEvents = Boolean.TRUE;
    @Getter
    @Setter
    private Map<String, String> externalLinks = new HashMap<>();
    @Getter
    @Setter
    private String hostname;
    @Getter
    private final String name;
    @Getter
    private final String healthEndpointId;
    @Getter
    @Setter
    @NestedConfigurationProperty
    private ClientServerProperties server = new ClientServerProperties();

    private final ManagementServerProperties managementServerProperties;
    private final ServerProperties serverProperties;
    private final WebEndpointProperties webEndpointProperties;

    @Autowired
    public LightminClientProperties(final ManagementServerProperties managementServerProperties,
                                    final ServerProperties serverProperties,
                                    @Value("${spring.batch.lightmin.application-name:null}") final String name,
                                    @Value("${endpoints.health.id:health}") final String healthEndpointId,
                                    final WebEndpointProperties webEndpointProperties,
                                    final Environment environment) {
        if (name == null || "null".equals(name)) {
            this.name = environment.getProperty("spring.application.name", "spring-boot-application");
        } else {
            this.name = name;
        }
        this.healthEndpointId = healthEndpointId;
        this.managementServerProperties = managementServerProperties;
        this.serverProperties = serverProperties;
        this.webEndpointProperties = webEndpointProperties;
    }

    public String getManagementUrl() {

        final String resultManagementUrl;

        if (this.managementUrl != null) {
            resultManagementUrl = this.managementUrl;
        } else {
            if ((this.managementPort == null || this.managementPort.equals(this.serverPort))
                    && this.getServiceUrl() != null) {
                resultManagementUrl =
                        this.append(
                                this.append(this.getServiceUrl(),
                                        this.managementServerProperties.getBasePath()),
                                this.webEndpointProperties.getBasePath());
            } else {
                if (this.managementPort == null) {
                    throw new IllegalStateException(
                            "serviceUrl must be set when deployed to servlet-container");
                } else {
                    if (this.preferIp) {
                        final InetAddress address = this.serverProperties.getAddress();
                        final String hostAddress = this.getHostAddress(address);
                        resultManagementUrl = this.append(
                                this.append(this.createLocalUri(hostAddress, this.managementPort),
                                        this.managementServerProperties.getBasePath()),
                                this.webEndpointProperties.getBasePath());

                    } else {
                        resultManagementUrl = this.append(
                                this.append(this.createLocalUri(this.determineHost(), this.managementPort),
                                        this.managementServerProperties.getBasePath()),
                                this.webEndpointProperties.getBasePath());
                    }
                }
            }
        }
        return resultManagementUrl;
    }

    private String getHostAddress(final InetAddress address) {
        final String hostAddress;
        if (address == null) {
            hostAddress = this.determineHost();
        } else {
            hostAddress = address.getHostAddress();
        }
        return hostAddress;
    }

    public String getHealthUrl() {
        if (this.healthUrl != null) {
            return this.healthUrl;
        }
        final String managementUrl = this.getManagementUrl();
        return this.append(managementUrl, this.healthEndpointId);
    }

    public String getServiceUrl() {
        final String resultServiceUrl;
        if (this.serviceUrl != null) {
            resultServiceUrl = this.serviceUrl;
        } else {
            if (this.serverPort == null) {
                throw new IllegalStateException(
                        "serviceUrl must be set when deployed to servlet-container");
            } else {
                if (this.preferIp) {
                    final InetAddress address = this.serverProperties.getAddress();
                    final String hostAddress = this.getHostAddress(address);
                    resultServiceUrl =
                            this.append(this.createLocalUri(hostAddress, this.serverPort),
                                    this.serverProperties.getServlet().getContextPath());

                } else {
                    resultServiceUrl = this.append(this.createLocalUri(this.determineHost(), this.serverPort),
                            this.serverProperties.getServlet().getContextPath());
                }
            }
        }
        return resultServiceUrl;
    }

    private String createLocalUri(final String host, final int port) {
        final String scheme =
                this.serverProperties.getSsl() != null && this.serverProperties.getSsl().isEnabled() ? "https" : "http";
        return scheme + "://" + host + ":" + port;
    }

    private String append(final String uri, final String path) {
        final String baseUri = uri.replaceFirst("/+$", "");
        final String resultUri;
        if (StringUtils.isEmpty(path)) {
            resultUri = baseUri;
        } else {

            final String normPath = path.replaceFirst("^/+", "").replaceFirst("/+$", "");
            resultUri = baseUri + "/" + normPath;
        }
        return resultUri;
    }

    private InetAddress getHostAddress() {
        try {
            return InetAddress.getLocalHost();
        } catch (final UnknownHostException ex) {
            throw new IllegalArgumentException(ex.getMessage(), ex);
        }
    }

    private String determineHost() {
        final String host;
        if (StringUtils.hasText(this.hostname)) {
            host = this.hostname;
        } else {
            final InetAddress inetAddress = this.getHostAddress();
            host = inetAddress.getCanonicalHostName();
        }
        return host;
    }

    @Data
    public static class ClientServerProperties {
        private String username;
        private String password;
    }

}
