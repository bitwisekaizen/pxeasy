package com.bitwisekaizen.resource;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PxeSessionResource resource = (PxeSessionResource) o;

        if (!uuid.equals(resource.uuid)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + uuid.hashCode();
        return result;
    }
}
