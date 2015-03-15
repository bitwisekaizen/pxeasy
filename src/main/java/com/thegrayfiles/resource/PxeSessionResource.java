package com.thegrayfiles.resource;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.hateoas.ResourceSupport;

import java.util.UUID;

public class PxeSessionResource extends ResourceSupport {

    private final String macAddress;
    private String uuid;

    @JsonCreator
    public PxeSessionResource(@JsonProperty("macAddress") String macAddress) {
        this.macAddress = macAddress;
        this.uuid = UUID.randomUUID().toString();
    }

    public String getUuid() {
        return uuid;
    }

    public String getMacAddress() {
        return macAddress;
    }
}
