package com.thegrayfiles.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class SyslogParserService {

    private PxeFileService pxeFileService;
    private Map<String, String> ipToMacFileMappings;

    @Autowired
    public SyslogParserService(PxeFileService pxeFileService) {
        this.pxeFileService = pxeFileService;
        this.ipToMacFileMappings = new HashMap<String, String>();
    }

    public void parse(String line) {
        String macAddressFileRegexp = ".*?RRQ from (.*?) filename.*?pxelinux.cfg/(01(?:-[\\da-fA-F]{2}){6}).*";
        String toolsRegexp = ".*?RRQ from (.*?) .*?pxe/esxi-5.5.0/tools.t00";
        if (line.matches(macAddressFileRegexp)) {
            ipToMacFileMappings.put(line.replaceAll(macAddressFileRegexp, "$1"), line.replaceAll(macAddressFileRegexp, "$2"));
        } else if (line.matches(toolsRegexp)) {
            String macFile = ipToMacFileMappings.remove(line.replaceAll(toolsRegexp, "$1"));
            // not thread safe
            if (macFile != null) {
                pxeFileService.deleteMacAddressConfiguration(macFile);
            }
        }
    }
}
