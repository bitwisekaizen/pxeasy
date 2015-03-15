package com.thegrayfiles.controller;

import com.thegrayfiles.resource.RootResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@RestController
@RequestMapping("/")
public class RootController {

    @RequestMapping
    public ResponseEntity<RootResource> root() {
        RootResource root = new RootResource();
        root.add(linkTo(methodOn(RootController.class).root()).withSelfRel());
        root.add(linkTo(PxeSessionController.class).withRel("session"));
        return new ResponseEntity<RootResource>(root, HttpStatus.OK);
    }
}

