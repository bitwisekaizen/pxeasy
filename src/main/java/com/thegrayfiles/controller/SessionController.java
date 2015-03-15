package com.thegrayfiles.controller;

import com.thegrayfiles.resource.PxeSessionResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@RestController
@RequestMapping("psod")
public class SessionController {

    @RequestMapping
    public ResponseEntity<PxeSessionResource> psod(@RequestParam String vm) {
        PxeSessionResource psod = new PxeSessionResource(vm);
        psod.add(linkTo(methodOn(SessionController.class).psod(vm)).withSelfRel());
        return new ResponseEntity<PxeSessionResource>(psod, HttpStatus.OK);
    }
}
