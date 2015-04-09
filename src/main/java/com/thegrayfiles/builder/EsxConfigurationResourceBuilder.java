package com.thegrayfiles.builder;

import com.thegrayfiles.resource.EsxConfigurationResource;
import org.apache.commons.lang3.builder.Builder;

public class EsxConfigurationResourceBuilder implements Builder<EsxConfigurationResource> {

    private String ip = "1.2.3.4";
    private String password = "password";
    private String version = "5.5";
    private String netmask = "255.255.248.0";
    private String gateway = "10.100.200.1";
    private String hostname = "localhost";

    private EsxConfigurationResourceBuilder() {

    }

    public static EsxConfigurationResourceBuilder anEsxConfiguration() {
        return new EsxConfigurationResourceBuilder();
    }

    @Override
    public EsxConfigurationResource build() {
        EsxConfigurationResource config = new EsxConfigurationResource();
        config.setIp(ip);
        config.setPassword(password);
        config.setVersion(version);
        config.setGateway(gateway);
        config.setNetmask(netmask);
        config.setHostname(hostname);
        return config;
    }

    public EsxConfigurationResourceBuilder withIp(String ip) {
        this.ip = ip;
        return this;
    }

    public EsxConfigurationResourceBuilder withPassword(String password) {
        this.password = password;
        return this;
    }

    public EsxConfigurationResourceBuilder withNetmask(String netmask) {
        this.netmask = netmask;
        return this;
    }

    public EsxConfigurationResourceBuilder withGateway(String gateway) {
        this.gateway = gateway;
        return this;
    }

    public EsxConfigurationResourceBuilder withVersion(String version) {
        this.version = version;
        return this;
    }

    public EsxConfigurationResourceBuilder withHostname(String hostname) {
        this.hostname = hostname;
        return this;
    }
}
