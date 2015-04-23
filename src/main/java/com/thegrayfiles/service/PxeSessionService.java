package com.thegrayfiles.service;

import com.thegrayfiles.entity.PxeInstallEntity;
import com.thegrayfiles.exception.DuplicateSessionException;
import com.thegrayfiles.exception.SessionNotFound;
import com.thegrayfiles.repository.SessionRepository;
import com.thegrayfiles.resource.EsxConfigurationResource;
import com.thegrayfiles.resource.PxeSessionResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class PxeSessionService {

    private PxeFileService fileService;
    private SessionRepository repository;

    @Autowired
    public PxeSessionService(PxeFileService fileService, SessionRepository repository) {
        this.fileService = fileService;
        this.repository = repository;
    }

    public PxeSessionResource createSession(String macAddress, EsxConfigurationResource config) throws DuplicateSessionException {
        fileService.createMacAddressConfiguration(macAddress, config.getVersion());
        fileService.createKickstartConfiguration(macAddress, config);

        if (repository.findByMacAddress(macAddress).size() != 0) {
            throw new DuplicateSessionException();
        }

        PxeInstallEntity entity = new PxeInstallEntity(macAddress);
        repository.save(entity);
        return new PxeSessionResource(macAddress, UUID.fromString(entity.getUuid()));
    }

    public PxeSessionResource getSession(String uuid) throws SessionNotFound {
        List<PxeInstallEntity> sessions = repository.findByUuid(uuid);
        if (sessions.size() == 0) {
            throw new SessionNotFound(uuid);
        }
        PxeInstallEntity entity = sessions.get(0);
        return new PxeSessionResource(entity.getMacAddress(), UUID.fromString(entity.getUuid()));
    }

    public List<PxeSessionResource> getAllSessions() {
        List<PxeSessionResource> list = new ArrayList<PxeSessionResource>();
        for (PxeInstallEntity session : repository.findAll()) {
            list.add(new PxeSessionResource(session.getMacAddress(), UUID.fromString(session.getUuid())));
        }
        return list;
    }

    public void delete(String uuid) {
        PxeInstallEntity entity = repository.findByUuid(uuid).get(0);
        repository.deleteByMacAddress(entity.getMacAddress());
        // @todo change file api
        fileService.deleteMacAddressConfiguration("01-" + entity.getMacAddress().replaceAll("[:]", "-"));
    }

    public void deleteByMacAddress(String macAddress) {
        delete(repository.findByMacAddress(macAddress).get(0).getUuid());
    }
}
