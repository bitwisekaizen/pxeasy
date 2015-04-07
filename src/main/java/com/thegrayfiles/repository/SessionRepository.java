package com.thegrayfiles.repository;

import com.thegrayfiles.entity.SessionEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface SessionRepository extends CrudRepository<SessionEntity, Long> {
    List<SessionEntity> findByUuid(String uuid);
}
