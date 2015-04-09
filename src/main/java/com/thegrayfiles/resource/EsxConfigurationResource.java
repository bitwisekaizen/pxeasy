package com.thegrayfiles.resource;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class EsxConfigurationResource {

    private String ip;
    private String password;
    private String version;
    private String gateway;
    private String netmask;
    private String hostname;

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

    public void setGateway(String gateway) {
        this.gateway = gateway;
    }

    public void setNetmask(String netmask) {
        this.netmask = netmask;
    }

    public String getNetmask() {
        return netmask;
    }

    public String getGateway() {
        return gateway;
    }

    public String getHostname() {
        return hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }
}
