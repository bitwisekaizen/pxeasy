package com.thegrayfiles.resource;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class PxeSessionRequestResource {

    private String macAddress;
    private String ip;
    private String password;

    @JsonCreator
    public PxeSessionRequestResource(@JsonProperty("macAddress") String macAddress, @JsonProperty("ip") String ip, @JsonProperty("password") String password) {
        this.macAddress = macAddress;
        this.ip = ip;
        this.password = password;
    }

    public String getMacAddress() {
        return macAddress;
    }

    public String getIp() {
        return ip;
    }

    public String getPassword() {
        return password;
    }
}
