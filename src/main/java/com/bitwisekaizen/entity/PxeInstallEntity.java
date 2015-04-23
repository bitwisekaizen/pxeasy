package com.bitwisekaizen.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.UUID;

@Entity(name = "pxeinstall")
public class PxeInstallEntity {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private long id;

    private String uuid;
    private String macAddress;

    protected PxeInstallEntity() {
        this.uuid = UUID.randomUUID().toString();
    }

    public PxeInstallEntity(String macAddress) {
        this();
        this.macAddress = macAddress;
    }

    @Override
    public String toString() {
        return String.format("PxeInstall[id=%d, uuid='%s', macAddress='%s']", id, uuid, macAddress);
    }

    public String getUuid() {
        return uuid;
    }

    public String getMacAddress() {
        return macAddress;
    }
}