package com.thegrayfiles.controller;

import com.thegrayfiles.resource.PxeSessionRequestResource;
import com.thegrayfiles.resource.PxeSessionResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@RestController
@RequestMapping("session")
public class PxeSessionController {

    @RequestMapping
    public ResponseEntity<PxeSessionResource> create(@RequestBody PxeSessionRequestResource request) {
        PxeSessionResource session = new PxeSessionResource(request.getMacAddress());
        session.add(linkTo(methodOn(PxeSessionController.class).create(request)).withSelfRel());
        return new ResponseEntity<PxeSessionResource>(session, HttpStatus.OK);
    }
}
