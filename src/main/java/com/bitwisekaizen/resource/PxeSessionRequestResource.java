package com.bitwisekaizen.resource;

import com.bitwisekaizen.validation.ValidationMethod;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.validator.constraints.NotEmpty;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PxeSessionRequestResource {

    // http://stackoverflow.com/questions/4260467/what-is-a-regular-expression-for-a-mac-address
    private static final String REGEX = "^([0-9A-Fa-f]{2}[:-]){5}[0-9A-Fa-f]{2}$";

    Pattern macAddressPattern = Pattern.compile(REGEX);

    @NotEmpty(message = "mac.address.empty")
    private String macAddress;
    private EsxConfigurationResource config;

    @JsonCreator
    public PxeSessionRequestResource(@JsonProperty("macAddress") String macAddress, @JsonProperty("esxConfig") EsxConfigurationResource config) {
        this.macAddress = macAddress;
        this.config = config;
    }

    public String getMacAddress() {
        return macAddress;
    }

    public EsxConfigurationResource getConfig() {
        return config;
    }

    @ValidationMethod(message = "mac.address.malformed")
    public boolean isValid() {
        if (macAddress == null) {
            return false;
        }

        Matcher matcher = macAddressPattern.matcher(macAddress);
        if (matcher.matches()) {
            return true;
        }

        return false;
    }
}
