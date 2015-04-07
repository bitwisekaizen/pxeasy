package com.thegrayfiles.resource;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.hateoas.ResourceSupport;

import java.util.UUID;

public class PxeSessionResource extends ResourceSupport {

    private final String macAddress;
    private String uuid;

    @JsonCreator
    public PxeSessionResource(@JsonProperty("macAddress") String macAddress, @JsonProperty("uuid") UUID uuid) {
        this.macAddress = macAddress;
        this.uuid = uuid.toString();
    }

    public String getUuid() {
        return uuid;
    }

    public String getMacAddress() {
        return macAddress;
    }
}
