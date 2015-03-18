package com.thegrayfiles.controller;

import com.thegrayfiles.resource.PxeSessionRequestResource;
import com.thegrayfiles.resource.PxeSessionResource;
import com.thegrayfiles.service.PxeFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@RestController
@RequestMapping("session")
public class PxeSessionController {

    private PxeFileService fileCreator;

    @Autowired
    public PxeSessionController(PxeFileService fileCreator) {
        this.fileCreator = fileCreator;
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<PxeSessionResource> create(@RequestBody PxeSessionRequestResource request) {
        String macAddress = request.getMacAddress();
        PxeSessionResource session = new PxeSessionResource(macAddress);
        session.add(linkTo(methodOn(PxeSessionController.class).getByUuid(session.getUuid())).withSelfRel());
        fileCreator.createMacAddressConfiguration(macAddress);
        fileCreator.createKickstartConfiguration(macAddress);
        return new ResponseEntity<PxeSessionResource>(session, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{uuid}")
    public ResponseEntity<PxeSessionResource> getByUuid(@PathVariable String uuid) {
        return null;
    }
}
