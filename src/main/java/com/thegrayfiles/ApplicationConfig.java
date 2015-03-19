package com.thegrayfiles;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.embedded.EmbeddedServletContainerFactory;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationConfig {

    @Value("${pxe.path}")
    private String pxePath;

    @Value("${kickstart.path}")
    private String kickstartPath;

    private String syslogPath = "/var/log/syslog";

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

    public void setSyslogPath(String syslogPath) {
        this.syslogPath = syslogPath;
    }

    public String getSyslogPath() {
        return syslogPath;
    }
}
