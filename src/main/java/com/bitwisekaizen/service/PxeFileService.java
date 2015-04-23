package com.bitwisekaizen.service;

import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;
import com.bitwisekaizen.ApplicationConfig;
import com.bitwisekaizen.repository.SessionRepository;
import com.bitwisekaizen.resource.EsxConfigurationResource;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

@Service
public class PxeFileService {

    private ClassPathResource macTemplate;
    private ClassPathResource kickstartTemplate;
    private ApplicationConfig config;
    private SessionRepository repository;

    @Autowired
    public PxeFileService(ApplicationConfig config, SessionRepository repository) {
        this.config = config;
        this.repository = repository;
        macTemplate = new ClassPathResource("mac");
        kickstartTemplate = new ClassPathResource("kickstart.cfg");
    }

    public void createMacAddressConfiguration(String macAddress, String version) {
        try {
            Map<String, Object> scopes = new HashMap<String, Object>();
            scopes.put("kickstartFile", macAddress.replaceAll("[:]", "-") + ".cfg");
            scopes.put("version", version);
            scopes.put("pxeUrl", config.getPxeUrl());
            FileOutputStream fos = new FileOutputStream(convertMacAddressToFile(macAddress));
            Writer writer = new OutputStreamWriter(fos);
            MustacheFactory mf = new DefaultMustacheFactory();
            Mustache mustache = mf.compile(new InputStreamReader(macTemplate.getInputStream()), "mac");
            mustache.execute(writer, scopes);
            writer.flush();
        } catch (IOException e) {
            Logger.getRootLogger().error(e);
        }
    }

    private File convertMacAddressToFile(String macAddress) {
        return new File(config.getPxePath() + "/01-" + macAddress.replaceAll("[:]", "-"));
    }

    public void createKickstartConfiguration(String macAddress, EsxConfigurationResource esxConfig) {
        try {
            Map<String, Object> scopes = new HashMap<String, Object>();
            scopes.put("ip", esxConfig.getIp());
            scopes.put("hostname", esxConfig.getHostname());
            scopes.put("password", esxConfig.getPassword());
            scopes.put("gateway", esxConfig.getGateway());
            scopes.put("netmask", esxConfig.getNetmask());
            scopes.put("pxeUrl", config.getPxeUrl());
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
