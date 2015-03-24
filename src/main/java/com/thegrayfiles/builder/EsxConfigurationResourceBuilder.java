package com.thegrayfiles.builder;

import com.thegrayfiles.resource.EsxConfigurationResource;
import org.apache.commons.lang3.builder.Builder;

public class EsxConfigurationResourceBuilder implements Builder<EsxConfigurationResource> {

    private String ip = "1.2.3.4";
    private String password = "password";
    private String version = "5.5";

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

    public EsxConfigurationResourceBuilder withVersion(String version) {
        this.version = version;
        return this;
    }
}
