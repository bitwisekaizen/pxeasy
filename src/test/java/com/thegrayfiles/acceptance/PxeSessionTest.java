package com.thegrayfiles.acceptance;

import com.google.common.io.Files;
import com.thegrayfiles.Application;
import com.thegrayfiles.ApplicationConfig;
import com.thegrayfiles.resource.PxeSessionRequestResource;
import com.thegrayfiles.resource.PxeSessionResource;
import com.thegrayfiles.resource.RootResource;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.integration.file.tail.FileTailingMessageProducerSupport;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.client.RestTemplate;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static org.testng.Assert.*;


@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest({"server.port:0"})
// See http://stackoverflow.com/questions/25537436/acceptance-testing-a-spring-boot-web-app-with-testng
@TestExecutionListeners(inheritListeners = false, listeners = {
        DependencyInjectionTestExecutionListener.class,
        DirtiesContextTestExecutionListener.class })
@Test
public class PxeSessionTest extends AbstractTestNGSpringContextTests {

    @Value("${local.server.port}")
    private int serverPort;

    @Autowired
    private ApplicationConfig config;

    @Autowired
    private FileTailingMessageProducerSupport tailer;

    private RestTemplate template = new TestRestTemplate();

    private File syslogFile;

    @BeforeMethod
    public void setup() throws IOException {
        String pxePath = Files.createTempDir().getAbsolutePath();
        config.setPxePath(pxePath);
        String kickstartPath = Files.createTempDir().getAbsolutePath();
        config.setKickstartPath(kickstartPath);
        syslogFile = File.createTempFile("sys", "log");
        tailer.stop();
        tailer.setFile(syslogFile);
        tailer.start();
    }

    @Test
    public void pxeSessionGeneratesMacConfigFile() throws IOException {
        String macAddress = "00:1a:2b:3c:4d:5e";
        String macAddressFilename = "01-" + macAddress.replaceAll("[:]", "-");

        ResponseEntity<PxeSessionResource> session = createPxeSessionForMacAddress(macAddress);
        assertEquals(session.getStatusCode().value(), 200);

        File macAddressFile = new File(config.getPxePath() + "/" + macAddressFilename);
        macAddressFile.deleteOnExit();
        assertTrue(macAddressFile.exists(), "Mac Address file " + macAddressFile.getAbsolutePath() + " should exist.");

        List<String> macAddressFileContent = FileUtils.readLines(macAddressFile);
        assertTrue(macAddressFileContent.get(0).startsWith("default menu.c32"),
                "Unexpected first line of MAC address file: " + macAddressFileContent.get(0));

        assertTrue(macAddressFileContent.get(macAddressFileContent.size() - 1).matches("^\\s+append.*?" + macAddress.replaceAll("[:]", "-")+ "\\.cfg$"),
                "Unexpected last line of MAC address file: " + macAddressFileContent.get(macAddressFileContent.size() - 1));
    }

    @Test
    public void pxeSessionGeneratesKickstartFile() throws IOException {
        String macAddress = "00:11:22:33:44:55";
        String kickstartFilename = config.getKickstartPath() + "/" + macAddress.replaceAll("[:]", "-") + ".cfg";
        ResponseEntity<PxeSessionResource> session = createPxeSessionForMacAddress(macAddress);
        assertEquals(session.getStatusCode().value(), 200);

        File kickstartFile = new File(kickstartFilename);
        kickstartFile.deleteOnExit();

        String kickstartFileContent = FileUtils.readFileToString(kickstartFile);

        assertTrue(kickstartFile.exists(), "Kickstart file " + kickstartFile.getAbsolutePath() + " should exist.");
        assertTrue(kickstartFileContent.startsWith("accepteula"),
                "Unexpected kickstart file content: " + kickstartFileContent);
    }

    @Test(timeOut = 5*1000)
    public void configFilesDeletedWhenSyslogUpdatedWithMacAddress() throws IOException, InterruptedException {
        String macAddress = "00:1a:2b:3c:4d:5e";
        String macAddressFilename = "01-" + macAddress.replaceAll("[:]", "-");
        String kickstartFilename = config.getKickstartPath() + "/" + macAddress.replaceAll("[:]", "-") + ".cfg";

        ResponseEntity<PxeSessionResource> session = createPxeSessionForMacAddress(macAddress);
        assertEquals(session.getStatusCode().value(), 200);

        File kickstartFile = new File(kickstartFilename);
        kickstartFile.deleteOnExit();
        File macAddressFile = new File(config.getPxePath() + "/" + macAddressFilename);
        macAddressFile.deleteOnExit();

        String macAddressSyslog = "Mar 15 11:41:49 pxe in.tftpd[7034]: RRQ from 10.100.12.178 filename pxe/pxelinux.cfg/" + macAddressFilename + "\n";
        String toolsSyslog = "Mar 15 11:41:49 pxe in.tftpd[7034]: RRQ from 10.100.12.178 filename pxe/esxi-5.5.0/tools.t00\n";
        while (macAddressFile.exists()) {
            FileUtils.writeStringToFile(syslogFile, macAddressSyslog);
            Thread.sleep(1000);
            FileUtils.writeStringToFile(syslogFile, toolsSyslog);
        }

        assertTrue(kickstartFile.exists(), "Kickstart file " + kickstartFile.getAbsolutePath() + " should exist.");
        assertFalse(macAddressFile.exists(), "Mac Address file " + macAddressFile.getAbsolutePath() + " should not exist.");
    }

    private ResponseEntity<PxeSessionResource> createPxeSessionForMacAddress(String macAddress) {
        String url = "http://127.0.0.1:" + serverPort;
        RootResource root = template.getForEntity(url, RootResource.class).getBody();
        return template.postForEntity(root.getLink("session").getHref(), new PxeSessionRequestResource(macAddress), PxeSessionResource.class);
    }
}
