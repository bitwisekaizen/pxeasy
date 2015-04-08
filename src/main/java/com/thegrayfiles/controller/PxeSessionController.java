package com.thegrayfiles.controller;

import com.thegrayfiles.exception.SessionNotFound;
import com.thegrayfiles.resource.EsxConfigurationResource;
import com.thegrayfiles.resource.PxeSessionRequestResource;
import com.thegrayfiles.resource.PxeSessionResource;
import com.thegrayfiles.service.PxeSessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@RestController
@RequestMapping("session")
public class PxeSessionController {

    private PxeSessionService sessionService;

    @Autowired
    public PxeSessionController(PxeSessionService fileService) {
        this.sessionService = fileService;
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<PxeSessionResource> create(@RequestBody PxeSessionRequestResource request) {
        EsxConfigurationResource esxConfig = request.getConfig();
        String macAddress = request.getMacAddress();
        PxeSessionResource session = sessionService.createSession(macAddress, esxConfig);
        session.add(linkTo(methodOn(PxeSessionController.class).getByUuid(session.getUuid())).withSelfRel());
        return new ResponseEntity<PxeSessionResource>(session, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{uuid}")
    public ResponseEntity<PxeSessionResource> getByUuid(@PathVariable String uuid) {
        try {
            return new ResponseEntity<PxeSessionResource>(sessionService.getSession(uuid), HttpStatus.OK);
        } catch (SessionNotFound e) {
            return new ResponseEntity<PxeSessionResource>(HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<PxeSessionResource>> getAllSessions() {
        List<PxeSessionResource> sessions = sessionService.getAllSessions();
        for (PxeSessionResource session : sessions) {
            session.add(linkTo(methodOn(PxeSessionController.class).getByUuid(session.getUuid())).withSelfRel());
        }
        return new ResponseEntity<List<PxeSessionResource>>(sessions, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/{uuid}")
    public void deleteSession(@PathVariable String uuid) {
        sessionService.delete(uuid);
    }
}
