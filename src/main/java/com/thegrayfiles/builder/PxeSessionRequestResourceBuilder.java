package com.thegrayfiles.builder;

import com.thegrayfiles.resource.PxeSessionRequestResource;
import org.apache.commons.lang3.builder.Builder;

public class PxeSessionRequestResourceBuilder implements Builder<PxeSessionRequestResource> {

    private EsxConfigurationResourceBuilder esxConfigBuilder;
    private String macAddress;

    private PxeSessionRequestResourceBuilder(String macAddress) {
        this.macAddress = macAddress;
        this.esxConfigBuilder = EsxConfigurationResourceBuilder.anEsxConfiguration();
    }

    public static PxeSessionRequestResourceBuilder aSessionRequest(String macAddress) {
        return new PxeSessionRequestResourceBuilder(macAddress);
    }

    @Override
    public PxeSessionRequestResource build() {
        return new PxeSessionRequestResource(macAddress, esxConfigBuilder.build());
    }

    public PxeSessionRequestResourceBuilder withEsxConfiguration(EsxConfigurationResourceBuilder esxConfigBuilder) {
        this.esxConfigBuilder = esxConfigBuilder;
        return this;
    }
}
