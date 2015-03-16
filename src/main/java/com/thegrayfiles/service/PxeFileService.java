package com.thegrayfiles.service;

import org.apache.commons.io.FileUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;

@Service
public class PxeFileService {

    private ClassPathResource macTemplate;
    private ClassPathResource kickstartTemplate;

    public PxeFileService() {
        macTemplate = new ClassPathResource("mac.cfg");
        kickstartTemplate = new ClassPathResource("kickstart.ks");
    }

    public void createMacAddressConfiguration(String macAddress) {
        try {
            FileUtils.copyFile(macTemplate.getFile(), convertMacAddressToFile(macAddress));
        } catch (IOException e) {
            // throw relevant wrapped exception here.
        }
    }

    private File convertMacAddressToFile(String macAddress) {
        return new File("/tftpboot/pxe/pxelinux.cfg/01-" + macAddress.replaceAll("[:]", "-") + ".cfg");
    }

    public void createKickstartConfiguration() {
        try {
            FileUtils.copyFile(kickstartTemplate.getFile(), new File("/var/www/ks/auto-esxhost/test.ks"));
        } catch (IOException e) {
            // throw relevant wrapped exception here.
        }
    }
}
