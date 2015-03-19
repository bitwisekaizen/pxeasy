package com.thegrayfiles.service;

import com.thegrayfiles.ApplicationConfig;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Service
public class PxeFileService {

    private ClassPathResource macTemplate;
    private ClassPathResource kickstartTemplate;
    private ApplicationConfig config;

    @Autowired
    public PxeFileService(ApplicationConfig config) {
        this.config = config;
        macTemplate = new ClassPathResource("mac");
        kickstartTemplate = new ClassPathResource("kickstart.cfg");
    }

    public void createMacAddressConfiguration(String macAddress) {
        try {
            File tempFile = File.createTempFile("temp", "file");
            tempFile.deleteOnExit();
            FileUtils.copyURLToFile(macTemplate.getURL(), tempFile);
            List<String> fileStrings = FileUtils.readLines(tempFile);
            fileStrings.set(fileStrings.size() - 1, fileStrings.get(fileStrings.size() - 1).replaceAll("test", macAddress.replaceAll("[:]", "-")));
            FileUtils.writeLines(convertMacAddressToFile(macAddress), fileStrings);
        } catch (IOException e) {
            Logger.getRootLogger().error(e);
        }
    }

    private File convertMacAddressToFile(String macAddress) {
        return new File(config.getPxePath() + "/01-" + macAddress.replaceAll("[:]", "-"));
    }

    public void createKickstartConfiguration(String macAddress) {
        try {
            FileUtils.copyURLToFile(kickstartTemplate.getURL(), new File(config.getKickstartPath() + "/" + macAddress.replaceAll("[:]", "-") + ".cfg"));
        } catch (IOException e) {
            Logger.getRootLogger().error(e);
        }
    }

    public void deleteMacAddressConfiguration(String macAddressFile) {
        try {
            Logger.getRootLogger().info("Attempting to delete files.");
            FileUtils.forceDelete(new File(config.getPxePath() + "/" + macAddressFile));
        } catch (IOException e) {
            Logger.getRootLogger().error(e);
        }
    }
}
