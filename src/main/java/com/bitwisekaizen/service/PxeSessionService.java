package com.bitwisekaizen.service;

import com.bitwisekaizen.entity.PxeInstallEntity;
import com.bitwisekaizen.exception.DuplicateSessionException;
import com.bitwisekaizen.exception.SessionNotFound;
import com.bitwisekaizen.repository.SessionRepository;
import com.bitwisekaizen.resource.EsxConfigurationResource;
import com.bitwisekaizen.resource.PxeSessionResource;
import org.apache.log4j.Logger;
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

    private Logger logger = Logger.getLogger(PxeSessionService.class);

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

    public void delete(PxeInstallEntity install) {
        repository.deleteByMacAddress(install.getMacAddress());
        // @todo change file api
        fileService.deleteMacAddressConfiguration("01-" + install.getMacAddress().replaceAll("[:]", "-"));
    }

    public void deleteByMacAddress(String macAddress) {
        List<PxeInstallEntity> installations = repository.findByMacAddress(macAddress);
        if (installations.size() > 0) {
            delete(installations.get(0));
        }
        logger.warn("Trying to delete installation for mac address " + macAddress + " that doesn't exist.");
        // @todo throw if session doesn't exist
    }

    public void deleteByUuid(String uuid) {
        List<PxeInstallEntity> installations = repository.findByUuid(uuid);
        if (installations.size() > 0) {
            delete(installations.get(0));
        }
        // @todo throw if install doesn't exist
        logger.warn("Trying to delete installation for uuid " + uuid + " that doesn't exist.");
    }
}
