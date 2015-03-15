package com.thegrayfiles.resource;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.hateoas.ResourceSupport;

public class PxeSessionResource extends ResourceSupport {

    private String uuid;

    @JsonCreator
    public PxeSessionResource(@JsonProperty("uuid") String uuid) {
        this.uuid = uuid;
    }

    public String getUuid() {
        return uuid;
    }
}
