package com.thegrayfiles.service;

import org.apache.commons.io.FileUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;

@Service
public class PxeFileCreator {

    private ClassPathResource template;

    public PxeFileCreator() {
        template = new ClassPathResource("mac.cfg");
    }

    public void createMacAddressConfiguration(String macAddress) {
        try {
            FileUtils.copyFile(template.getFile(), convertMacAddressToFile(macAddress));
        } catch (IOException e) {
            // throw relevant wrapped exception here.
        }
    }

    private File convertMacAddressToFile(String macAddress) {
        return new File("/tftpboot/pxe/pxelinux.cfg/01-" + macAddress.replaceAll("[:]", "-") + ".cfg");
    }
}
