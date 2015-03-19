package com.thegrayfiles;

import org.springframework.boot.context.embedded.EmbeddedServletContainerFactory;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationConfig {

    private String pxePath = "/tftpboot/pxe/pxelinux.cfg";

    private String kickstartPath = "/var/www/ks/auto-esxhost";

    public String getPxePath() {
        return pxePath;
    }

    @Bean
    public EmbeddedServletContainerFactory servletContainer() {
        TomcatEmbeddedServletContainerFactory factory = new TomcatEmbeddedServletContainerFactory();
        //factory.setPort(9000);
        //factory.setSessionTimeout(10, TimeUnit.MINUTES);
        //factory.addErrorPages(new ErrorPage(HttpStatus.404, "/notfound.html"));
        return factory;
    }

    public void setPxePath(String pxePath) {
        this.pxePath = pxePath;
    }

    public String getKickstartPath() {
        return kickstartPath;
    }

    public void setKickstartPath(String kickstartPath) {
        this.kickstartPath = kickstartPath;
    }
}
