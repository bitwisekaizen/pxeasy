package com.bitwisekaizen;

import org.flywaydb.core.Flyway;
import org.h2.Driver;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.embedded.EmbeddedServletContainerFactory;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.instrument.classloading.InstrumentationLoadTimeWeaver;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

import javax.sql.DataSource;
import java.util.Properties;

@Configuration
public class ApplicationConfig {

    private String pxePath = "/tftpboot/pxe/pxelinux.cfg";

    private String kickstartPath = "/var/www/ks/auto-esxhost";

    @Value("${pxe.url}")
    private String pxeUrl;

    @Value("${pxeasy.db.file:~/pxeasy.db}")
    private String dbFile;

    public String getPxePath() {
        return pxePath;
    }

    @Bean
    public EmbeddedServletContainerFactory servletContainer() {
        return new TomcatEmbeddedServletContainerFactory();
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

    @Bean
    public DataSource dataSource() {
        String jdbcUrl = "jdbc:h2:file:" + dbFile;
        SimpleDriverDataSource ds = new SimpleDriverDataSource();
        ds.setDriverClass(Driver.class);
        ds.setUrl(jdbcUrl);
        ds.setUsername("sa");
        ds.setPassword("");
        return ds;
    }

    @DependsOn("flyway")
    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(DataSource dataSource, JpaVendorAdapter jpaVendorAdapter) {
        LocalContainerEntityManagerFactoryBean lef = new LocalContainerEntityManagerFactoryBean();
        lef.setDataSource(dataSource);
        lef.setJpaVendorAdapter(jpaVendorAdapter);
        lef.setPackagesToScan("com.bitwisekaizen.entity");
        lef.setDataSource(dataSource());
        lef.setJpaProperties(jpaProperties());
        lef.setLoadTimeWeaver(new InstrumentationLoadTimeWeaver());
        return lef;
    }

    private Properties jpaProperties() {
        Properties props = new Properties();
        props.put("hibernate.query.substitutions", "true 'Y', false 'N'");
        props.put("hibernate.hbm2ddl.auto", "validate");
        props.put("hibernate.show_sql", "false");
        props.put("hibernate.format_sql", "true");
        return props;
    }

    @Bean
    public JpaVendorAdapter jpaVendorAdapter() {
        HibernateJpaVendorAdapter adapter = new HibernateJpaVendorAdapter();
        adapter.setShowSql(false);
        adapter.setDatabase(Database.H2);
        return adapter;
    }

    @Bean(initMethod = "migrate")
    public Flyway flyway() {
        Flyway flyway = new Flyway();
        flyway.setBaselineOnMigrate(true);
        flyway.setDataSource(dataSource());
        return flyway;
    }

    public String getPxeUrl() {
        return pxeUrl;
    }

    public void setPxeUrl(String pxeUrl) {
        this.pxeUrl = pxeUrl;
    }
}
