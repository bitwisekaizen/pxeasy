package com.thegrayfiles.service;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;

@Service
public class PxeFileService {

    private ClassPathResource macTemplate;
    private ClassPathResource kickstartTemplate;

    public PxeFileService() {
        macTemplate = new ClassPathResource("mac");
        kickstartTemplate = new ClassPathResource("kickstart.cfg");
    }

    public void createMacAddressConfiguration(String macAddress) {
        try {
            FileUtils.copyURLToFile(macTemplate.getURL(), convertMacAddressToFile(macAddress));
        } catch (IOException e) {
            Logger.getRootLogger().error(e);
        }
    }

    private File convertMacAddressToFile(String macAddress) {
        return new File("/tftpboot/pxe/pxelinux.cfg/01-" + macAddress.replaceAll("[:]", "-"));
    }

    public void createKickstartConfiguration() {
        try {
            FileUtils.copyURLToFile(kickstartTemplate.getURL(), new File("/var/www/ks/auto-esxhost/test.cfg"));
        } catch (IOException e) {
            Logger.getRootLogger().error(e);
        }
    }

    public void deleteMacAddressConfiguration(String macAddressFile) {
        try {
            Logger.getRootLogger().info("Attempting to delete files.");
            FileUtils.forceDelete(new File("/tftpboot/pxe/pxelinux.cfg/" + macAddressFile));
        } catch (IOException e) {
            Logger.getRootLogger().error(e);
        }
    }
}
