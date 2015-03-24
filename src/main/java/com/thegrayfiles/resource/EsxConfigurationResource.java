package com.thegrayfiles.resource;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class EsxConfigurationResource {

    private String ip;
    private String password;
    private String version;

    @JsonCreator
    public EsxConfigurationResource() {

    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    @JsonProperty("ip")
    public String getIp() {
        return ip;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @JsonProperty("password")
    public String getPassword() {
        return password;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    @JsonProperty("version")
    public String getVersion() {
        return version;
    }
}
