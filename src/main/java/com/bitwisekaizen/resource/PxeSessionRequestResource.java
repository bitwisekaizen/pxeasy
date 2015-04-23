package com.bitwisekaizen.resource;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class PxeSessionRequestResource {

    private String macAddress;
    private EsxConfigurationResource config;

    @JsonCreator
    public PxeSessionRequestResource(@JsonProperty("macAddress") String macAddress, @JsonProperty("esxConfig") EsxConfigurationResource config) {
        this.macAddress = macAddress;
        this.config = config;
    }

    public String getMacAddress() {
        return macAddress;
    }

    public EsxConfigurationResource getConfig() {
        return config;
    }
}
