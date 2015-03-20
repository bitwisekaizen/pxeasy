package com.thegrayfiles.resource;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class PxeSessionRequestResource {

    private String macAddress;
    private String ip;

    @JsonCreator
    public PxeSessionRequestResource(@JsonProperty("macAddress") String macAddress, @JsonProperty("ip") String ip) {
        this.macAddress = macAddress;
        this.ip = ip;
    }

    public String getMacAddress() {
        return macAddress;
    }

    public String getIp() {
        return ip;
    }
}
