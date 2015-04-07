package com.thegrayfiles.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.UUID;

@Entity
public class SessionEntity {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private long id;

    private String uuid;
    private String macAddress;

    protected SessionEntity() {
        this.uuid = UUID.randomUUID().toString();
    }

    public SessionEntity(String macAddress) {
        this();
        this.macAddress = macAddress;
    }

    @Override
    public String toString() {
        return String.format("Customer[id=%d, uuid='%s', macAddress='%s']", id, uuid, macAddress);
    }

    public String getUuid() {
        return uuid;
    }

    public String getMacAddress() {
        return macAddress;
    }
}