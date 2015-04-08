package com.thegrayfiles.service;

import com.thegrayfiles.entity.SessionEntity;
import com.thegrayfiles.repository.SessionRepository;
import com.thegrayfiles.resource.EsxConfigurationResource;
import com.thegrayfiles.resource.PxeSessionResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class PxeSessionService {

    private PxeFileService fileService;
    private SessionRepository repository;

    @Autowired
    public PxeSessionService(PxeFileService fileService, SessionRepository repository) {
        this.fileService = fileService;
        this.repository = repository;
    }

    public PxeSessionResource createSession(String macAddress, EsxConfigurationResource config) {
        fileService.createMacAddressConfiguration(macAddress, config.getVersion());
        fileService.createKickstartConfiguration(macAddress, config.getIp(), config.getPassword());
        SessionEntity entity = new SessionEntity(macAddress);
        repository.save(entity);
        return new PxeSessionResource(macAddress, UUID.fromString(entity.getUuid()));
    }

    public PxeSessionResource getSession(String uuid) {
        SessionEntity entity = repository.findByUuid(uuid).get(0);
        return new PxeSessionResource(entity.getMacAddress(), UUID.fromString(entity.getUuid()));
    }

    public List<PxeSessionResource> getAllSessions() {
        List<PxeSessionResource> list = new ArrayList<PxeSessionResource>();
        for (SessionEntity session : repository.findAll()) {
            list.add(new PxeSessionResource(session.getMacAddress(), UUID.fromString(session.getUuid())));
        }
        return list;
    }
}
