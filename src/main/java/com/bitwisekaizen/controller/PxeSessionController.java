package com.bitwisekaizen.controller;

import com.bitwisekaizen.exception.DuplicateSessionException;
import com.bitwisekaizen.exception.SessionNotFound;
import com.bitwisekaizen.resource.EsxConfigurationResource;
import com.bitwisekaizen.resource.PxeSessionRequestResource;
import com.bitwisekaizen.resource.PxeSessionResource;
import com.bitwisekaizen.service.PxeSessionService;
import com.bitwisekaizen.validation.DtoValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.ConstraintViolationException;
import java.util.List;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@RestController
@RequestMapping("session")
public class PxeSessionController {

    private PxeSessionService sessionService;
    private DtoValidator dtoValidator;

    @Autowired
    public PxeSessionController(PxeSessionService fileService, DtoValidator dtoValidator) {
        this.sessionService = fileService;
        this.dtoValidator = dtoValidator;
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<PxeSessionResource> create(@RequestBody PxeSessionRequestResource request) {
        // @todo: need to add in response interceptor to avoid manually catching these exceptions
        try {
            dtoValidator.validate(request);
        } catch (ConstraintViolationException e) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }

        EsxConfigurationResource esxConfig = request.getConfig();
        String macAddress = request.getMacAddress();
        try {
            PxeSessionResource session = sessionService.createSession(macAddress, esxConfig);
            session.add(linkTo(methodOn(PxeSessionController.class).getByUuid(session.getUuid())).withSelfRel());
            return new ResponseEntity<PxeSessionResource>(session, HttpStatus.OK);
        } catch (DuplicateSessionException e) {
            return new ResponseEntity(HttpStatus.FORBIDDEN);
        }
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
        sessionService.deleteByUuid(uuid);
    }
}
