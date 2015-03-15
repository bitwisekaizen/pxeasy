package com.thegrayfiles.resource;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class PxeSessionRequestResource {

    private String macAddress;

    @JsonCreator
    public PxeSessionRequestResource(@JsonProperty("macAddress") String macAddress) {
        this.macAddress = macAddress;
    }

    public String getMacAddress() {
        return macAddress;
    }
}
