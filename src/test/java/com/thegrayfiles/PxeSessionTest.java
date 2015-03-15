package com.thegrayfiles;

import com.thegrayfiles.resource.PxeSessionRequestResource;
import com.thegrayfiles.resource.PxeSessionResource;
import com.thegrayfiles.resource.RootResource;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.client.RestTemplate;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;


@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest({"server.port:0"})
// See http://stackoverflow.com/questions/25537436/integration-testing-a-spring-boot-web-app-with-testng
@TestExecutionListeners(inheritListeners = false, listeners = {
        DependencyInjectionTestExecutionListener.class,
        DirtiesContextTestExecutionListener.class })
@Test
public class PxeSessionTest extends AbstractTestNGSpringContextTests {

    @Value("${local.server.port}")
    private int serverPort;

    private RestTemplate template = new TestRestTemplate();

    @Test
    public void pxeSessionGeneratesMacConfigFile() throws IOException {
        String macAddress = "00:11:22:33:44:55";
        String macAddressFilename = "01-" + macAddress.replaceAll("[:]", "-") + ".cfg";
        ResponseEntity<PxeSessionResource> session = createPxeSessionForMacAddress(macAddress);
        assertEquals(session.getStatusCode().value(), 200);

        File macAddressFile = new File("/tftpboot/pxe/pxelinux.cfg/" + macAddressFilename);
        macAddressFile.deleteOnExit();

        String macAddressFileContent = FileUtils.readFileToString(macAddressFile);
        assertTrue(macAddressFile.exists(), "Mac Address file " + macAddressFile + "should exist.");
        assertTrue(macAddressFileContent.startsWith("default menu.c32"),
                "Unexpected MAC address file content: " + macAddressFileContent);
    }

    private ResponseEntity<PxeSessionResource> createPxeSessionForMacAddress(String macAddress) {
        String url = "http://127.0.0.1:" + serverPort;
        RootResource root = template.getForEntity(url, RootResource.class).getBody();
        return template.postForEntity(root.getLink("session").getHref(), new PxeSessionRequestResource(macAddress), PxeSessionResource.class);
    }

    /**
     *         RestTemplate template = new RestTemplate();
     RootResource root = template.getForEntity("http://localhost:8080/", Roo\
     tResource.class).getBody();
     PsodResource psod = template.getForEntity(root.getLink("psod").getHref(\
     ) + "?vm=" + vm, PsodResource.class).getBody();
     return psod.getText();
     root@pxe:~# ls /var/www/ks/auto-esxhost/
     esxi-v55-112.cfg
     //Mar 15 11:41:49 pxe in.tftpd[7034]: RRQ from 10.100.12.178 filename pxe/pxelinux.cfg/01-78-2b-cb-28-07-a6
     root@pxe:~# ls /tftpboot/pxe/pxelinux.cfg/
     01-00-50-56-9c-36-c5  block  default
     root@pxe:~# cat /tftpboot/pxe/pxelinux.cfg/01-00-50-56-9c-36-c5
     default menu.c32
     prompt 0
     timeout 10

     menu title PXE Boot Menu

     label local
     menu label - Install VMware ESXi 5.5 - Fully Configured
     kernel esxi-5.5/mboot.c32
     append -c esxi-5.5/boot.cfg ks=http://10.100.20.49/ks/auto-esxhost/esxi-v55-112.cfg

     label local
     menu label ^1 - Install VMware ESXi 4.1 - Fully Configured
     kernel esxi-4.1/mboot.c32
     append esxi-4.1/vmkboot.gz ks=http://10.100.20.49/ks/auto-esxhost/esxi-v55-112.cfg --- esxi-4.1/vmkernel.gz --- esxi-4.1/sys.vgz --- esxi-4.1/cim.vgz --- esxi-4.1/ienviron.vgz --- esxi-4.1/install.vgz

     label local
     menu label ^1 - Install VMware ESXi 5.0 - Fully Configured
     kernel esxi-5.0/mboot.c32
     append -c esxi-5.0/boot.cfg ks=http://10.100.20.49/ks/auto-esxhost/esxi-v55-112.cfg

     label local
     menu label ^2 - Install VMware ESXi 5.1 - Fully Configured
     kernel esxi-5.1/mboot.c32
     append -c esxi-5.1/boot.cfg ks=http://10.100.20.49/ks/auto-esxhost/esxi-v55-112.cfg
     */
}
