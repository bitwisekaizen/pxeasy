package com.thegrayfiles.service;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class SyslogParserService {

    private Logger logger = Logger.getLogger(SyslogParserService.class);

    private Map<String, String> ipToMacFileMappings;
    private PxeSessionService sessionService;
    private boolean macAddressDetected;
    private boolean macAddressFileRemoved;

    @Autowired
    public SyslogParserService(PxeSessionService sessionService) {
        this.sessionService = sessionService;
        this.ipToMacFileMappings = new ConcurrentHashMap<String, String>();
    }

    public void parse(String line) {
        String macAddressFileRegexp = ".*?RRQ from (.*?) filename.*?pxelinux.cfg/(01(?:-[\\da-fA-F]{2}){6}).*";
        String toolsRegexp = ".*?RRQ from (.*?) .*?pxe/(.*?)/tools.t00";
        if (line.matches(macAddressFileRegexp)) {
            ipToMacFileMappings.put(line.replaceAll(macAddressFileRegexp, "$1"), line.replaceAll(macAddressFileRegexp, "$2"));
            macAddressDetected = true;
        } else if (line.matches(toolsRegexp)) {
            String macFile = ipToMacFileMappings.remove(line.replaceAll(toolsRegexp, "$1"));
            // not thread safe
            if (macFile != null) {
                sessionService.deleteByMacAddress(macFile.replaceAll("^01-(.*)", "$1").replaceAll("-", ":"));
                macAddressFileRemoved = true;
            }
        }
    }

    public boolean isMacAddressDetected() {
        boolean macAddressDetected = this.macAddressDetected;
        this.macAddressDetected = false;
        return macAddressDetected;
    }

    public boolean isMacAddressFileRemoved() {
        boolean macAddressFileRemoved = this.macAddressFileRemoved;
        this.macAddressFileRemoved = false;
        return macAddressFileRemoved;
    }
}
