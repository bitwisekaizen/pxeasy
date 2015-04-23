package com.bitwisekaizen.repository;

import com.bitwisekaizen.entity.PxeInstallEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface SessionRepository extends CrudRepository<PxeInstallEntity, Long> {
    List<PxeInstallEntity> findByUuid(String uuid);

    void deleteByMacAddress(String macAddress);

    List<PxeInstallEntity> findByMacAddress(String macAddress);
}
