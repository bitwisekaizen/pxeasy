package com.thegrayfiles.service;

import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;
import com.thegrayfiles.ApplicationConfig;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    public void createKickstartConfiguration(String macAddress, String ip) {
        try {
            Map<String, Object> scopes = new HashMap<String, Object>();
            scopes.put("ip", ip);
            scopes.put("hostname", "localhost");
            FileOutputStream fos = new FileOutputStream(new File(config.getKickstartPath() + "/" + macAddress.replaceAll("[:]", "-") + ".cfg"));
            Writer writer = new OutputStreamWriter(fos);
            MustacheFactory mf = new DefaultMustacheFactory();
            Mustache mustache = mf.compile(new InputStreamReader(kickstartTemplate.getInputStream()), "kickstart");
            mustache.execute(writer, scopes);
            writer.flush();
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
